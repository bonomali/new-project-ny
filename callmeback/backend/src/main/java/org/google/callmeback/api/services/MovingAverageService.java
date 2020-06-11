// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.google.callmeback.api.services;

import java.time.Duration;
import java.util.OptionalDouble;
import org.google.callmeback.api.Reservation;
import org.google.callmeback.api.ReservationEvent;
import org.google.callmeback.api.ReservationEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** Methods for calculating an exponential moving average for Reservation wait time. */
@Service
public class MovingAverageService {

  private final int numberOfCallsToAverage;
  private final int smoothingFactor;

  public MovingAverageService(
      @Value("${defaults.movingAverage.numberOfCalls}") int numberOfCallsToAverage,
      @Value("${defaults.movingAverage.smoothingFactor}") int smoothingFactor) {

    this.numberOfCallsToAverage = numberOfCallsToAverage;
    this.smoothingFactor = smoothingFactor;
  }

  /**
   * Calculates the current exponential moving average. If {@code previousMovingAverage} is not
   * present, returns {@code newValue}.
   *
   * @param smoothingFactor The most common choice is '2'. As this is increased, more recent events
   *     have a higher impact on the moving average.
   * @param observedEvents the number of events that the moving average should consider, expressed
   *     in units used when the previousMovingAverage was calculated
   * @param newValue the actual value being measured at this point in time
   * @param previousMovingAverage the exponential moving average that was calculated during the
   *     previous period.
   */
  private double exponentialMovingAverage(
      double smoothingFactor,
      int observedEvents,
      double newValue,
      OptionalDouble previousMovingAverage) {

    if (observedEvents <= 0) {
      throw new IllegalArgumentException(
          "Number of observed events must be positive to calculate a moving average.");
    } else if (observedEvents == Integer.MAX_VALUE) {
      throw new IllegalArgumentException(
          "Integer overflow: Number of observed events is too large.");
    }

    double multiplier = smoothingFactor / (1 + observedEvents);

    if (!previousMovingAverage.isPresent()) {
      return newValue;
    } else {
      return newValue * multiplier + previousMovingAverage.getAsDouble() * (1 - multiplier);
    }
  }

  /**
   * Given a Reservation that has been connected to an agent, and the ReservationEvent of type
   * CONNECTED, updates the existing exponential moving average with the actual call wait time from
   * the provided reservation.
   *
   * @param reservation the Reservation representing the next call to be connected to an agent.
   * @param connectedEvent the ReservationEventType.CONNECTED event created for {$code nextCall}.
   * @param previousMovingAverage the most recent exponential moving average, required to calculate
   *     the new exponential moving average.
   * @return the new exponential moving average.
   */
  public double getNewExponentialMovingAverage(
      Reservation reservation,
      ReservationEvent connectedEvent,
      OptionalDouble previousMovingAverage) {

    if (reservation == null) {
      throw new IllegalArgumentException(
          "Moving average cannot be calculated for a `null` Reservation");
    } else if (connectedEvent == null || connectedEvent.type != ReservationEventType.CONNECTED) {
      throw new IllegalArgumentException(
          "A non-null ReservationEvent of type CONNECTED must be provided to calculate a "
              + "moving average.");
    }

    double waitTimeMs =
        Duration.between(reservation.requestDate.toInstant(), connectedEvent.date.toInstant())
            .toMillis();

    return exponentialMovingAverage(
        smoothingFactor, numberOfCallsToAverage, waitTimeMs, previousMovingAverage);
  }
}
