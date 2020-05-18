package org.google.callmeback.api;

import java.util.Date;

/** Represents the final resolution status of the reservation. */
public class ReservationResolution {
  /** The date/time when this reservation was resolved. */
  public Date date;

  /** The final resolution type of a reservation. */
  public ReservationResolutionType type;
}
