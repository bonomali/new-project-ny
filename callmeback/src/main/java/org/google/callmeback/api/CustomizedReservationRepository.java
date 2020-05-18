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
 * @param <ID> the type of hte id of the entity the repository manages, specifically String
 */
public interface CustomizedReservationRepository<T, ID> {
  /**
   * Returns a Reservation by the specified ID, including populating the ReservationWindow, based
   * on the number of reservations in the system.
   */
  Optional<T> findById(ID id);

  /**
   * Persists and returns the specified Reservation, with the ReservationWindow populated, based on
   * the number of reservations in the system.
   */
  <S extends T> S save(S entity);
}

class CustomizedReservationRepositoryImpl<T, ID> implements CustomizedReservationRepository<T, ID> {

  @Autowired
  private MongoTemplate mongoTemplate;

  // (Hard-coded) Average time it takes for a call to be taken off the queue, after a reservation is
  // made
  private static final int AVERAGE_CALL_HANDLING_MINS = 10;

  // (Hard-coded) Length of the expected reservation window
  private static final int WINDOW_LENGTH_MINS = 30;

  @Override
  public Optional<T> findById(ID id) {
    Reservation reservation = mongoTemplate.findById(id, Reservation.class);
    reservation.window = getWindow(reservation.reservationCreatedDate);
    return Optional.of((T)reservation);
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
    Query query = new Query(
        Criteria.where("events").is(null).and("reservationCreatedDate").lt(requestDate));
    long countReservations = mongoTemplate.count(query, Reservation.class);
    long expectedWaitTimeMins = countReservations * AVERAGE_CALL_HANDLING_MINS;

    Instant requestDateInstant = requestDate.toInstant();
    window.min = Date.from(requestDateInstant.plus(
        Duration.ofMinutes(expectedWaitTimeMins - (WINDOW_LENGTH_MINS / 2))));
    window.exp = Date.from(requestDateInstant.plus(Duration.ofMinutes(expectedWaitTimeMins)));
    window.max = Date.from(requestDateInstant.plus(
        Duration.ofMinutes(expectedWaitTimeMins + (WINDOW_LENGTH_MINS / 2))));
    return window;
  }
}