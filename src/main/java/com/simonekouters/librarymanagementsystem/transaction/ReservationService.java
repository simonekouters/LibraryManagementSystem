package com.simonekouters.librarymanagementsystem.transaction;

import com.simonekouters.librarymanagementsystem.book.Book;
import com.simonekouters.librarymanagementsystem.book.BookRepository;
import com.simonekouters.librarymanagementsystem.exceptions.BorrowingLimitExceededException;
import com.simonekouters.librarymanagementsystem.exceptions.NotFoundException;
import com.simonekouters.librarymanagementsystem.exceptions.ReservationLimitExceededException;
import com.simonekouters.librarymanagementsystem.member.Member;
import com.simonekouters.librarymanagementsystem.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private static final int RESERVATION_LIMIT = 5;

    public void checkForLongestPendingReservation(Book book) {
        Optional<Reservation> longestPendingReservation = reservationRepository.findByBookAndStatus(book, ReservationStatus.PENDING)
                .stream()
                .min(Comparator.comparing(Reservation::getReservationDate));

        if (longestPendingReservation.isPresent()) {
            Member memberToNotify = longestPendingReservation.get().getMember();
            // notify member with an email
        }
    }



    public Reservation reserveBook(Long memberId, String isbn) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        Book book = bookRepository.findById(isbn).orElseThrow(NotFoundException::new);
        if (member.getBorrowedBooks().size() == RESERVATION_LIMIT) {
            throw new ReservationLimitExceededException();
        }
        Reservation reservation = new Reservation();
        reservation.setMember(member);
        reservation.setBook(book);
        reservation.setReservationDate(LocalDate.now());
        reservationRepository.save(reservation);

        member.getReservedBooks().add(reservation);
        memberRepository.save(member);
        return reservation;
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    public void cancelReservation(Reservation reservation) {
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }
}
