package com.simonekouters.librarymanagementsystem.transaction;

import com.simonekouters.librarymanagementsystem.book.BookResponseDto;
import com.simonekouters.librarymanagementsystem.member.MemberDto;

import java.time.LocalDate;

public record ReservationDto(Long id, BookResponseDto book, MemberDto member, LocalDate reservationDate, ReservationStatus status) {
    public static ReservationDto from(Reservation reservation) {
        return new ReservationDto(reservation.getId(), BookResponseDto.from(reservation.getBook()), MemberDto.from(reservation.getMember()), reservation.getReservationDate(), reservation.getStatus());
    }
}
