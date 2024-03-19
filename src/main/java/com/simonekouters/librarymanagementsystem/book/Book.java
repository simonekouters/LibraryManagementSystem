package com.simonekouters.librarymanagementsystem.book;

import com.simonekouters.librarymanagementsystem.author.Author;
import com.simonekouters.librarymanagementsystem.author.AuthorMapper;
import com.simonekouters.librarymanagementsystem.exceptions.BadInputException;
import com.simonekouters.librarymanagementsystem.genre.Genre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")

public class Book {
    @Id
    private String isbn;

    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private Author author;

    private Integer publicationYear;

//    @ManyToMany
//    private Set<Genre> genres = new HashSet<>();

    public static String[] isValid(BookDto bookDto) {
        return Arrays.stream(new String[]{isbnIsValid(bookDto), publicationYearIsValid(bookDto),
                        titleIsValid(bookDto), authorIsValid(bookDto)})
                .filter(value -> value != null)
                .toArray(String[]::new);
    }

    private static String isbnIsValid(BookDto bookDto) {
        var isbn = bookDto.isbn();
        // remove any hyphens or spaces
        isbn = isbn.replaceAll("[\\s-]+", "");
        // check if it's 13 characters long
        if (isbn.length() != 13 || !isbn.matches("[0-9]")) {
            return "A book requires a valid ISBN";
        }
        return null;
    }

    private static String publicationYearIsValid(BookDto bookDto) {
        var publicationYear = bookDto.publicationYear();
        if (publicationYear == null || publicationYear < 0) {
            return "A book requires a positive publication year";
        }
        return null;
    }

    private static String titleIsValid(BookDto bookDto) {
        var title = bookDto.title();
        if (title == null || title.isBlank()) {
            return "A book requires a (non blank) title";
        }
        return null;
    }

    private static String authorIsValid(BookDto bookDto) {
        var author = AuthorMapper.toEntity(bookDto.authorDto());

        if (author.getName() == null || author.getName().isBlank()) {
            return "A book requires a (non blank) author name";
        }
        if (author.getId() != null) {
            return "Author should not contain an id value, as that is assigned by the database.";
        }
        if (!author.getName().matches("^[^,]+,\\s+[^,]+$")) {
            return "Author name should be entered as 'firstname, lastname'.";
        }
        return null;
    }
}
