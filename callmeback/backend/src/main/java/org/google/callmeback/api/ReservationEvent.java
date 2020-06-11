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
