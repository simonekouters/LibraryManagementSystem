package com.simonekouters.librarymanagementsystem.book;

import com.simonekouters.librarymanagementsystem.author.Author;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Book {
    @Id
    private String isbn;

    private String title;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "author_id")
    private Author author;
    private Integer publicationYear;
    private boolean hasBeenDeleted = false;
    private boolean available = true;

    public Book(String isbn, String title, Author author, Integer publicationYear) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
    }
}
