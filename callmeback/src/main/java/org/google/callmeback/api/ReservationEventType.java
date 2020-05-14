package org.google.callmeback.api;

/** Represents a type of event in the reservation history. */
public enum ReservationEventType {
  /** Call as been removed from the queue by a call agent, who will attempt to reach the caller. */
  INPROGRESS,
  /** Call was attempted but did not connect (busy, no answer, voicemail). */
  ATTEMPTED,
  /** Call connected and conversation started. */
  CONNECTED,
  /** Call was disrupted and conversation cut off (call failure, hangup). */
  DISRUPTED,
  /** Call was completed and conversation ended. */
  COMPLETED
}
