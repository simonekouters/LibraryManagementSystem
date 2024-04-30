package com.simonekouters.librarymanagementsystem.member;

import com.simonekouters.librarymanagementsystem.book.Book;
import com.simonekouters.librarymanagementsystem.transaction.Borrowing;
import com.simonekouters.librarymanagementsystem.transaction.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Member {
    @Id
    @SequenceGenerator(name = "member_number", initialValue = 68860880, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "member_number")
    private Long memberId;

    private String firstName;
    private String lastName;
    private String password;

    @Column(unique = true)
    private String email;

    @ManyToMany(mappedBy = "member")
    private Set<Borrowing> borrowedBooks = new HashSet<>();

    @ManyToMany(mappedBy = "member")
    private Set<Reservation> reservedBooks = new HashSet<>();

    private boolean enabled = true;

    public Member(String firstName, String lastName, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }
}
