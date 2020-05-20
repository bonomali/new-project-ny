package org.google.callmeback.api;

import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;

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

  // (Hard-coded) Length of the expected reservation window
  private static final int WINDOW_LENGTH_MILLIS = 600000;

  @Override
  @SuppressWarnings("unchecked")
  public Optional<T> findById(ID id) {
    Reservation reservation = mongoTemplate.findById(id, Reservation.class);
    reservation.window = getWindow(reservation.reservationCreatedDate);
    return Optional.of((T) reservation);
  }

  @Override
  @SuppressWarnings("unchecked")
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

    // Set expected value as the reservation request time plus the expected wait time
    Double averageWaitTimeMillis = getAverageWaitTimeMillis();
    long expectedWaitTimeMillis =
        (averageWaitTimeMillis != null) ? averageWaitTimeMillis.longValue() : 0L;
    window.exp = Date.from(requestDate.toInstant().plus(Duration.ofMillis(expectedWaitTimeMillis)));

    // Set window minimum as the greater value of expected time minus half of the hard-coded window
    // length and the current time. If it is set to the current time, update window.exp to the current
    // time as well. This ensures the window is either inclusive of or later than the current time
    // and that the expected time is not earlier than the current time.
    Date currentDate = new Date();
    Date calculatedWindowMinimum =
        Date.from(window.exp.toInstant().minus(Duration.ofMillis(WINDOW_LENGTH_MILLIS / 2)));
    window.min = calculatedWindowMinimum;
    if (calculatedWindowMinimum.before(currentDate)) {
      window.min = currentDate;
      window.exp = currentDate;
    }

    // Set window maximum as the window minimum plus the window length
    window.max = Date.from(window.min.toInstant().plus(Duration.ofMillis(WINDOW_LENGTH_MILLIS)));
    return window;
  }

  /**
   * Returns the average wait time (i.e. the amount of time between the reservation being requested
   * and the first CONNECTED event) for all reservations in the database. Note that the return value
   * can be null if there are no reservations in the database that have been taken off the queue.
   */
  @SuppressWarnings({ "rawtypes" })
  private Double getAverageWaitTimeMillis() {
    // Stage 1: All events with connected status
    ProjectionOperation connectedEventsStage =
        Aggregation.project("reservationCreatedDate")
            .and(ArrayOperators.Filter.filter("events")
                .as("events")
                .by(ComparisonOperators.Eq.valueOf(
                  "events.type").equalToValue("CONNECTED"))).as("connectedEvents");

    // Stage 2: The first connected event
    ProjectionOperation connectedEventStage =
        Aggregation.project("reservationCreatedDate")
            .and(ArrayOperators.ArrayElemAt.arrayOf("connectedEvents").elementAt(0))
                .as("connectedEvent");
    
    // Stage 3: The time difference between the request time and the first connected event 
    ProjectionOperation waitTimeStage =
        Aggregation.project("reservationCreatedDate", "connectedEvent.date")
            .and("connectedEvent.date").minus("reservationCreatedDate").as("waitTime");

    // Stage 4: The average of those time differences
    GroupOperation avgWaitGroup = Aggregation.group().avg("waitTime").as("avgWait");
    Aggregation aggregation = Aggregation.newAggregation(
        connectedEventsStage, connectedEventStage, waitTimeStage, avgWaitGroup);
    AggregationResults<Map> output =
        mongoTemplate.aggregate(aggregation, "reservation", Map.class);   
    return (Double) output.getUniqueMappedResult().get("avgWait");
  }
}
