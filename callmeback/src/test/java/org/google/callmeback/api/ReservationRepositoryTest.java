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

  // TODO Create a builder to handle the creation of Reservations.
  private Reservation createAndPersistReservation(String topic) {
    return createAndPersistReservation(new Date(), topic, Optional.empty());
  }

  private Reservation createAndPersistReservation(Date reservationCreatedDate) {
    return createAndPersistReservation(reservationCreatedDate, businessTopic, Optional.empty());
  }

  private Reservation createAndPersistReservation(Date reservationCreatedDate,
      Optional<ReservationEventType> reservationEventType) {
    return createAndPersistReservation(reservationCreatedDate, businessTopic, reservationEventType);
  }
  
  private Reservation createAndPersistReservation(Date reservationCreatedDate, String topic,
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
