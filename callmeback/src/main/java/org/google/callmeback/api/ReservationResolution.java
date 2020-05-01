package org.google.callmeback.api;

/** Represents the final resolution status of a reservation. */
public enum ReservationResolution {
  /** The reservation had invalid data, such as a non-working phone number. */
  INVALID,

  /** The reservation was canceled by the user. */
  CANCELED,

  /** The reservation was resolved. */
  RESOLVED
}
