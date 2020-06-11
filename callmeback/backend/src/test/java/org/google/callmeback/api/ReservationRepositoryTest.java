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

import static org.assertj.core.api.Assertions.assertThat;
import static org.google.callmeback.api.CustomizedReservationRepository.WINDOW_LENGTH_MILLIS;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import org.google.callmeback.TestHelpers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReservationRepositoryTest {
  @Autowired ReservationRepository reservationRepository;

  final String businessTopic = "Business";
  final String unemploymentTopic = "Unemployment";
  final String dmvTopic = "DMV";
  final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @BeforeEach
  public void setUp() {
    reservationRepository.deleteAll();
    reservationRepository.calculateAverageWaitTime();
  }

  @Test
  public void testSave_setsId() {
    Reservation reservation = createAndPersistReservation(businessTopic);

    assertThat(reservation.id).isNotNull();
    assertThat(reservationRepository.findAll()).containsExactlyInAnyOrder(reservation);
  }

  @Test
  public void testFindByTopic_singleReservation() {
    Reservation reservation = createAndPersistReservation(businessTopic);

    assertThat(reservation.id).isNotNull();
    assertThat(reservation.topic).isEqualTo(businessTopic);
    assertThat(reservationRepository.findByTopic(businessTopic))
        .containsExactlyInAnyOrder(reservation);
    assertThat(reservationRepository.findByTopic(unemploymentTopic)).isEmpty();
    assertThat(reservationRepository.findByTopic(dmvTopic)).isEmpty();
  }

  @Test
  public void testFindByTopic_multipleReservations() {
    Reservation reservation1 = createAndPersistReservation(businessTopic);
    Reservation reservation2 = createAndPersistReservation(businessTopic);
    Reservation reservation3 = createAndPersistReservation(unemploymentTopic);

    assertThat(reservationRepository.findByTopic(businessTopic))
        .containsExactlyInAnyOrder(reservation1, reservation2);
    assertThat(reservationRepository.findByTopic(unemploymentTopic))
        .containsExactlyInAnyOrder(reservation3);
    assertThat(reservationRepository.findByTopic(dmvTopic)).isEmpty();
  }

  @Test
  public void testStartNextCall_setsEventForSingleCall() {
    Date currentDate = new Date();
    createAndPersistReservation(currentDate);

    Reservation reservation = reservationRepository.startNextCall();
    assertThat(reservation.id).isNotNull();
    assertThat(reservation.requestDate).isEqualTo(currentDate);
    assertThat(reservation.events).isNotNull();
    assertThat(reservation.events.size()).isEqualTo(1);
    assertThat(reservation.events.get(0).type).isEqualTo(ReservationEventType.CONNECTED);
    assertThat(reservation.events.get(0).date).isNotNull();
  }

  @Test
  public void testStartNextCall_noCallToStart() {
    createAndPersistReservation(new Date(), Optional.of(ReservationEventType.CONNECTED));

    Reservation reservation = reservationRepository.startNextCall();
    assertThat(reservation).isNull();
  }

  @Test
  public void testStartNextCall_setsEventForMostRecentUnresolvedCall() {
    Date date1 = new Date();
    createAndPersistReservation(date1, Optional.of(ReservationEventType.CONNECTED));

    Date date2 = new Date();
    Reservation res2 = createAndPersistReservation(date2);

    Date date3 = new Date();
    Reservation res3 = createAndPersistReservation(date3);

    Reservation reservation = reservationRepository.startNextCall();
    assertThat(reservation.id).isEqualTo(res2.id);
    assertThat(reservation.requestDate).isEqualTo(date2);
    assertThat(reservation.events).isNotNull();
    assertThat(reservation.events.size()).isEqualTo(1);
    assertThat(reservation.events.get(0).type).isEqualTo(ReservationEventType.CONNECTED);
    assertThat(reservation.events.get(0).date).isNotNull();

    Reservation reservation2 = reservationRepository.startNextCall();
    assertThat(reservation2.id).isEqualTo(res3.id);
    assertThat(reservation2.requestDate).isEqualTo(date3);
    assertThat(reservation2.events).isNotNull();
    assertThat(reservation2.events.size()).isEqualTo(1);
    assertThat(reservation2.events.get(0).type).isEqualTo(ReservationEventType.CONNECTED);
    assertThat(reservation2.events.get(0).date).isNotNull();
  }

  @Test
  public void testNaiveReservationWindow_requestDateBeforeCurrentDate() {
    Date requestedDate = new Date();
    Reservation reservation = createAndPersistReservation(requestedDate);

    // Exp and Min should be equivalent to requestDate; Max should be window length after Min
    Window window = reservation.window.naiveWindow;
    assertThat(dateFormat.format(window.exp)).isEqualTo(dateFormat.format(requestedDate));
    assertThat(window.exp).isEqualTo(window.min);
    assertThat(dateFormat.format(window.max))
        .isEqualTo(
            dateFormat.format(
                Date.from(window.exp.toInstant().plus(Duration.ofMillis(WINDOW_LENGTH_MILLIS)))));

    Reservation reservationById = reservationRepository.findById(reservation.id).get();
    window = reservationById.window.naiveWindow;
    assertThat(dateFormat.format(window.exp)).isEqualTo(dateFormat.format(requestedDate));
    assertThat(window.exp).isEqualTo(window.min);
    assertThat(dateFormat.format(window.max))
        .isEqualTo(
            dateFormat.format(
                Date.from(window.exp.toInstant().plus(Duration.ofMillis(WINDOW_LENGTH_MILLIS)))));
  }

  @Test
  public void testNaiveReservationWindow_requestDateAfterCurrentDate() {
    // Create a reservation with requested date after current date
    Date requestedDate = Date.from(new Date().toInstant().plus(Duration.ofDays(1)));
    Reservation reservation = createAndPersistReservation(requestedDate);

    // Exp should be equivalent to requestDate; Min should be half of window length before Exp; Max
    // should be half of window length after Exp
    Window window = reservation.window.naiveWindow;
    assertThat(dateFormat.format(window.exp)).isEqualTo(dateFormat.format(requestedDate));
    assertThat(dateFormat.format(window.min))
        .isEqualTo(
            dateFormat.format(
                Date.from(
                    window.exp.toInstant().minus(Duration.ofMillis(WINDOW_LENGTH_MILLIS / 2)))));
    assertThat(dateFormat.format(window.max))
        .isEqualTo(
            dateFormat.format(
                Date.from(
                    window.exp.toInstant().plus(Duration.ofMillis(WINDOW_LENGTH_MILLIS / 2)))));

    Reservation reservationById = reservationRepository.findById(reservation.id).get();
    window = reservationById.window.naiveWindow;
    assertThat(dateFormat.format(window.exp)).isEqualTo(dateFormat.format(requestedDate));
    assertThat(dateFormat.format(window.min))
        .isEqualTo(
            dateFormat.format(
                Date.from(
                    window.exp.toInstant().minus(Duration.ofMillis(WINDOW_LENGTH_MILLIS / 2)))));
    assertThat(dateFormat.format(window.max))
        .isEqualTo(
            dateFormat.format(
                Date.from(
                    window.exp.toInstant().plus(Duration.ofMillis(WINDOW_LENGTH_MILLIS / 2)))));
  }

  @Test
  public void testGetAverageWaitTimeMillis_withoutReservationsIsEmpty() {
    assertThat(reservationRepository.getAverageWaitTimeMillis().isPresent()).isFalse();
    reservationRepository.calculateAverageWaitTime();
    assertThat(reservationRepository.getAverageWaitTimeMillis().isPresent()).isFalse();
  }

  @Test
  public void testGetAverageWaitTimeMillis_withNonConnectedReservationsIsEmpty() {
    createAndPersistReservation(new Date());
    createAndPersistReservation(new Date(), Optional.of(ReservationEventType.ATTEMPTED));

    reservationRepository.calculateAverageWaitTime();
    assertThat(reservationRepository.getAverageWaitTimeMillis().isPresent()).isFalse();
  }

  @Test
  public void testGetAverageWaitTimeMillis_withSingleConnectedReservation() {
    // Request date and connected date are 20 minutes apart
    Date requestedDate = new Date();
    Date connectedDate = Date.from(requestedDate.toInstant().plus(Duration.ofMinutes(20)));
    createAndPersistReservation(
        requestedDate, Optional.of(ReservationEventType.CONNECTED), Optional.of(connectedDate));
    createAndPersistReservation(new Date());

    reservationRepository.calculateAverageWaitTime();
    assertThat(reservationRepository.getAverageWaitTimeMillis().getAsLong()).isEqualTo(1200000L);
  }

  @Test
  public void testGetAverageWaitTimeMillis_withMultipleConnectedReservations() {
    // Request date and connected date are 20 minutes apart
    Date requestedDate = new Date();
    Date connectedDate = Date.from(requestedDate.toInstant().plus(Duration.ofMinutes(20)));
    createAndPersistReservation(
        requestedDate, Optional.of(ReservationEventType.CONNECTED), Optional.of(connectedDate));
    // Request date and connected date are 30 minutes apart
    Date requestedDate2 = new Date();
    Date connectedDate2 = Date.from(requestedDate2.toInstant().plus(Duration.ofMinutes(30)));
    createAndPersistReservation(
        requestedDate2, Optional.of(ReservationEventType.CONNECTED), Optional.of(connectedDate2));

    reservationRepository.calculateAverageWaitTime();
    assertThat(reservationRepository.getAverageWaitTimeMillis().getAsLong()).isEqualTo(1500000L);
  }

  @Test
  public void testGetAverageWaitTimeMillis_multipleReservationsWithMultipleEvents() {
    // Add reservations with and without events and with different delays between them.
    // Reservations with no events (will not be factored in to avg wait time calculation).
    Date date1 = new Date();
    Reservation res1WithNoEventsInSystem = createAndPersistReservation(date1);
    createAndPersistReservation(new Date(), Optional.of(ReservationEventType.ATTEMPTED));
    Reservation res2WithNoEventsInSystem = createAndPersistReservation(date1);

    // Confirm the average wait time does not yet exist
    reservationRepository.calculateAverageWaitTime();
    assertThat(reservationRepository.getAverageWaitTimeMillis().isPresent()).isFalse();

    // Check that the reservations added so far have expected wait time of 0 (exp = min).
    assertThat(res1WithNoEventsInSystem.window.naiveWindow.exp)
        .isEqualTo(res1WithNoEventsInSystem.window.naiveWindow.min);
    assertThat(res1WithNoEventsInSystem.window.naiveWindow.max)
        .isEqualTo(
            Date.from(
                res1WithNoEventsInSystem
                    .window
                    .naiveWindow
                    .min
                    .toInstant()
                    .plus(Duration.ofMillis(600000))));
    assertThat(res2WithNoEventsInSystem.window.naiveWindow.exp)
        .isEqualTo(res2WithNoEventsInSystem.window.naiveWindow.min);
    assertThat(res2WithNoEventsInSystem.window.naiveWindow.max)
        .isEqualTo(
            Date.from(
                res2WithNoEventsInSystem
                    .window
                    .naiveWindow
                    .min
                    .toInstant()
                    .plus(Duration.ofMillis(600000))));

    // Reservation with multiple events and 10 minute wait time until first connect.
    Date date2 = new Date();
    Reservation connectedRes =
        createAndPersistReservation(
            date2,
            Optional.of(ReservationEventType.ATTEMPTED),
            Optional.of(Date.from(date2.toInstant().plus(Duration.ofMinutes(5)))));
    // Add a connected event.
    ReservationEvent event = new ReservationEvent();
    event.date = Date.from(date2.toInstant().plus(Duration.ofMinutes(10)));
    event.type = ReservationEventType.CONNECTED;
    // Add a second connected event.
    ReservationEvent event2 = new ReservationEvent();
    event2.date = Date.from(date2.toInstant().plus(Duration.ofMinutes(15)));
    event2.type = ReservationEventType.CONNECTED;
    connectedRes.events.add(event);
    connectedRes.events.add(event2);
    reservationRepository.save(connectedRes);

    // Confirm the average wait time is now 10 minutes
    reservationRepository.calculateAverageWaitTime();
    assertThat(reservationRepository.getAverageWaitTimeMillis().getAsLong()).isEqualTo(600000L);

    // New reservation should have exp wait time of 10 minutes.
    Date date3 = new Date();
    Reservation resWithOnePriorConnectedRes = createAndPersistReservation(date3);

    assertThat(dateFormat.format(resWithOnePriorConnectedRes.window.naiveWindow.exp))
        .isEqualTo(dateFormat.format(Date.from(date3.toInstant().plus(Duration.ofMinutes(10)))));

    // Reservation with connected event and 20 minute wait time.
    Date date4 = new Date();
    createAndPersistReservation(
        date4,
        Optional.of(ReservationEventType.CONNECTED),
        Optional.of(Date.from(date4.toInstant().plus(Duration.ofMinutes(20)))));

    // Confirm the average wait time is now 15 minutes
    reservationRepository.calculateAverageWaitTime();
    assertThat(reservationRepository.getAverageWaitTimeMillis().getAsLong()).isEqualTo(900000L);

    // New reservation should have expected wait time of 15 minutes.
    Date date5 = new Date();
    Reservation resWithMultiplePriorConnectedRes = createAndPersistReservation(date5);
    assertThat(dateFormat.format(resWithMultiplePriorConnectedRes.window.naiveWindow.exp))
        .isEqualTo(dateFormat.format(Date.from(date5.toInstant().plus(Duration.ofMinutes(15)))));
    assertThat(dateFormat.format(resWithMultiplePriorConnectedRes.window.naiveWindow.min))
        .isEqualTo(
            dateFormat.format(
                Date.from(
                    date5
                        .toInstant()
                        .plus(
                            Duration.ofMinutes(15)
                                .minus(Duration.ofMillis(WINDOW_LENGTH_MILLIS / 2))))));

    // Old reservations should have updated wait times.
    Optional<Reservation> originalRes = reservationRepository.findById(res1WithNoEventsInSystem.id);
    assertThat(dateFormat.format(originalRes.get().window.naiveWindow.exp))
        .isEqualTo(dateFormat.format(Date.from(date1.toInstant().plus(Duration.ofMinutes(15)))));
    assertThat(originalRes.get().updatedDate).isAfter(res1WithNoEventsInSystem.updatedDate);

    // Minimum callback time is before the current time so exp and min time are set to current.
    Date date6 = Date.from(date1.toInstant().minus(Duration.ofMinutes(20)));
    Date timeCreatingRes = new Date();
    Reservation resWithWaitTimePassed = createAndPersistReservation(date6);
    assertThat(resWithWaitTimePassed.window.naiveWindow.exp)
        .isEqualTo(resWithWaitTimePassed.window.naiveWindow.min);
    assertThat(resWithWaitTimePassed.window.naiveWindow.exp)
        .isAfter(dateFormat.format(Date.from(date6.toInstant().plus(Duration.ofMinutes(15)))));
    assertThat(resWithWaitTimePassed.window.naiveWindow.exp).isAfter(timeCreatingRes);
    assertThat(resWithWaitTimePassed.window.naiveWindow.exp).isBefore(new Date());
  }

  private Reservation createAndPersistReservation(String topic) {
    return createAndPersistReservation(new Date(), topic, Optional.empty());
  }

  private Reservation createAndPersistReservation(Date requestDate) {
    return createAndPersistReservation(requestDate, businessTopic, Optional.empty());
  }

  private Reservation createAndPersistReservation(
      Date requestDate, Optional<ReservationEventType> reservationEventType) {
    return createAndPersistReservation(requestDate, reservationEventType, Optional.of(new Date()));
  }

  private Reservation createAndPersistReservation(
      Date requestDate,
      Optional<ReservationEventType> reservationEventType,
      Optional<Date> reservationEventDate) {
    if (!reservationEventType.isPresent() || !reservationEventDate.isPresent()) {
      return createAndPersistReservation(requestDate, businessTopic, Optional.empty());
    }
    ReservationEvent event = new ReservationEvent();
    event.type = reservationEventType.get();
    event.date = reservationEventDate.get();
    return createAndPersistReservation(requestDate, businessTopic, Optional.of(event));
  }

  private Reservation createAndPersistReservation(
      Date requestDate, String topic, Optional<ReservationEvent> reservationEvent) {
    Reservation reservation = TestHelpers.createReservation(requestDate, topic, reservationEvent);
    return reservationRepository.save(reservation);
  }
}
