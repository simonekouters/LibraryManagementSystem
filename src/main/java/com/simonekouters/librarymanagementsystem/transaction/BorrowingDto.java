package com.simonekouters.librarymanagementsystem.transaction;

import com.simonekouters.librarymanagementsystem.book.BookResponseDto;
import com.simonekouters.librarymanagementsystem.member.MemberDto;

import java.time.LocalDate;

public record BorrowingDto(Long id, BookResponseDto book, Long memberId, LocalDate borrowDate, LocalDate dueDate) {
    public static BorrowingDto from(Borrowing borrowing) {
        return new BorrowingDto(borrowing.getId(), BookResponseDto.from(borrowing.getBook()), borrowing.getMember().getMemberId(), borrowing.getBorrowDate(), borrowing.getDueDate());
    }
}
