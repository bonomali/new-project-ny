package org.google.callmeback.api;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.google.callmeback.model.Reservation;
import org.google.callmeback.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/reservations")
@RestController
public class ReservationController {

  private final ReservationService reservationService;

  @Autowired
  public ReservationController(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @PostMapping
  public Reservation addReservation(@Valid @NonNull @RequestBody Reservation reservation) {
    return reservationService.addReservation(reservation);
  }

  @GetMapping
  public List<Reservation> getAllReservations() {
    return reservationService.getAllReservations();
  }

  /**
   * Returns a Reservation based on the specified id.
   * 
   * Always returns a 200 code, no matter whether the Reservation is found.
   */
  @GetMapping(path = "{id}")
  public Reservation getReservationById(@PathVariable("id") UUID id) {
    return reservationService.getReservationById(id).orElse(null);
  }
}
