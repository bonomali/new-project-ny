package org.google.callmeback.api;

import java.util.Date;

/** Represents an event in the reservation history. */
public class ReservationEvent {
  /** The date/time when this event occurred. */
  public Date date;

  /** The event type. */
  public ReservationEventType type;
}
