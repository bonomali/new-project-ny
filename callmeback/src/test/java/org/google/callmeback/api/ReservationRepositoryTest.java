package org.google.callmeback.api;

import static org.assertj.core.api.Assertions.assertThat;

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
}
