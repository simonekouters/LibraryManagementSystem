package com.simonekouters.librarymanagementsystem.book;

import com.simonekouters.librarymanagementsystem.author.AuthorService;
import com.simonekouters.librarymanagementsystem.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;


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
        var optionalBook = bookService.findByIsbn(isbn);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            bookService.delete(book);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @DeleteMapping("{isbn}")
//    public ResponseEntity<Void> delete1(@PathVariable("isbn") String isbn) {
//        Book book = bookService.findByIsbn(isbn).orElseThrow(NotFoundException::new);
//        bookService.delete(book);
//        return ResponseEntity.noContent().build();
//    }

    @PatchMapping("{isbn}")
    public ResponseEntity<BookDto> updateBookDetails(@PathVariable String isbn, @RequestBody BookDto changedBook) {
        var possibleOriginalBook = bookService.findByIsbn(isbn);
        if (possibleOriginalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var originalBook = possibleOriginalBook.get();

        Book updatedBook = bookService.updateExistingBook(originalBook, changedBook);
        return ResponseEntity.ok(BookDto.from(updatedBook));
    }

    @PatchMapping("{isbn}/author/{id}")
    public ResponseEntity<BookDto> changeBookAuthor(@PathVariable String isbn, @PathVariable Long id) {
        var possiblyExistingBook = bookService.findByIsbn(isbn);
        if (possiblyExistingBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var book = possiblyExistingBook.get();

        var possiblyExistingAuthor = authorService.findById(id);
        if (possiblyExistingAuthor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var newAuthor = possiblyExistingAuthor.get();
        book.setAuthor(newAuthor);

        bookService.save(book);
        return ResponseEntity.ok(BookDto.from(book));
    }
}
