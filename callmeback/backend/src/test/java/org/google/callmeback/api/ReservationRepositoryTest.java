package org.google.callmeback.api;

import static org.assertj.core.api.Assertions.assertThat;

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
    assertThat(reservation.reservationCreatedDate).isEqualTo(currentDate);
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
    assertThat(reservation.reservationCreatedDate).isEqualTo(date2);
    assertThat(reservation.events).isNotNull();
    assertThat(reservation.events.size()).isEqualTo(1);
    assertThat(reservation.events.get(0).type).isEqualTo(ReservationEventType.CONNECTED);
    assertThat(reservation.events.get(0).date).isNotNull();

    Reservation reservation2 = reservationRepository.startNextCall();
    assertThat(reservation2.id).isEqualTo(res3.id);
    assertThat(reservation2.reservationCreatedDate).isEqualTo(date3);
    assertThat(reservation2.events).isNotNull();
    assertThat(reservation2.events.size()).isEqualTo(1);
    assertThat(reservation2.events.get(0).type).isEqualTo(ReservationEventType.CONNECTED);
    assertThat(reservation2.events.get(0).date).isNotNull();
  }

  @Test
  public void testReservationWindow_singleReservation() {
    Date requestedDate = new Date();
    Reservation reservation = createAndPersistReservation(requestedDate);

    // There are no other reservations in the system, so the window.min should be equivalent to the
    // window.exp
    ReservationWindow window = reservation.window;
    assertThat(window.exp).isEqualTo(window.min);
    assertThat(window.max).isAfter(window.exp);

    Reservation reservationById = reservationRepository.findById(reservation.id).get();
    window = reservationById.window;
    assertThat(window.exp).isEqualTo(window.min);
    assertThat(window.max).isAfter(window.exp);
  }

  @Test
  public void testProjections() {
    Reservation reservation = new Reservation();
    reservation.reservationCreatedDate = new Date();
    reservation.topic = "Business";
    // ReservationEvent event = new ReservationEvent();
    // event.date = Date.from(
    //     reservation.reservationCreatedDate.toInstant().plus(Duration.ofMinutes(10)));
    // event.type = ReservationEventType.CONNECTED;
    // ReservationEvent event2 = new ReservationEvent();
    // event2.date = Date.from(
    //     reservation.reservationCreatedDate.toInstant().plus(Duration.ofMinutes(20)));
    // event2.type = ReservationEventType.CONNECTED;
    // reservation.events = Lists.newArrayList(event, event2);
    Reservation savedReservation = reservationRepository.save(reservation);

    ReservationWindow window = savedReservation.window;
    assertThat(window.exp).isNotNull();
  }

  @Test
  public void testReservationWindow_multipleReservations() {
    // Older reservations with and without events
    Reservation olderReservationWithNoEvents = createAndPersistReservation(new Date());
    createAndPersistReservation(new Date(), Optional.of(ReservationEventType.CONNECTED));

    // Reservations made with one and then two reservations in the queue
    Reservation reservationWithOneInQueue = createAndPersistReservation(new Date());
    Reservation reservationWithTwoInQueue = createAndPersistReservation(new Date());

    // Newer reservations with and without events
    createAndPersistReservation(new Date());
    createAndPersistReservation(new Date(), Optional.of(ReservationEventType.ATTEMPTED));

    // Verify first reservation has same window minimum as expected callback, because there are no
    // reservations in the queue
    ReservationWindow window = olderReservationWithNoEvents.window;
    assertThat(window.exp).isEqualTo(window.min);
    assertThat(window.max).isAfter(window.exp);
    window = reservationRepository.findById(olderReservationWithNoEvents.id).get().window;
    assertThat(window.exp).isEqualTo(window.min);
    assertThat(window.max).isAfter(window.exp);

    // Verify reservation with one in the queue has the expected callback occur between the minimum
    // and maximum, because we're currently in the middle of the window
    window = reservationWithOneInQueue.window;
    assertThat(window.exp).isAfter(window.min);
    assertThat(window.max).isAfter(window.exp);
    window = reservationRepository.findById(reservationWithOneInQueue.id).get().window;
    assertThat(window.exp).isAfter(window.min);
    assertThat(window.max).isAfter(window.exp);

    // Verify reservation with two in the queue has the expected callback occur between the minimum
    // and maximum, because we're currently before the window
    window = reservationWithTwoInQueue.window;
    assertThat(window.exp).isAfter(window.min);
    assertThat(window.max).isAfter(window.exp);
    window = reservationRepository.findById(reservationWithTwoInQueue.id).get().window;
    assertThat(window.exp).isAfter(window.min);
    assertThat(window.max).isAfter(window.exp);

    // Verify that older reservation window occurs before the more recent reservations
    ReservationWindow olderWindow = olderReservationWithNoEvents.window;
    ReservationWindow newerWindow = reservationWithTwoInQueue.window;
    assertThat(olderWindow.min).isBefore(newerWindow.min);
    assertThat(olderWindow.exp).isBefore(newerWindow.exp);
    assertThat(olderWindow.max).isBefore(newerWindow.max);
  }

  private Reservation createAndPersistReservation(String topic) {
    return createAndPersistReservation(new Date(), topic, Optional.empty());
  }

  private Reservation createAndPersistReservation(Date reservationCreatedDate) {
    return createAndPersistReservation(reservationCreatedDate, businessTopic, Optional.empty());
  }

  private Reservation createAndPersistReservation(
      Date reservationCreatedDate, Optional<ReservationEventType> reservationEventType) {
    return createAndPersistReservation(reservationCreatedDate, businessTopic, reservationEventType);
  }

  private Reservation createAndPersistReservation(
      Date reservationCreatedDate,
      String topic,
      Optional<ReservationEventType> reservationEventType) {
    Reservation reservation = new Reservation();
    reservation.reservationCreatedDate = reservationCreatedDate;
    reservation.topic = topic;
    if (reservationEventType.isPresent()) {
      ReservationEvent event = new ReservationEvent();
      event.date = new Date();
      event.type = reservationEventType.get();
      reservation.events = Lists.newArrayList(event);
    }
    return reservationRepository.save(reservation);
  }
}
