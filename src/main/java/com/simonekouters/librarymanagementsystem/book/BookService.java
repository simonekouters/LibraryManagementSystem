package com.simonekouters.librarymanagementsystem.book;

import com.simonekouters.librarymanagementsystem.author.Author;
import com.simonekouters.librarymanagementsystem.author.AuthorService;
import com.simonekouters.librarymanagementsystem.exceptions.BadInputException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;


    @Transactional
    public Book createNewBook(BookDto book) {
        if (!Validation.isValid(book).isEmpty()) {
            var invalidBookArguments = String.join(", ", Validation.isValid(book));
            throw new BadInputException(invalidBookArguments);
        }
        var isbn = book.isbn().replaceAll("[\\s-]+", "");
        verifyThatBookDoesNotYetExist(isbn);
        var title = book.title().trim();

        // Check if the author already exists, if not, create a new author
        Author author = authorService.findByNameAndBirthYear(book.author().firstName(), book.author().lastName(), book.author().birthYear())
                .orElseGet(() -> {
                    Author newAuthor = new Author(book.author().firstName(), book.author().lastName(), book.author().birthYear());
                    authorService.save(newAuthor);
                    return newAuthor;
                });

        var newBook = new Book(isbn, title, author, book.publicationYear());
        return bookRepository.save(newBook);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    public Book updateExistingBook(Book originalBook, BookDto changedBook) {
        String newIsbn = changedBook.isbn();
        if (newIsbn != null) {
            validateAndSetIsbn(originalBook, changedBook, newIsbn);
        }

        String newTitle = changedBook.title();
        if (newTitle != null) {
            validateAndSetTitle(originalBook, changedBook, newTitle);
        }

        if (changedBook.author() != null) {
            throw new BadInputException("Authors can only be updated separately.");
        }

        Integer newPublicationYear = changedBook.publicationYear();
        if (newPublicationYear != null) {
            validateAndSetPublicationYear(originalBook, changedBook, newPublicationYear);
        }
        return bookRepository.save(originalBook);
    }

    private void validateAndSetIsbn(Book originalBook, BookDto changedBook, String newIsbn) {
        verifyThatBookDoesNotYetExist(newIsbn);
        String isbnValidationResult = Validation.isbnIsValid(changedBook);
        if (isbnValidationResult != null) {
            throw new BadInputException(isbnValidationResult);
        }
        originalBook.setIsbn(newIsbn);
    }

    private void validateAndSetTitle(Book originalBook, BookDto changedBook, String newTitle) {
        String titleValidationResult = Validation.titleIsValid(changedBook);
        if (titleValidationResult != null) {
            throw new BadInputException(titleValidationResult);
        }
        originalBook.setTitle(newTitle);
    }

    private void validateAndSetPublicationYear(Book originalBook, BookDto changedBook, Integer newPublicationYear) {
        String publicationYearValidationResult = Validation.publicationYearIsValid(changedBook);
        if (publicationYearValidationResult != null) {
            throw new BadInputException(publicationYearValidationResult);
        }
        originalBook.setPublicationYear(newPublicationYear);
    }

    private void verifyThatBookDoesNotYetExist(String isbn) {
        var possibleExistingBook = findByIsbn(isbn);
        if (possibleExistingBook.isPresent()) {
            throw new BadInputException("A book with that ISBN already exists");
        }
    }

    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbnAndHasBeenDeletedFalse(isbn);
    }

    public Page<Book> findByTitleIgnoringCaseContaining(String title, Pageable pageable) {
        return bookRepository.findByTitleIgnoringCaseContainingAndHasBeenDeletedFalse(title, pageable);
    }

    public Page<Book> findByAuthorIgnoringCaseContaining(String name, Pageable pageable) {
        return bookRepository.findByAuthorNameIgnoringCaseContaining(name, name, pageable);
    }

    public void delete(Book book) {
        book.setHasBeenDeleted(true);
        save(book);

        // if an author doesn't have any books in the system anymore, (soft) delete the author
        Author author = book.getAuthor();
        boolean hasNonDeletedBooks = author.getBooks().stream()
                .anyMatch(b -> !b.isHasBeenDeleted());

        if (!hasNonDeletedBooks) {
            author.setHasBeenDeleted(true);
            authorService.save(author);
        }
    }
}
