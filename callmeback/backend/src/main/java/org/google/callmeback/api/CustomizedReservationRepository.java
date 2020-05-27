package org.google.callmeback.api;

import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * A ReservationRepository that overrides various methods of MongoRepository.
 *
 * @param <T> the domain the type repository manages, specifically Reservation
 * @param <ID> the type of the id of the entity the repository manages, specifically String
 */
public interface CustomizedReservationRepository<T, ID> {

  // (Hard-coded) Length of the expected reservation window
  public static final int WINDOW_LENGTH_MILLIS = 600000;

  /**
   * Returns a Reservation by the specified ID, including populating the ReservationWindow, based on
   * the average wait time for reservations in the system.
   */
  Optional<T> findById(ID id);

  /**
   * Persists and returns the specified Reservation, with the ReservationWindow populated, based on
   * the average wait time for reservations in the system.
   */
  <S extends T> S save(S entity);

  /**
   * Calculates and stores the average wait time for all reservations in the database. Note that
   * this is visible for testing.
   */
  void calculateAverageWaitTime();

  /**
   * Returns the average wait time for all reservations in the database. Note that this is visible
   * for testing.
   */
  Optional<Long> getAverageWaitTimeMillis();
}

class CustomizedReservationRepositoryImpl<T, ID> implements CustomizedReservationRepository<T, ID> {

  @Autowired private MongoTemplate mongoTemplate;

  @Autowired private AuditingHandler auditingHandler;

  // Average time between reservation request and the first connection event
  public Optional<Long> averageWaitTimeMillis = Optional.empty();

  private static Logger logger = LoggerFactory.logger(CustomizedReservationRepository.class);

  @Override
  @SuppressWarnings("unchecked")
  public Optional<T> findById(ID id) {
    Reservation reservation = mongoTemplate.findById(id, Reservation.class);
    reservation.window = getWindow(reservation.requestDate);
    // Indicate Reservation has changed so that the API caller can get the new window
    // even if none of the saved fields have changed.
    auditingHandler.markModified(reservation);
    return Optional.of((T) reservation);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S extends T> S save(S entity) {
    Reservation reservation = (Reservation) entity;
    mongoTemplate.save(reservation);
    reservation.window = getWindow(reservation.requestDate);
    return (S) reservation;
  }

  /**
   * Returns a ReservationWindow, incorporating the number of reservations created prior to the
   * specified date, which do not currently have any reservation events associated.
   */
  private ReservationWindow getWindow(Date requestDate) {
    ReservationWindow window = new ReservationWindow();

    // Set expected value as the reservation request time plus the expected wait time
    long expectedWaitTimeMillis =
        averageWaitTimeMillis.isPresent() ? averageWaitTimeMillis.get() : 0L;
    window.naiveExp =
        Date.from(requestDate.toInstant().plus(Duration.ofMillis(expectedWaitTimeMillis)));

    // Set window minimum as the greater value of expected time minus half of the hard-coded window
    // length and the current time. If it is set to the current time, update window.naiveExp to the
    // current time as well. This ensures the window is either inclusive of or later than the
    // current time and that the expected time is not earlier than the current time.
    Date currentDate = new Date();
    Date calculatedWindowMinimum =
        Date.from(window.naiveExp.toInstant().minus(Duration.ofMillis(WINDOW_LENGTH_MILLIS / 2)));
    if (calculatedWindowMinimum.before(currentDate)) {
      window.naiveMin = currentDate;
      window.naiveExp = currentDate;
    } else {
      window.naiveMin = calculatedWindowMinimum;
    }

    // Set window maximum as the window minimum plus the window length
    window.naiveMax =
        Date.from(window.naiveMin.toInstant().plus(Duration.ofMillis(WINDOW_LENGTH_MILLIS)));
    return window;
  }

  /**
   * Calculates and stores the average wait time (i.e. the amount of time between the reservation
   * being requested and the first CONNECTED event) for all reservations in the database. Note that
   * the stored average can be empty if there are no reservations in the database that have been
   * taken off the queue.
   *
   * <p>Note that this method is scheduled to run once every minute. Since it's scheduled, it must
   * have void return type.
   */
  @Scheduled(fixedDelay = 60000)
  @SuppressWarnings({"rawtypes"})
  public void calculateAverageWaitTime() {
    // TODO: Try to chain pipeline operators instead of having 4 distinct stages.
    // Stage 1: All events with connected status
    ProjectionOperation connectedEventsStage =
        Aggregation.project("requestDate")
            .and(
                ArrayOperators.Filter.filter("events")
                    .as("events")
                    .by(ComparisonOperators.Eq.valueOf("events.type").equalToValue("CONNECTED")))
            .as("connectedEvents");

    // Stage 2: The first connected event
    ProjectionOperation connectedEventStage =
        Aggregation.project("requestDate")
            .and(ArrayOperators.ArrayElemAt.arrayOf("connectedEvents").elementAt(0))
            .as("connectedEvent");

    // Stage 3: The time difference between the request time and the first connected event
    ProjectionOperation waitTimeStage =
        Aggregation.project("requestDate", "connectedEvent.date")
            .and("connectedEvent.date")
            .minus("requestDate")
            .as("waitTime");

    // Stage 4: The average of those time differences
    GroupOperation avgWaitGroup = Aggregation.group().avg("waitTime").as("avgWait");
    Aggregation aggregation =
        Aggregation.newAggregation(
            connectedEventsStage, connectedEventStage, waitTimeStage, avgWaitGroup);
    AggregationResults<Map> output = mongoTemplate.aggregate(aggregation, "reservation", Map.class);

    // TODO: Return stddev as well and use that rather than a hardcoded window length.

    Double avgWaitTime =
        output.getMappedResults().size() == 1
            ? (Double) output.getUniqueMappedResult().get("avgWait")
            : null;
    averageWaitTimeMillis =
        (avgWaitTime == null) ? Optional.empty() : Optional.of(avgWaitTime.longValue());
    logger.info(
        "Average wait time: "
            + (averageWaitTimeMillis.isPresent() ? averageWaitTimeMillis.get() : 0L)
            + " ms");
  }

  @Override
  public Optional<Long> getAverageWaitTimeMillis() {
    return averageWaitTimeMillis;
  }
}
