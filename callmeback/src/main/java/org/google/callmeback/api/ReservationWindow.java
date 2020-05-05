package org.google.callmeback.api;

import java.time.Duration;
import java.util.Date;

public class ReservationWindow {
  public Date min;
  public Date max;
  public Date exp;

  public ReservationWindow() {
    Date current = new Date();
    this.min = Date.from(current.toInstant().plus(Duration.ofMinutes(20)));
    this.exp = Date.from(current.toInstant().plus(Duration.ofMinutes(30)));
    this.max = Date.from(current.toInstant().plus(Duration.ofMinutes(40)));
  }
}
