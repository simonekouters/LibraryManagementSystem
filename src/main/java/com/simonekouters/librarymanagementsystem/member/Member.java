package com.simonekouters.librarymanagementsystem.member;

import com.simonekouters.librarymanagementsystem.book.Book;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "members")

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

    @ManyToMany
    private Set<Book> borrowedBooks = new HashSet<>();

    @ManyToMany
    private Set<Book> reservedBooks = new HashSet<>();

    public Member(String firstName, String lastName, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }
}
