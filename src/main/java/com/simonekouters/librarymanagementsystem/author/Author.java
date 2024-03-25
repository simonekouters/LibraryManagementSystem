package com.simonekouters.librarymanagementsystem.author;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "authors")

public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String firstName;
    private String lastName;
    private Integer birthYear;

    public Author(String firstName, String lastName, Integer birthYear) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
    }

    public static Author from(AuthorDto authorDto) {
        return new Author(authorDto.firstName(), authorDto.lastName(), authorDto.birthYear());
    }

    public String getFormattedAuthorName() {
        return lastName + ", " + firstName;
    }
}
