package org.google.callmeback.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.google.callmeback.extensions.CustomMongoAggregation;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;

/**
 * QueueingRepository is a custom repository with methods for interacting with reservations inside
 * of a queue.
 */
interface QueueingRepository {
  /**
   * Gets the first caller in line (has the earliest reservation create time of all reservations
   * that do not have any ReservationEvents, i.e. have not already been contacted by an agent) and
   * starts their call.
   */
  public Reservation startNextCall();

  public List<MovingAverageAggregate> movingAverage();
}

@Component
class QueueingRepositoryImpl implements QueueingRepository {
  private final MongoTemplate mongoTemplate;

  private final Logger logger = LoggerFactory.logger(QueueingRepository.class);

  @Autowired
  public QueueingRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }
  
  public List<MovingAverageAggregate> movingAverage() {
    Aggregation agg = Aggregation.newAggregation(
      match(new Criteria().orOperator(where("events").exists(false), where("events").size(0))),
      sort(Sort.Direction.ASC, "reservationCreatedDate"),
      limit(1),
      new CustomMongoAggregation(
          "{$lookup:"
          + "{from: 'reservation',"
          + "pipeline: ["
          + "{$unwind: '$events'},"
          + "{$sort: {'events.date': -1}},"
          + "{$limit: 1},"
          + "{$project: {_id: 0, waitTimeMovingAvg: 1}}"
          + "],"
          + "as: 'previousAverage'}}"
      ),
      unwind("previousAverage")
    );

    logger.info("Query: " + agg.toString());
    
    AggregationResults<MovingAverageAggregate> results =  
      mongoTemplate.aggregate(agg, "reservation", MovingAverageAggregate.class);
    
    return results.getMappedResults();
  }

  public Reservation startNextCall() {
    Sort sort = Sort.by("requestDate").ascending();
    /**
     * Note: This query only pulls callers who have no associated events. For the current scope, we
     * don't handle here (or elsewhere) calls that were ATTEMPTED or DISRUPTED but not completed.
     * Once those callers are handled, we should consider whether they should re-enter the queue and
     * be able to be selected by agents here, or if they should be handled separately.
     */
    Query query = new Query(where("events").is(null)).with(sort).limit(1);

    List<ReservationEvent> resEvents = new ArrayList<ReservationEvent>();
    ReservationEvent resEvent = new ReservationEvent();
    resEvent.date = new Date();
    /**
     * Note: this makes the (unrealistic) assumption that an agent is immediately connected to the
     * first caller in line. Out of scope for now is a separate locking mechanism to indicate that
     * an agent is working on a caller and no other agent should pick up that caller, but without
     * assuming the agent is immediately connected (instead, they would need to try to reach the
     * caller, and later update the caller's ReservationEvents).
     */
    resEvent.type = ReservationEventType.CONNECTED;
    resEvents.add(resEvent);
    Update update = new Update().set("events", resEvents);

    FindAndModifyOptions opts = FindAndModifyOptions.options().returnNew(true);
    /** Returns the updated reservation */
    Reservation updatedRes =
        mongoTemplate.findAndModify(query, update, opts, Reservation.class, "reservation");
    return updatedRes;
  }
}
