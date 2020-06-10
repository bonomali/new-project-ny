package org.google.callmeback.api;

import java.util.Date;
import java.util.List;
import java.util.Objects;
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

  /**
   * The client-specified date/time when this reservation was created. For reservations created in
   * real time through the app, this will be the same as the createdDate. Reservations can also be
   * simulated to where their simulated create date may not be the same as the wall clock time when
   * the reservation is written to the database.
   */
  public Date requestDate;

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

  /** The expected call back times for the resident. */
  public ReservationWindow window;

  /**
   * The exponential moving average, in milliseconds, representing the average wait time based on
   * the previous moving average and the time that it took for this Reservation to be connected with
   * an agent.
   *
   * <p>Therefore, the moving average stored on a reservation document does *not* represent the
   * amount of time that the reservation waited before being connected with an agent.
   *
   * <p>TODO: this field should ideally be stored in a separate document (likely in a separate
   * MongoDB collection). Storing the moving average separately from the Reservation provides a
   * clearer separation of concerns, given that the moving average stored on a document incorporates
   * the historical moving average for all Reservations that were connected to an agent before it.
   */
  public double waitTimeMovingAverage;

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Reservation)) {
      return false;
    }
    Reservation reservation = (Reservation) obj;
    return Objects.equals(reservation.id, this.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}