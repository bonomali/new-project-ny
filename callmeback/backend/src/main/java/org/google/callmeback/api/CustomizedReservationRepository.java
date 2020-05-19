package org.google.callmeback.api;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Cond;

/**
 * A ReservationRepository that overrides various methods of MongoRepository.
 *
 * @param <T> the domain the type repository manages, specifically Reservation
 * @param <ID> the type of the id of the entity the repository manages, specifically String
 */
public interface CustomizedReservationRepository<T, ID> {
  /**
   * Returns a Reservation by the specified ID, including populating the ReservationWindow, based on
   * the number of reservations in the system.
   */
  Optional<T> findById(ID id);

  /**
   * Persists and returns the specified Reservation, with the ReservationWindow populated, based on
   * the number of reservations in the system.
   */
  <S extends T> S save(S entity);
}

class CustomizedReservationRepositoryImpl<T, ID> implements CustomizedReservationRepository<T, ID> {

  @Autowired private MongoTemplate mongoTemplate;

  // (Hard-coded) Average time it takes for a call to be taken off the queue, after a reservation is
  // made
  private static final int AVERAGE_WAIT_TIME_MINS = 10;

  // (Hard-coded) Length of the expected reservation window
  private static final int WINDOW_LENGTH_MINS = 30;

  @Override
  public Optional<T> findById(ID id) {
    Reservation reservation = mongoTemplate.findById(id, Reservation.class);
    reservation.window = getWindow(reservation.reservationCreatedDate);
    return Optional.of((T) reservation);
  }

  @Override
  public <S extends T> S save(S entity) {
    Reservation reservation = (Reservation) entity;
    mongoTemplate.save(reservation);
    reservation.window = getWindow(reservation.reservationCreatedDate);
    return (S) reservation;
  }

  /**
   * Returns a ReservationWindow, incorporating the number of reservations created prior to the
   * specified date, which do not currently have any reservation events associated.
   */
  private ReservationWindow getWindow(Date requestDate) {
    ReservationWindow window = new ReservationWindow();
    Query eventsQuery =
        new Query(Criteria.where("events").is(null).and("reservationCreatedDate").lt(requestDate));
    long countReservations = mongoTemplate.count(eventsQuery, Reservation.class);
  
    /* db.rez.aggregate(
      [{$set:
        {connects:
          {$arrayElemAt: [{$filter: {input: '$events', as: 'e', cond: {$eq: ['$$e.eventType', 'CONNECTED']}}}, 0]}
        }
      },
      {$group:
        {_id: 'ALL', avg_age: {$avg: {$subtract: ['$connects.eventDate', '$requestDate']}}}
      }]).pretty()
    */
    /* filter example
    new AggregationExpression() {
                @Override
                public DBObject toDbObject(AggregationOperationContext aggregationOperationContext) {
                    DBObject filterExpression = new BasicDBObject();
                    filterExpression.put("input", "$devices");
                    filterExpression.put("as", "device");
                    filterExpression.put("cond", new BasicDBObject("$eq", Arrays.<Object> asList("$$device.evaluationDate", date)));
                    return new BasicDBObject("$filter", filterExpression);
                }
            }
    */

    ProjectionOperation projectStage =
      Aggregation.project("requestDate").and("events").filter("e", Cond.when(Criteria.where("e.eventType")).equals("CONNECTED")).arrayElementAt(0).as("connected");
    ProjectionOperation waitTime =
      Aggregation.project().andExpression("connected.eventDate - requestDate").as("waitTime");
    GroupOperation avgWait = Aggregation.group("id")            
      .avg("waitTime").as("avgWait");
            
    Aggregation aggregation 
      = Aggregation.newAggregation(projectStage, waitTime, avgWait);
    
    long avgWaitTime = mongoTemplate.aggregate(aggregation, "reservations");

    long expectedWaitTimeMins = avgWaitTime - requestDate;

    Instant requestDateInstant = requestDate.toInstant();
    window.min =
        Date.from(
            requestDateInstant.plus(
                Duration.ofMinutes(expectedWaitTimeMins - (WINDOW_LENGTH_MINS / 2))));
    window.exp = Date.from(requestDateInstant.plus(Duration.ofMinutes(expectedWaitTimeMins)));
    window.max =
        Date.from(
            requestDateInstant.plus(
                Duration.ofMinutes(expectedWaitTimeMins + (WINDOW_LENGTH_MINS / 2))));
    return window;
  }
}
