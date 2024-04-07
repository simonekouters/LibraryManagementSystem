package com.simonekouters.librarymanagementsystem.transaction;

import com.simonekouters.librarymanagementsystem.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("reservations")
public class ReservationController {
    private ReservationService reservationService;

    @PostMapping("{memberId}/{isbn}")
    public ResponseEntity<ReservationDto> addReservation(@PathVariable Long memberId, @PathVariable String isbn, UriComponentsBuilder ucb) {
        var reservation = reservationService.reserveBook(memberId, isbn);
        URI locationOfNewReservation = ucb.path("reservations/{id}").buildAndExpand(reservation.getId()).toUri();
        return ResponseEntity.created(locationOfNewReservation).body(ReservationDto.from(reservation));
    }

    @PatchMapping("{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        var reservation = reservationService.findById(id).orElseThrow(NotFoundException::new);
        reservationService.cancelReservation(reservation);
        return ResponseEntity.ok("Book returned successfully");
    }
}
