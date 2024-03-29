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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String password;

    @Column(unique = true)
    private String email;
    private double fine;

    @ManyToMany
    private Set<Book> borrowedBooks = new HashSet<>();

    @ManyToMany
    private Set<Book> reservedBooks = new HashSet<>();
    
}
