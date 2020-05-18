package org.google.callmeback.api;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/** A ReservationRepository that overrides various methods of MongoRepository. */
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

  private static final int AVERAGE_CALL_DURATION_MINUTES = 10;
  private static final int WINDOW_LENGTH_MINUTES = 30;

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
    long expectedWaitTimeMins = countReservations * AVERAGE_CALL_DURATION_MINUTES;

    Date current = new Date();
    window.min = Date.from(current.toInstant().plus(Duration.ofMinutes(expectedWaitTimeMins)));
    window.exp = Date.from(current.toInstant().plus(
        Duration.ofMinutes(expectedWaitTimeMins + (WINDOW_LENGTH_MINUTES / 2))));
    window.max = Date.from(current.toInstant().plus(
        Duration.ofMinutes(expectedWaitTimeMins + WINDOW_LENGTH_MINUTES)));
    return window;
  }
}