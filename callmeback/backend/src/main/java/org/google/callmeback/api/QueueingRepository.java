package org.google.callmeback.api;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import org.bson.types.ObjectId;
import org.google.callmeback.api.services.MovingAverageService;
import org.springframework.beans.factory.annotation.Autowired;
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

  /**
   * Retrieves the most recent moving average from the database. This means finding the most recent
   * ReservationEventType.CONNECTED event, and returning the moving average stored on the document
   * where that event is found.
   *
   * @return the most recently calculated moving average.
   */
  public OptionalDouble getMovingAverage();
}

@Component
class QueueingRepositoryImpl implements QueueingRepository {
  private final MongoTemplate mongoTemplate;
  private final MovingAverageService movingAverageService;

  @Autowired
  public QueueingRepositoryImpl(
      MongoTemplate mongoTemplate, MovingAverageService movingAverageService) {

    this.mongoTemplate = mongoTemplate;
    this.movingAverageService = movingAverageService;
  }

  public Reservation startNextCall() {
    Reservation nextCall = getNextCall();
    if (nextCall == null) {
      return null;
    }

    OptionalDouble previousMovingAverage = getMovingAverage();

    /**
     * Note: this makes the (unrealistic) assumption that an agent is immediately connected to the
     * first caller in line. Out of scope for now is a separate locking mechanism to indicate that
     * an agent is working on a caller and no other agent should pick up that caller, but without
     * assuming the agent is immediately connected (instead, they would need to try to reach the
     * caller, and later update the caller's ReservationEvents).
     */
    ReservationEvent connectedEvent = ReservationEvent.newConnectedEvent();
    double newMovingAverage =
        movingAverageService.getNewExponentialMovingAverage(
            nextCall, connectedEvent, previousMovingAverage);

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
  public OptionalDouble getMovingAverage() {
    Aggregation agg =
        Aggregation.newAggregation(
            Aggregation.unwind("events"),
            Aggregation.match(Criteria.where("events.type").is(ReservationEventType.CONNECTED)),
            Aggregation.sort(Direction.DESC, "events.date"),
            Aggregation.limit(1),
            Aggregation.project("waitTimeMovingAverage"));

    AggregationResults<Reservation> results =
        mongoTemplate.aggregate(agg, "reservation", Reservation.class);

    List<Reservation> resultList = results.getMappedResults();
    Reservation lastConnectedReservation = resultList.stream().findFirst().orElse(null);
    OptionalDouble waitTimeMovingAverage =
        lastConnectedReservation == null
            ? OptionalDouble.empty()
            : OptionalDouble.of(lastConnectedReservation.waitTimeMovingAverage);
    return waitTimeMovingAverage;
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
  private Reservation updateNextCall(
      Reservation nextCall, ReservationEvent connectedEvent, double waitTimeMovingAverage) {

    Query idQuery = new Query(Criteria.where("_id").is(new ObjectId(nextCall.id)));
    Update update =
        new Update()
            .set("events", Arrays.asList(connectedEvent))
            .set("waitTimeMovingAverage", waitTimeMovingAverage);
    FindAndModifyOptions opts = FindAndModifyOptions.options().returnNew(true);
    return mongoTemplate.findAndModify(idQuery, update, opts, Reservation.class, "reservation");
  }
}
