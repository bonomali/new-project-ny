package org.google.callmeback.api.services;

import static org.google.callmeback.TestHelpers.createReservation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.OptionalDouble;
import org.google.callmeback.TestHelpers;
import org.google.callmeback.api.Reservation;
import org.google.callmeback.api.ReservationEvent;
import org.google.callmeback.api.ReservationEventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MovingAverageServiceTest {

  private static final int TWENTY_MINUTES_MILLIS = 20 * 60 * 1000;

  private MovingAverageService movingAverageService;
  private final int numberOfCallsToAverage = 9;
  private final int smoothingFactor = 2;

  @BeforeEach
  public void init() {
    movingAverageService = new MovingAverageService(numberOfCallsToAverage, smoothingFactor);
  }

  @Test
  public void testGetNewExponentialMovingAverage_handlesNullReservation() {
    ReservationEvent connected = ReservationEvent.newConnectedEvent();

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              movingAverageService.getNewExponentialMovingAverage(null, connected, null);
            });

    assertEquals("Moving average cannot be calculated for a `null` Reservation", e.getMessage());
  }

  @Test
  public void testGetNewExponentialMovingAverage_handlesInvalidConnectedEvent() {
    Reservation res = TestHelpers.createReservation();
    Exception nullEx =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              movingAverageService.getNewExponentialMovingAverage(res, null, null);
            });

    assertEquals(
        "A non-null ReservationEvent of type CONNECTED must be provided to calculate a "
            + "moving average.",
        nullEx.getMessage());

    ReservationEvent notConnected =
        new ReservationEvent(new Date(), ReservationEventType.ATTEMPTED);
    Exception typeEx =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              movingAverageService.getNewExponentialMovingAverage(res, notConnected, null);
            });

    assertEquals(
        "A non-null ReservationEvent of type CONNECTED must be provided to calculate a "
            + "moving average.",
        typeEx.getMessage());
  }

  @Test
  public void testGetNewExponentialMovingAverage_handlesZeroObservedEvents() {
    movingAverageService = new MovingAverageService(0, smoothingFactor);

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              getMovingAverage(0);
            });

    assertEquals(
        "Number of observed events must be positive to calculate a moving average.",
        e.getMessage());
  }

  @Test
  public void testGetNewExponentialMovingAverage_handlesIntMaxObservedEvents() {
    movingAverageService = new MovingAverageService(Integer.MAX_VALUE, smoothingFactor);

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              getMovingAverage(0);
            });

    assertEquals("Integer overflow: Number of observed events is too large.", e.getMessage());
  }

  @Test
  public void testGetNewExponentialMovingAverage_handlesNullPastMovingAverage() {
    double newAvg = getMovingAverage(TWENTY_MINUTES_MILLIS);
    assertEquals(TWENTY_MINUTES_MILLIS, newAvg);
  }

  @Test
  public void testGetNewExponentialMovingAverage_updatesMovingAverage() {
    OptionalDouble tenMinutesInMillis = OptionalDouble.of(10 * 60 * 1000);

    double result = getMovingAverage(TWENTY_MINUTES_MILLIS, tenMinutesInMillis);
    assertEquals(12 * 60 * 1000, result); // 12 minutes in ms
  }

  /** Returns a moving average from one event for the specified number of milliseconds. */
  private double getMovingAverage(int timeMillis) {
    return getMovingAverage(timeMillis, OptionalDouble.empty());
  }

  /**
   * Returns a {@code timeMillis} moving average by default. If passed a non-null previousAverage,
   * will calculate the updated moving average based on the previous value.
   */
  private double getMovingAverage(int timeMillis, OptionalDouble previousAverage) {
    ZonedDateTime d = ZonedDateTime.of(2020, 05, 28, 10, 0, 0, 0, ZoneOffset.UTC);
    ZonedDateTime d2 = d.plus(Duration.ofMillis(timeMillis));
    Date requestDate = Date.from(d.toInstant());
    Date connectedDate = Date.from(d2.toInstant());
    Reservation res = createReservation(requestDate);
    ReservationEvent connected =
        new ReservationEvent(connectedDate, ReservationEventType.CONNECTED);

    return movingAverageService.getNewExponentialMovingAverage(res, connected, previousAverage);
  }
}
