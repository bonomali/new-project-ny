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

  public ReservationWindow(Date requestDate) {
    int countReservations =
        reservationRepository.countByEventsNullAndReservationCreatedDateLessThan(requestDate);
    Date current = new Date();
    long expectedWaitTimeMins = countReservations * 10;
    this.min = Date.from(current.toInstant().plus(Duration.ofMinutes(expectedWaitTimeMins - 10)));
    this.exp = Date.from(current.toInstant().plus(Duration.ofMinutes(expectedWaitTimeMins)));
    this.max = Date.from(current.toInstant().plus(Duration.ofMinutes(expectedWaitTimeMins + 10)));
  }
}
