package com.simonekouters.librarymanagementsystem.transaction;

import com.simonekouters.librarymanagementsystem.book.Book;
import com.simonekouters.librarymanagementsystem.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private ReservationRepository reservationRepository;

    public void checkForLongestPendingReservation(Book book) {
        Optional<Reservation> longestPendingReservation = reservationRepository.findByBookAndStatus(book, ReservationStatus.PENDING)
                .stream()
                .min(Comparator.comparing(Reservation::getReservationDate));

        if (longestPendingReservation.isPresent()) {
            Member memberToNotify = longestPendingReservation.get().getMember();
            // notify member with an email
        }
    }
}
