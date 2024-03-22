package com.simonekouters.librarymanagementsystem.book;

import com.simonekouters.librarymanagementsystem.author.Author;
import com.simonekouters.librarymanagementsystem.author.AuthorService;
import com.simonekouters.librarymanagementsystem.exceptions.BadInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
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
        if (Book.isValid(bookDto).length > 0) {
            var invalidBookArguments = Arrays.toString((Book.isValid(bookDto)));
            throw new BadInputException(invalidBookArguments);
        }
        var isbn = bookDto.isbn().replaceAll("[\\s-]+", "");
        verifyThatBookDoesNotYetExist(isbn);
        var title = bookDto.title().trim();

        // Check if the author already exists, if not, create a new author
        Author author = authorService.findByNameAndBirthYear(bookDto.author().firstName(), bookDto.author().lastName(), bookDto.author().birthYear())
                .orElseGet(() -> {
                    Author newAuthor = new Author(bookDto.author().firstName(), bookDto.author().lastName(), bookDto.author().birthYear());
                    authorService.createAuthor(newAuthor);
                    return newAuthor;
                });

        var newBook = new Book(isbn, title, author, bookDto.publicationYear());
        bookService.createBook(newBook);
        URI locationOfNewBook = ucb.path("books/{isbn}").buildAndExpand(newBook.getIsbn()).toUri();
        return ResponseEntity.created(locationOfNewBook).body(BookResponseDto.from(newBook));
    }

    private void verifyThatBookDoesNotYetExist(String isbn) {
        var possibleExistingBook = bookService.findByIsbn(isbn);
        if (possibleExistingBook.isPresent()) {
            throw new BadInputException("Book already exists");
        }
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


//    @PatchMapping("{isbn}")              // requestbody BookDto instead?
//    public ResponseEntity<BookDto> patch(@RequestBody Book changedBook, @PathVariable String isbn) {
//        // isbns can be changed too, so should be in body, but should it also be in path?
//
//        // what if trying to change author name?
//    }
}
