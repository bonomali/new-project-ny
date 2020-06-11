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

package org.google.callmeback;

import java.util.Date;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.google.callmeback.api.Reservation;
import org.google.callmeback.api.ReservationEvent;

public class TestHelpers {

  static final String DEFAULT_TOPIC = "Business";

  public static Reservation createReservation() {
    return createReservation(Optional.empty());
  }

  public static Reservation createReservation(Date date) {
    return createReservation(date, DEFAULT_TOPIC, Optional.empty());
  }

  public static Reservation createReservation(Optional<ReservationEvent> reservationEvent) {
    return createReservation(DEFAULT_TOPIC, reservationEvent);
  }

  public static Reservation createReservation(
      String topic, Optional<ReservationEvent> reservationEvent) {
    return createReservation(new Date(), topic, reservationEvent);
  }

  public static Reservation createReservation(
      Date requestDate, String topic, Optional<ReservationEvent> reservationEvent) {
    Reservation reservation = new Reservation();
    reservation.requestDate = requestDate;
    reservation.topic = topic;
    if (reservationEvent.isPresent()) {
      reservation.events = Lists.newArrayList(reservationEvent.get());
    }
    return reservation;
  }
}
