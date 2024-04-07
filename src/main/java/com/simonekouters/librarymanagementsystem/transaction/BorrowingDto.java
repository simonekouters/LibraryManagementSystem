package com.simonekouters.librarymanagementsystem.transaction;

import com.simonekouters.librarymanagementsystem.book.BookResponseDto;
import com.simonekouters.librarymanagementsystem.member.MemberDto;

import java.time.LocalDate;

public record BorrowingDto(BookResponseDto book, MemberDto member, LocalDate borrowDate, LocalDate dueDate) {
    public static BorrowingDto from(Borrowing borrowing) {
        return new BorrowingDto(BookResponseDto.from(borrowing.getBook()), MemberDto.from(borrowing.getMember()), borrowing.getBorrowDate(), borrowing.getDueDate());
    }
}
