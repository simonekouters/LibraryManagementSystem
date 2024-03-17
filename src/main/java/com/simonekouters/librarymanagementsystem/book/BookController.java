package com.simonekouters.librarymanagementsystem.book;

import com.simonekouters.librarymanagementsystem.book.BookService;
import com.simonekouters.librarymanagementsystem.exceptions.BadInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    // Add a new book
    // Update book information (title, author, genre, ISBN)
    // Delete a book
    // Search for books by title, author or ISBN
    // View book details?

}
