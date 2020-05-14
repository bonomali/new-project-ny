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
        assertThat(reservationRepository.findAll())
            .containsExactlyInAnyOrder(reservation);
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
        assertThat(reservationRepository.findByTopic(businessTopic))
            .containsExactlyInAnyOrder(reservation);
        assertThat(reservationRepository.findByTopic(unemploymentTopic))
            .isEmpty();
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

        assertThat(reservationRepository.findByTopic(businessTopic))
            .containsExactlyInAnyOrder(reservation1, reservation2);
        assertThat(reservationRepository.findByTopic(unemploymentTopic))
            .containsExactlyInAnyOrder(reservation3);
        assertThat(reservationRepository.findByTopic(dmvTopic)).isEmpty();
    }
}
