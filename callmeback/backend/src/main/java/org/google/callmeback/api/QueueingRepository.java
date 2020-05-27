package org.google.callmeback.api;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * QueueingRepository is a custom repository with methods for interacting with reservations inside
 * of a queue.
 */
interface QueueingRepository {
  /**
   * Gets the first caller in line (has the earliest reservation create time of all reservations
   * that do not have any ReservationEvents, i.e. have not already been contacted by an agent) and
   * starts their call.
   *
   * @return the call that was started, or null if there are no calls available to start.
   */
  public Reservation startNextCall();
}

@Component
class QueueingRepositoryImpl implements QueueingRepository {
  private final MongoTemplate mongoTemplate;

  @Value("${defaults.movingAverage.numberOfCalls}")
  private int numberOfCallsToAverage;

  @Value("${defaults.movingAverage.smoothingFactor}")
  private int smoothingFactor;

  @Autowired
  public QueueingRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public Reservation startNextCall() {
    Reservation nextCall = getNextCall();
    if (nextCall == null) {
      return null;
    }

    ReservationWaitTime previousMovingAverage = getMovingAverage();

    /**
     * Note: this makes the (unrealistic) assumption that an agent is immediately connected to the
     * first caller in line. Out of scope for now is a separate locking mechanism to indicate that
     * an agent is working on a caller and no other agent should pick up that caller, but without
     * assuming the agent is immediately connected (instead, they would need to try to reach the
     * caller, and later update the caller's ReservationEvents).
     */
    ReservationEvent connectedEvent = ReservationEvent.newConnectedEvent();
    double newMovingAverage =
        getNewExponentialMovingAverage(nextCall, connectedEvent, previousMovingAverage);

    return updateNextCall(nextCall, connectedEvent, newMovingAverage);
  }

  /**
   * Retrieves the first caller in line as described in {@code startNextCall()} from the database.
   */
  private Reservation getNextCall() {
    /**
     * Note: This query only pulls callers who have no associated events. For the current scope, we
     * don't handle here (or elsewhere) calls that were ATTEMPTED or DISRUPTED but not completed.
     * Once those callers are handled, we should consider whether they should re-enter the queue and
     * be able to be selected by agents here, or if they should be handled separately.
     */
    Sort sort = Sort.by("requestDate").ascending();
    Query nextCallQuery = new Query(Criteria.where("events").is(null)).with(sort).limit(1);
    return mongoTemplate.findOne(nextCallQuery, Reservation.class);
  }

  /**
   * Retrieves the most recent moving average from the database. This means finding the most recent
   * ReservationEventType.CONNECTED event, and returning the moving average stored on the document
   * where that event is found.
   */
  private ReservationWaitTime getMovingAverage() {
    Aggregation agg =
        Aggregation.newAggregation(
            Aggregation.unwind("events"),
            Aggregation.match(Criteria.where("events.type").is(ReservationEventType.CONNECTED)),
            Aggregation.sort(Direction.DESC, "events.date"),
            Aggregation.limit(1),
            Aggregation.project("waitTimeMovingAverage"));

    AggregationResults<ReservationWaitTime> results =
        mongoTemplate.aggregate(agg, "reservation", ReservationWaitTime.class);

    List<ReservationWaitTime> resultList = results.getMappedResults();
    return resultList.stream().findFirst().orElse(null);
  }

  /**
   * Calculates the current exponential moving average. If {@code previousMovingAverage} is not
   * present, returns {@code newValue}.
   *
   * @param smoothingFactor The most common choice is '2'. As this is increased, more recent events
   *     have a higher impact on the moving average.
   * @param observedEvents the number of events that the moving average should consider, expressed
   *     in units used when the previousMovingAverage was calculated
   * @param newValue the actual value being measured at this point in time
   * @param previousMovingAverage the exponential moving average that was calculated during the
   *     previous period.
   */
  private double exponentialMovingAverage(
      double smoothingFactor,
      int observedEvents,
      double newValue,
      OptionalDouble previousMovingAverage) {

    if (observedEvents < 0) {
      throw new IllegalArgumentException("Number of observed events must be positive.");
    } else if (observedEvents == Integer.MAX_VALUE) {
      throw new IllegalArgumentException("Number of observed events is too large.");
    }

    double multiplier = smoothingFactor / (1 + observedEvents);

    if (!previousMovingAverage.isPresent()) {
      return newValue;
    } else {
      return newValue * multiplier + previousMovingAverage.getAsDouble() * (1 - multiplier);
    }
  }

  /**
   * Given a Reservation that has been connected to an agent, and the ReservationEvent of type
   * CONNECTED, updates the existing exponential moving average with the actual call wait time from
   * the provided reservation.
   *
   * @param reservation the Reservation representing the next call to be connected to an agent.
   * @param connectedEvent the ReservationEventType.CONNECTED event created for {$code nextCall}.
   * @param previousMovingAverage the most recent exponential moving average, required to calculate
   *     the new exponential moving average.
   * @return the new exponential moving average.
   */
  private double getNewExponentialMovingAverage(
      Reservation reservation,
      ReservationEvent connectedEvent,
      ReservationWaitTime previousMovingAverage) {

    OptionalDouble waitTimeMovingAverage =
        previousMovingAverage == null
            ? OptionalDouble.empty()
            : OptionalDouble.of(previousMovingAverage.waitTimeMovingAverage);

    double waitTimeMs =
        Duration.between(reservation.requestDate.toInstant(), connectedEvent.date.toInstant())
            .toMillis();

    return exponentialMovingAverage(
        smoothingFactor, numberOfCallsToAverage, waitTimeMs, waitTimeMovingAverage);
  }

  /**
   * Saves the updated call Reservation to MongoDB, inclusive of the most recent wait time moving
   * average.
   *
   * @param nextCall the reservation that has been connected to an agent.
   * @param connectedEvent the event describing the reservation's connection to an agent.
   * @param waitTimeMovingAverage the estimated wait time as an exponential moving average.
   * @return the updated Reservation, with the connectedEvent and waitTimeMovingAverage embedded.
   */
  private ReservationWaitTime updateNextCall(
      Reservation nextCall, ReservationEvent connectedEvent, double waitTimeMovingAverage) {

    Query idQuery = new Query(Criteria.where("_id").is(new ObjectId(nextCall.id)));
    Update update =
        new Update()
            .set("events", Arrays.asList(connectedEvent))
            .set("waitTimeMovingAverage", waitTimeMovingAverage);
    FindAndModifyOptions opts = FindAndModifyOptions.options().returnNew(true);
    return mongoTemplate.findAndModify(
        idQuery, update, opts, ReservationWaitTime.class, "reservation");
  }
}
