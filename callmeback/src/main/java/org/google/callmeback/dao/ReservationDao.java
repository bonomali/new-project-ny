package org.google.callmeback.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.google.callmeback.model.Reservation;

public interface ReservationDao {
  Reservation insertReservation(UUID id, Reservation reservation);

  default Reservation insertReservation(Reservation reservation) {
    UUID id = UUID.randomUUID();
    return insertReservation(id, reservation);
  }

  List<Reservation> selectAllReservations();

  Optional<Reservation> selectReservationById(UUID id);
}
