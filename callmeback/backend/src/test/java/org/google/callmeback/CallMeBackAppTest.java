package org.google.callmeback;

import static org.assertj.core.api.Assertions.assertThat;

import org.google.callmeback.api.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CallMeBackAppTest {
  @Autowired
  private ReservationRepository reservationRepository;

  @Test
  public void contextLoads() {
    assertThat(reservationRepository).isNotNull();
  }
}
