package com.simonekouters.librarymanagementsystem.transaction;

import com.simonekouters.librarymanagementsystem.book.Book;
import com.simonekouters.librarymanagementsystem.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Borrowings")

public class Borrowing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Temporal(TemporalType.DATE)
    private LocalDate borrowDate;

    @Temporal(TemporalType.DATE)
    private LocalDate dueDate;

    @Temporal(TemporalType.DATE)
    private LocalDate returnDate;
}
