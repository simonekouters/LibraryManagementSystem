package com.simonekouters.librarymanagementsystem.book;

import com.simonekouters.librarymanagementsystem.author.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody BookDto bookDto, UriComponentsBuilder ucb) {
        Book newBook = bookService.createNewBook(bookDto);
        URI locationOfNewBook = ucb.path("books/{isbn}").buildAndExpand(newBook.getIsbn()).toUri();
        return ResponseEntity.created(locationOfNewBook).body(BookResponseDto.from(newBook));
    }

    @GetMapping("search/isbns/{isbn}")
    public ResponseEntity<BookResponseDto> getByIsbn(@PathVariable String isbn) {
        Optional<Book> possibleBook = bookService.findByIsbn(isbn);
        return possibleBook
                .map(book -> ResponseEntity.ok(BookResponseDto.from(book)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("search/titles/{query}")
    public ResponseEntity<List<BookResponseDto>> findTitlesContaining(@PathVariable String query) {
        List<BookResponseDto> bookResponseDtos = bookService.findByTitleIgnoringCaseContaining(query).stream()
                .map(BookResponseDto::from)
                .toList();
        return ResponseEntity.ok(bookResponseDtos);
    }

    @GetMapping("search/authors/{name}")
    public ResponseEntity<List<BookResponseDto>> findByAuthor(@PathVariable String name) {
        List<BookResponseDto> bookResponseDtos = bookService.findByAuthorIgnoringCaseContaining(name).stream()
                .map(BookResponseDto::from)
                .toList();
        return ResponseEntity.ok(bookResponseDtos);
    }


    @DeleteMapping("{isbn}")
    public ResponseEntity<Void> delete(@PathVariable String isbn) {
        if (bookService.findByIsbn(isbn).isPresent()) {
            bookService.deleteByIsbn(isbn);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.notFound().build();
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
