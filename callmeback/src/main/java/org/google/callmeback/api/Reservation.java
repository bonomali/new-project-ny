package org.google.callmeback.api;

import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

/** Represents a resident's reservation for a call from an operator. */
@Document
public class Reservation {
  /** The unique identifier for the reservation. Auto-generated. */
  @Id public String id;

  /** The date/time when this reservation was created. Auto-generated. */
  @CreatedDate public Date createdDate;

  /** The date/time when this reservation was modified. Auto-generated. */
  @LastModifiedDate public Date updatedDate;

  /** The telephone number where the resident would like to be called. */
  public String contactPhone;

  /** The email where the resident would like to receive notifications. */
  public String contactEmail;

  /** The preferred name of the resident that the operator should use. */
  public String preferredName;

  /** The resident's question or concern, in their own words. */
  public String query;

  /** The topic of the reservation, derived from the resident's query. */
  public String topic;

  /** The history of all calls attempts on this reservation. */
  public List<ReservationEvent> events;

  /** The final status of the reservation upon resolution. */
  public ReservationResolution resolution;

  /** The resident's feedback about the service experience. */
  public ReservationFeedback feedback;

  /** The resident's feedback about the service experience. */
  public ReservationWindow getWindow() {
    return new ReservationWindow();
  }
}
