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
            var requestProblems = Arrays.toString((Book.isValid(bookDto)));
            var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, requestProblems);
            return ResponseEntity.badRequest().body(problemDetail); //// Implement separate ExceptionControllerAdvice class
        }
        var isbn = bookDto.isbn().replaceAll("[\\s-]+", "");
        verifyThatBookDoesNotYetExist(isbn);
        var title = bookDto.title().trim();

        Author author = authorService.findByName(bookDto.authorDto().name())
                .stream()
                // if there are several authors with same name, check birth year to find the right one
                .filter(a -> a.getBirthYear().equals(bookDto.authorDto().birthYear()))
                .findFirst()
                .orElseGet(() -> {
                    Author newAuthor = new Author(bookDto.authorDto().name(), bookDto.authorDto().birthYear());
                    // if author doesn't exist yet, save new author in database
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

    @GetMapping("search/{isbn}")
    public ResponseEntity<BookDto> getByIsbn(@PathVariable String isbn) {
        Optional<Book> possibleBook = bookService.findByIsbn(isbn);
        return possibleBook
                .map(book -> ResponseEntity.ok(BookDto.from(book)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("search/{title}")
    public ResponseEntity<List<BookDto>> findTitlesContaining(@PathVariable String query) {
        List<BookDto> bookDtos = bookService.findByTitleIgnoringCaseContaining(query).stream()
                .map(BookDto::from)
                .toList();
        return ResponseEntity.ok(bookDtos);
    }

    @GetMapping("search/{author}")
    public ResponseEntity<List<BookDto>> findByAuthor(@PathVariable String name) {

    }


    @DeleteMapping("{isbn}")
    public ResponseEntity<Void> delete(@PathVariable String isbn) {
        if (bookService.findByIsbn(isbn).isPresent()) {
            bookService.deleteByIsbn(isbn);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.notFound().build();
    }

}
