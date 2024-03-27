package com.simonekouters.librarymanagementsystem.book;

import com.simonekouters.librarymanagementsystem.author.Author;
import com.simonekouters.librarymanagementsystem.author.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody BookDto bookDto, UriComponentsBuilder ucb) {
        Book newBook = bookService.createNewBook(bookDto);
        URI locationOfNewBook = ucb.path("books/{isbn}").buildAndExpand(newBook.getIsbn()).toUri();
        return ResponseEntity.created(locationOfNewBook).body(BookResponseDto.from(newBook));
    }


    @GetMapping("search/isbns/{isbn}")
    public ResponseEntity<BookResponseDto> getByIsbn(@PathVariable("isbn") String isbn) {
        Optional<Book> possibleBook = bookService.findByIsbn(isbn);
        return possibleBook
                .map(book -> ResponseEntity.ok(BookResponseDto.from(book)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("search/titles/{query}")
    public Page<BookResponseDto> findTitlesContaining(@PathVariable("query") String query, Pageable pageable) {
        return bookService.findByTitleIgnoringCaseContaining(query, pageable)
                .map(BookResponseDto::from);
    }

    @GetMapping("search/authors/{name}")
    public Page<BookResponseDto> findByAuthor(@PathVariable("name") String name, Pageable pageable) {
        return bookService.findByAuthorIgnoringCaseContaining(name, pageable)
                .map(BookResponseDto::from);
    }

    @DeleteMapping("{isbn}")
    public ResponseEntity<Void> delete(@PathVariable("isbn") String isbn) {
        Optional<Book> optionalBook = bookService.findByIsbn(isbn);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setHasBeenDeleted(true);
            bookService.save(book);

            // if an author doesn't have any books in the system anymore, (soft) delete the author
            Author author = book.getAuthor();
            boolean hasNonDeletedBooks = author.getBooks().stream()
                    .anyMatch(b -> !b.isHasBeenDeleted());

            if (!hasNonDeletedBooks) {
                author.setHasBeenDeleted(true);
                authorService.save(author);
            }
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PatchMapping("{isbn}")
    public ResponseEntity<BookDto> patch(@PathVariable String isbn, @RequestBody BookDto changedBook) {
        var possibleOriginalBook = bookService.findByIsbn(isbn);
        if (possibleOriginalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var originalBook = possibleOriginalBook.get();

        Book updatedBook = bookService.updateExistingBook(originalBook, changedBook);
        return ResponseEntity.ok(BookDto.from(updatedBook));
    }
}
