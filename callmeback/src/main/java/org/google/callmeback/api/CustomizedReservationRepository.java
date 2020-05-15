package org.google.callmeback.api;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public interface CustomizedReservationRepository<T, ID> {
  Optional<T> findById(ID id);

  <S extends T> S save(S entity);
}

class CustomizedReservationRepositoryImpl<T, ID> implements CustomizedReservationRepository<T, ID> {

  @Autowired
  private MongoTemplate mongoTemplate;

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

  private ReservationWindow getWindow(Date requestDate) {
    ReservationWindow window = new ReservationWindow();
    Query query = new Query(
        Criteria.where("events").is(null).and("reservationCreatedDate").lt(requestDate));
    long countReservations = mongoTemplate.count(query, Reservation.class);
    Date current = new Date();
    long expectedWaitTimeMins = countReservations * 10;
    window.min = Date.from(current.toInstant().plus(Duration.ofMinutes(expectedWaitTimeMins - 10)));
    window.exp = Date.from(current.toInstant().plus(Duration.ofMinutes(expectedWaitTimeMins)));
    window.max = Date.from(current.toInstant().plus(Duration.ofMinutes(expectedWaitTimeMins + 10)));
    return window;
  }
}