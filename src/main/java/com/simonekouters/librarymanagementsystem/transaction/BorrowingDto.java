package com.simonekouters.librarymanagementsystem.transaction;

import com.simonekouters.librarymanagementsystem.book.Book;
import com.simonekouters.librarymanagementsystem.member.Member;

import java.time.LocalDate;

public record BorrowingDto(Long id, Book book, Member member, LocalDate borrowDate, LocalDate dueDate) {
    public static BorrowingDto from(Borrowing borrowing) {
        return new BorrowingDto(borrowing.getId(), borrowing.getBook(), borrowing.getMember(), borrowing.getBorrowDate(), borrowing.getDueDate());
    }
}
