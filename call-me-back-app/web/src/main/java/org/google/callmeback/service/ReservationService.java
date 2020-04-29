package org.google.callmeback.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.google.callmeback.dao.ReservationDao;
import org.google.callmeback.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

  private final ReservationDao reservationDao;

  @Autowired
  public ReservationService(@Qualifier("fakeReservationDao") ReservationDao reservationDao) {
    this.reservationDao = reservationDao;
  }

  public Reservation addReservation(Reservation reservation) {
    return reservationDao.insertReservation(reservation);
  }

  public List<Reservation> getAllReservations() {
    return reservationDao.selectAllReservations();
  }

  public Optional<Reservation> getReservationById(UUID id) {
    return reservationDao.selectReservationById(id);
  }
}
