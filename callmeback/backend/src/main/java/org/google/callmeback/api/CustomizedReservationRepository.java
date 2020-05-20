package org.google.callmeback.api;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

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

  // (Hard-coded) Average amount of time between individual calls being taken off the queue
  private static final int AVERAGE_WAIT_TIME_MINS = 10;

  // (Hard-coded) Length of the expected reservation window
  private static final int WINDOW_LENGTH_MINS = 30;

  @Override
  @SuppressWarnings("unchecked")
  public Optional<T> findById(ID id) {
    Reservation reservation = mongoTemplate.findById(id, Reservation.class);
    reservation.window = getWindow(reservation.requestDate);
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

    // Query for the count of reservations without any events which were created earlier than the
    // specified requestDate.
    Query query =
        new Query(Criteria.where("events").is(null).and("requestDate").lt(requestDate));
    long countReservations = mongoTemplate.count(query, Reservation.class);

    // Set expected value based on count of open reservations and average wait time.
    long expectedWaitTimeMins = countReservations * AVERAGE_WAIT_TIME_MINS;
    Date currentDate = new Date();
    Instant currentDateInstant = currentDate.toInstant();
    window.exp = Date.from(currentDateInstant.plus(Duration.ofMinutes(expectedWaitTimeMins)));

    // Set window minimum as the greater value of expected time minus half of the hard-coded window
    // length and the current time. This ensures the current time is always within the window.
    Date calculatedWindowMinimum =
        Date.from(
            currentDateInstant.plus(
                Duration.ofMinutes(expectedWaitTimeMins - WINDOW_LENGTH_MINS / 2)));
    window.min =
        calculatedWindowMinimum.before(currentDate) ? currentDate : calculatedWindowMinimum;

    // Set window maximum as expected time plus half of the hard-coded window length
    window.max =
        Date.from(
            currentDateInstant.plus(
                Duration.ofMinutes(expectedWaitTimeMins + (WINDOW_LENGTH_MINS / 2))));
    return window;
  }
}
