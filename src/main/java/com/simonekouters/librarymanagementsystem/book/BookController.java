package com.simonekouters.librarymanagementsystem.book;

import com.simonekouters.librarymanagementsystem.author.Author;
import com.simonekouters.librarymanagementsystem.author.AuthorService;
import com.simonekouters.librarymanagementsystem.exceptions.BadInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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
        Author author = authorService.findByNameAndBirthYear(bookDto.authorDto().firstName(), bookDto.authorDto().lastName(), bookDto.authorDto().birthYear())
                .orElseGet(() -> {
                    Author newAuthor = new Author(bookDto.authorDto().firstName(), bookDto.authorDto().lastName(), bookDto.authorDto().birthYear());
                    authorService.createAuthor(newAuthor);
                    return newAuthor;
                });

        var newBook = new Book(isbn, title, author, bookDto.publicationYear());
        bookService.createBook(newBook);
        URI locationOfNewBook = ucb.path("books/{isbn}").buildAndExpand(newBook.getIsbn()).toUri();
        return ResponseEntity.created(locationOfNewBook).body(BookDto.from(newBook));
    }

    private void verifyThatBookDoesNotYetExist(String isbn) {
        var possibleExistingBook = bookService.findByIsbn(isbn);
        if (possibleExistingBook.isPresent()) {
            throw new BadInputException("Book already exists");
        }
    }

    @GetMapping("search/isbn/{isbn}")
    public ResponseEntity<BookDto> getByIsbn(@PathVariable String isbn) {
        Optional<Book> possibleBook = bookService.findByIsbn(isbn);
        return possibleBook
                .map(book -> ResponseEntity.ok(BookDto.from(book)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("search/title/{query}")
    public ResponseEntity<List<BookDto>> findTitlesContaining(@PathVariable String query) {
        List<BookDto> bookDtos = bookService.findByTitleIgnoringCaseContaining(query).stream()
                .map(BookDto::from)
                .toList();
        return ResponseEntity.ok(bookDtos);
    }

    @GetMapping("search/author/{name}")
    public ResponseEntity<List<BookDto>> findByAuthor(@PathVariable String name) {
        List<BookDto> bookDtos = bookService.findByAuthorIgnoringCaseContaining(name).stream()
                .map(BookDto::from)
                .toList();
        return ResponseEntity.ok(bookDtos);
    }


    @DeleteMapping("{isbn}")
    public ResponseEntity<Void> delete(@PathVariable String isbn) {
        if (bookService.findByIsbn(isbn).isPresent()) {
            bookService.deleteByIsbn(isbn);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.notFound().build();
    }

}
