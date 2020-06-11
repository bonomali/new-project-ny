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

/** Represents a type of event in the reservation history. */
public enum ReservationEventType {
  /** Call was attempted but did not connect (busy, no answer, voicemail). */
  ATTEMPTED,
  /** Call connected and conversation started. */
  CONNECTED,
  /** Call was disrupted and conversation cut off (call failure, hangup). */
  DISRUPTED,
  /** Call was completed and conversation ended. */
  COMPLETED
}
