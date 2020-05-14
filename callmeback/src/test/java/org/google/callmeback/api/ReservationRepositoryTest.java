package org.google.callmeback.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;

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
    Reservation unsavedReservation = new Reservation();
    Reservation reservation = reservationRepository.save(unsavedReservation);

    assertThat(reservation.id).isNotNull();
    assertThat(reservationRepository.findAll().size()).isEqualTo(1);
  }

  @Test
  public void testFindByTopic_singleReservation() {
    String businessTopic = "Business";
    String unemploymentTopic = "Unemployment";
    Reservation unsavedReservation = new Reservation();
    unsavedReservation.topic = businessTopic;
    Reservation reservation = reservationRepository.save(unsavedReservation);

    assertThat(reservation.id).isNotNull();
    assertThat(reservation.topic).isEqualTo(businessTopic);
    assertThat(reservationRepository.findByTopic(businessTopic).size()).isEqualTo(1);
    assertThat(reservationRepository.findByTopic(unemploymentTopic).size()).isEqualTo(0);
  }

  @Test
  public void testFindByTopic_multipleReservations() {
    Reservation reservation1 = new Reservation();
    reservation1.topic = businessTopic;
    reservation1 = reservationRepository.save(reservation1);

    Reservation reservation2 = new Reservation();
    reservation2.topic = businessTopic;
    reservation2 = reservationRepository.save(reservation2);

    Reservation reservation3 = new Reservation();
    reservation3.topic = unemploymentTopic;
    reservation3 = reservationRepository.save(reservation3);

    // TODO: Confirm the correct reservation is in the list
    assertThat(reservationRepository.findByTopic(businessTopic).size()).isEqualTo(2);
    assertThat(reservationRepository.findByTopic(unemploymentTopic).size()).isEqualTo(1);
    assertThat(reservationRepository.findByTopic(dmvTopic).size()).isEqualTo(0);
  }

  @Test
  public void testStartNextCall_setsEventForSingleCall() {
    Reservation unsavedReservation = new Reservation();
    Date currentDate = new Date();
    unsavedReservation.reservationCreatedDate = currentDate;
    reservationRepository.save(unsavedReservation);

    Reservation reservation = reservationRepository.startNextCall();
    assertThat(reservation.id).isNotNull();
    assertThat(reservation.reservationCreatedDate).isEqualTo(currentDate);
    assertThat(reservation.events).isNotNull();
    assertThat(reservation.events.size()).isEqualTo(1);
    assertThat(reservation.events.get(0).type).isEqualTo(ReservationEventType.ATTEMPTED);
  }

  @Test
  public void testStartNextCall_setsEventForMostRecentUnresolvedCall() {
    Reservation res1 = new Reservation();
    Date date1 = new Date();
    res1.reservationCreatedDate = date1;
    res1.events = new ArrayList<ReservationEvent>();
    ReservationEvent resEvent = new ReservationEvent();
    resEvent.date = new Date();
    resEvent.type = ReservationEventType.ATTEMPTED;
    res1.events.add(resEvent);
    reservationRepository.save(res1);
  
    Reservation res2 = new Reservation();
    Date date2 = new Date();
    res2.reservationCreatedDate = date2;
    reservationRepository.save(res2);
  
    Reservation res3 = new Reservation();
    Date date3 = new Date();
    res3.reservationCreatedDate = date3;
    reservationRepository.save(res3);

    Reservation reservation = reservationRepository.startNextCall();
    assertThat(reservation.id).isNotNull();
    assertThat(reservation.reservationCreatedDate).isEqualTo(date2);
    assertThat(reservation.events).isNotNull();
    assertThat(reservation.events.size()).isEqualTo(1);
    assertThat(reservation.events.get(0).type).isEqualTo(ReservationEventType.ATTEMPTED);

    Reservation reservation2 = reservationRepository.startNextCall();
    assertThat(reservation2.id).isNotNull();
    assertThat(reservation2.reservationCreatedDate).isEqualTo(date3);
    assertThat(reservation2.events).isNotNull();
    assertThat(reservation2.events.size()).isEqualTo(1);
    assertThat(reservation2.events.get(0).type).isEqualTo(ReservationEventType.ATTEMPTED);
  }
}
