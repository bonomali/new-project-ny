package org.google.callmeback.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.google.callmeback.model.Reservation;
import org.springframework.stereotype.Repository;

@Repository("fakeReservationDao")
public class FakeReservationDataAccessService implements ReservationDao {

  private static List<Reservation> DB =
      Arrays.asList(new Reservation(UUID.randomUUID(), "Jason Mendoza"));

  @Override
  public Reservation insertReservation(UUID id, Reservation reservation) {
    Reservation newReservation = new Reservation(id, reservation.getName());
    DB.add(newReservation);
    return newReservation;
  }

  @Override
  public List<Reservation> selectAllReservations() {
    return DB;
  }

  @Override
  public Optional<Reservation> selectReservationById(UUID id) {
    return DB.stream().filter(reservation -> reservation.getId().equals(id)).findFirst();
  }
}
