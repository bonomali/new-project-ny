package org.google.callmeback.api;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

  @Value("${movingAverage.numberOfCalls}")
  private int numberOfCallsToAverage;

  @Value("${movingAverage.smoothingFactor}")
  private int smoothingFactor;

  @Autowired
  public QueueingRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public Reservation startNextCall() {
    Reservation callToStart = getNextCall();
    if (callToStart == null) {
      return null;
    }

    MovingAverageAggregate storedAverage = getMovingAverage();
    double currentMovingAverage = storedAverage == null ? 0 : storedAverage.waitTimeMovingAverage;
    Update update = createCallStartDatabaseUpdate(callToStart, currentMovingAverage);

    Query idQuery = new Query(Criteria.where("_id").is(new ObjectId(callToStart.id)));
    FindAndModifyOptions opts = FindAndModifyOptions.options().returnNew(true);
    Reservation updatedRes =
        mongoTemplate.findAndModify(
            idQuery, update, opts, MovingAverageAggregate.class, "reservation");
    return updatedRes;
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

  /** Retrieves the existing moving average from the database. */
  private MovingAverageAggregate getMovingAverage() {
    Aggregation agg =
        Aggregation.newAggregation(
            Aggregation.unwind("events"),
            Aggregation.match(Criteria.where("events.type").is(ReservationEventType.CONNECTED)),
            Aggregation.sort(Direction.DESC, "events.date"),
            Aggregation.limit(1),
            Aggregation.project("waitTimeMovingAverage"));

    AggregationResults<MovingAverageAggregate> results =
        mongoTemplate.aggregate(agg, "reservation", MovingAverageAggregate.class);

    List<MovingAverageAggregate> resultList = results.getMappedResults();
    return resultList.stream().findFirst().orElse(null);
  }

  /**
   * Calculate the current exponential moving average.
   *
   * @param smoothingFactor The most common choice is '2'. As this is increased, more recent events
   *     have a higher impact on the moving average.
   * @param observedEvents the number of events that the moving average should consider, expressed
   *     in units used when the previousMovingAverage was calculated
   * @param newValue the actual value being measured at this point in time
   * @param previousMovingAverage the exponential moving average that was calculated during the
   *     previous period
   */
  private double exponentialMovingAverage(
      double smoothingFactor, int observedEvents, double newValue, double previousMovingAverage) {

    double multiplier = smoothingFactor / (1 + observedEvents);
    return newValue * multiplier + previousMovingAverage * (1 - multiplier);
  }

  /**
   * Create the {@code org.springframework.data.mongodb.core.query.Update} that marks a call as
   * connected and computes the new moving average based on the difference between the reservation's
   * {@code requestDate} and the connected event time.
   *
   * @param callToStart the Reservation representing the next call to be connected to an agent
   * @param currentMovingAverage the moving average for the call wait time currently stored in the
   *     database
   * @return the Update to be passed to a {@code MongoTemplate} which updates the {$code
   *     callToStart} record in the database
   */
  private Update createCallStartDatabaseUpdate(
      Reservation callToStart, double currentMovingAverage) {

    /**
     * Note: this makes the (unrealistic) assumption that an agent is immediately connected to the
     * first caller in line. Out of scope for now is a separate locking mechanism to indicate that
     * an agent is working on a caller and no other agent should pick up that caller, but without
     * assuming the agent is immediately connected (instead, they would need to try to reach the
     * caller, and later update the caller's ReservationEvents).
     */
    ReservationEvent connectedEvent = new ReservationEvent();
    connectedEvent.date = new Date();
    connectedEvent.type = ReservationEventType.CONNECTED;
    List<ReservationEvent> resEvents = new ArrayList<ReservationEvent>();
    resEvents.add(connectedEvent);

    double waitTime =
        Duration.between(callToStart.requestDate.toInstant(), connectedEvent.date.toInstant())
            .toMinutes();
    double newMovingAverage =
        exponentialMovingAverage(
            smoothingFactor, numberOfCallsToAverage, waitTime, currentMovingAverage);

    return new Update().set("events", resEvents).set("waitTimeMovingAverage", newMovingAverage);
  }
}
