package org.google.callmeback.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import org.assertj.core.util.Lists;
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

  @BeforeEach
  public void setUp() {
    reservationRepository.deleteAll();
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
  public void testReservationWindow_singleReservation() {
    Date requestedDate = new Date();
    Reservation reservation = createAndPersistReservation(requestedDate);

    // There are no other reservations in the system, so the window.naiveMin should be equivalent to the
    // window.naiveExp
    ReservationWindow window = reservation.window;
    assertThat(window.naiveExp).isEqualTo(window.naiveMin);
    assertThat(window.naiveMax).isAfter(window.naiveExp);

    Reservation reservationById = reservationRepository.findById(reservation.id).get();
    window = reservationById.window;
    assertThat(window.naiveExp).isEqualTo(window.naiveMin);
    assertThat(window.naiveMax).isAfter(window.naiveExp);
  }

  @Test
  public void testReservationWindow_multipleReservationsWithMultipleEvents() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Add reservations with and without events and with different delays between them.
    // Reservations with no events (will not be factored in to avg wait time calculation).
    Date date1 = new Date();
    Reservation res1WithNoEventsInSystem = createAndPersistReservation(date1);
    createAndPersistReservation(new Date(), Optional.of(ReservationEventType.ATTEMPTED));
    Reservation res2WithNoEventsInSystem = createAndPersistReservation(date1);

    // Check that the reservations added so far have expected wait time of 0 (exp = min).
    assertThat(res1WithNoEventsInSystem.window.naiveExp).isEqualTo(res1WithNoEventsInSystem.window.naiveMin);
    assertThat(res1WithNoEventsInSystem.window.naiveMax)
        .isEqualTo(
            Date.from(
                res1WithNoEventsInSystem.window.naiveMin.toInstant().plus(Duration.ofMillis(600000))));
    assertThat(res2WithNoEventsInSystem.window.naiveExp).isEqualTo(res2WithNoEventsInSystem.window.naiveMin);
    assertThat(res2WithNoEventsInSystem.window.naiveMax)
        .isEqualTo(
            Date.from(
                res2WithNoEventsInSystem.window.naiveMin.toInstant().plus(Duration.ofMillis(600000))));

    // Reservation with multiple events and 10 minute wait time until first connect.
    Date date2 = new Date();
    Reservation connectedRes =
        createAndPersistReservation(
            date2,
            Optional.of(ReservationEventType.ATTEMPTED),
            Optional.of(Date.from(date2.toInstant().plus(Duration.ofMinutes(5)))));
    // Add a second connected event.
    ReservationEvent event = new ReservationEvent();
    event.date = Date.from(date2.toInstant().plus(Duration.ofMinutes(10)));
    event.type = ReservationEventType.CONNECTED;
    // Add an attempted event.
    ReservationEvent event2 = new ReservationEvent();
    event2.date = Date.from(date2.toInstant().plus(Duration.ofMinutes(15)));
    event2.type = ReservationEventType.CONNECTED;
    connectedRes.events.add(event);
    connectedRes.events.add(event2);
    reservationRepository.save(connectedRes);

    // New reservation should have exp wait time of 10 minutes.
    Date date3 = new Date();
    Reservation resWithOnePriorConnectedRes = createAndPersistReservation(date3);
    
    assertThat(dateFormat.format(resWithOnePriorConnectedRes.window.naiveExp))
        .isEqualTo(dateFormat.format(Date.from(date3.toInstant().plus(Duration.ofMinutes(10)))));

    // Reservation with connected event and 20 minute wait time.
    Date date4 = new Date();
    createAndPersistReservation(
        date4,
        Optional.of(ReservationEventType.CONNECTED),
        Optional.of(Date.from(date4.toInstant().plus(Duration.ofMinutes(20)))));

    // New reservation should have expected wait time of 15 minutes.
    Date date5 = new Date();
    Reservation resWithMultiplePriorConnectedRes = createAndPersistReservation(date5);
    assertThat(dateFormat.format(resWithMultiplePriorConnectedRes.window.naiveExp))
        .isEqualTo(dateFormat.format(Date.from(date5.toInstant().plus(Duration.ofMinutes(15)))));
    assertThat(dateFormat.format(resWithMultiplePriorConnectedRes.window.naiveMin))
        .isEqualTo(
            dateFormat.format(
                Date.from(
                    date5
                        .toInstant()
                        .plus(Duration.ofMinutes(15).minus(Duration.ofMillis(300000))))));

    // Old reservations should have updated wait times.
    Optional<Reservation> originalRes = reservationRepository.findById(res1WithNoEventsInSystem.id);
    assertThat(dateFormat.format(originalRes.get().window.naiveExp))
        .isEqualTo(dateFormat.format(Date.from(date1.toInstant().plus(Duration.ofMinutes(15)))));

    // Minimum callback time is before the current time so exp and min time are set to current.
    Date date6 = Date.from(date1.toInstant().minus(Duration.ofMinutes(20)));
    Date timeCreatingRes = new Date();
    Reservation resWithWaitTimePassed = createAndPersistReservation(date6);
    assertThat(resWithWaitTimePassed.window.naiveExp).isEqualTo(resWithWaitTimePassed.window.naiveMin);
    assertThat(resWithWaitTimePassed.window.naiveExp)
        .isAfter(dateFormat.format(Date.from(date6.toInstant().plus(Duration.ofMinutes(15)))));
    assertThat(resWithWaitTimePassed.window.naiveExp).isAfter(timeCreatingRes);
    assertThat(resWithWaitTimePassed.window.naiveExp).isBefore(new Date());
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
    Reservation reservation = new Reservation();
    reservation.requestDate = requestDate;
    reservation.topic = topic;
    if (reservationEvent.isPresent()) {
      reservation.events = Lists.newArrayList(reservationEvent.get());
    }
    return reservationRepository.save(reservation);
  }
}
