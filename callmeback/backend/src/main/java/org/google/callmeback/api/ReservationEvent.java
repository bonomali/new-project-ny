package org.google.callmeback.api;

import java.util.Date;

/** Represents an event in the reservation history. */
public class ReservationEvent {
  /** The date/time when this event occurred. */
  public Date date;

  /** The event type. */
  public ReservationEventType type;

  /** Create an empty reservation event. */
  public ReservationEvent() {}

  /**
   * Create a reservation event.
   *
   * @param date the timestamp indicating when the event occured.
   * @param type the type of event that occurred.
   */
  public ReservationEvent(Date date, ReservationEventType type) {
    this.date = date;
    this.type = type;
  }

  /** Returns a ReservationEvent of ReservationEventType CONNECTED at the current Date. */
  public static ReservationEvent newConnectedEvent() {
    return new ReservationEvent(new Date(), ReservationEventType.CONNECTED);
  }
}
