package org.google.callmeback.api;

import java.time.Duration;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

public class ReservationWindow {
  public Date min;
  public Date max;
  public Date exp;

  @Autowired
  private ReservationRepository reservationRepository;

  public ReservationWindow() {
    Date current = new Date();
    this.min = Date.from(current.toInstant().plus(Duration.ofMinutes(20)));
    this.exp = Date.from(current.toInstant().plus(Duration.ofMinutes(30)));
    this.max = Date.from(current.toInstant().plus(Duration.ofMinutes(40)));
  }

  public Date getExpectedDate(Date requestDate) {
    int countReservations =
        reservationRepository.countByEventsNullAndReservationCreatedDateLessThan(requestDate);
    Date current = new Date();
    return Date.from(current.toInstant().plus(Duration.ofMinutes(10 * countReservations)));
  }
}
