package org.google.callmeback.api;

import static org.assertj.core.api.Assertions.assertThat;

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
  public void testCountByEventsNullAndReservationCreatedDateLessThan() {
    // Two reservations before the test date without events, and one with events.
    createAndPersistReservation(new Date());
    createAndPersistReservation(new Date());
    createAndPersistReservation(new Date(), Optional.of(ReservationEventType.CONNECTED));

    // One reservation with equivalent test date.
    Date testDate = new Date();
    createAndPersistReservation(testDate);

    // One reservation after the test date without events, and one with events.
    createAndPersistReservation(new Date());
    createAndPersistReservation(new Date(), Optional.of(ReservationEventType.ATTEMPTED));

    assertThat(reservationRepository.countByEventsNullAndReservationCreatedDateLessThan(testDate))
        .isEqualTo(2);
  }

  @Test
  public void testReservationWindow() {
    Date requestedDate = new Date();
    Reservation reservation = createAndPersistReservation(requestedDate);
    ReservationWindow window = reservation.window;
    assertThat(window).isNotNull();
    assertThat(window.min).isNotNull();
    assertThat(window.exp).isNotNull();
    assertThat(window.max).isNotNull();
  }

  // TODO Create a builder to handle the creation of Reservations.
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
