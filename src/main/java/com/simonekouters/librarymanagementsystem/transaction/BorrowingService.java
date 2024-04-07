package com.simonekouters.librarymanagementsystem.transaction;

import com.simonekouters.librarymanagementsystem.book.Book;
import com.simonekouters.librarymanagementsystem.book.BookRepository;
import com.simonekouters.librarymanagementsystem.exceptions.BookNotAvailableException;
import com.simonekouters.librarymanagementsystem.exceptions.BorrowingLimitExceededException;
import com.simonekouters.librarymanagementsystem.exceptions.NotFoundException;
import com.simonekouters.librarymanagementsystem.member.Member;
import com.simonekouters.librarymanagementsystem.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BorrowingService {
    private final BorrowingRepository borrowingRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final ReservationService reservationService;

    private static final int DEFAULT_DUE_DAYS = 21;
    private static final int BORROWING_LIMIT = 10;

    @Transactional
    public Borrowing borrowBook(Long memberId, String isbn) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        Book book = bookRepository.findById(isbn).orElseThrow(NotFoundException::new);
        if (!book.isAvailable()) {
            throw new BookNotAvailableException();
        }
        if (member.getBorrowedBooks().size() == BORROWING_LIMIT) {
            throw new BorrowingLimitExceededException();
        }
        Borrowing borrowing = new Borrowing();
        borrowing.setMember(member);
        book.setAvailable(false);
        borrowing.setBook(book);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setDueDate(LocalDate.now().plusDays(DEFAULT_DUE_DAYS));
        borrowingRepository.save(borrowing);

        member.getBorrowedBooks().add(borrowing);
        memberRepository.save(member);
        return borrowing;
    }

    public Optional<Borrowing> findById(Long id) {
        return borrowingRepository.findById(id);
    }

    @Transactional
    public void returnBook(Borrowing borrowing) {
        borrowing.setReturnDate(LocalDate.now());
        borrowingRepository.save(borrowing);

        var member = borrowing.getMember();
        member.getBorrowedBooks().remove(borrowing);

        var book = borrowing.getBook();
        reservationService.checkForLongestPendingReservation(book);
    }
}
