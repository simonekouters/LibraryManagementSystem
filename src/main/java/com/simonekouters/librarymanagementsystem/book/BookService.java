package com.simonekouters.librarymanagementsystem.book;

import com.simonekouters.librarymanagementsystem.author.Author;
import com.simonekouters.librarymanagementsystem.author.AuthorService;
import com.simonekouters.librarymanagementsystem.exceptions.BadInputException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;

    public BookService(BookRepository bookRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

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
                    authorService.createAuthor(newAuthor);
                    return newAuthor;
                });

        var newBook = new Book(isbn, title, author, book.publicationYear());
        return bookRepository.save(newBook);
    }

    public Book updateExistingBook(Book originalBook, BookDto changedBook) {
        var newIsbn = changedBook.isbn();
        // check if a book with that ISBN doesn't exist yet
        verifyThatBookDoesNotYetExist(newIsbn);
        if (newIsbn != null) {
            if (Validation.isbnIsValid(changedBook) != null) {
                throw new BadInputException(Validation.isbnIsValid(changedBook));
            } else {
                originalBook.setIsbn(newIsbn);
            }
        }

        var author = Author.from(changedBook.author());
        var newAuthorFirstName = changedBook.author().firstName();
        if (newAuthorFirstName != null) {
            if (Validation.authorFirstNameIsValid(author) != null) {
                throw new BadInputException(Validation.authorFirstNameIsValid(author));
            } else {
                originalBook.getAuthor().setFirstName(newAuthorFirstName);
            }
        }

        var newAuthorLastName = changedBook.author().lastName();
        if (newAuthorLastName != null) {
            if (Validation.authorLastNameIsValid(author) != null) {
                throw new BadInputException(Validation.authorLastNameIsValid(author));
            } else {
                originalBook.getAuthor().setLastName(newAuthorLastName);
            }
        }

        var newAuthorBirthYear = changedBook.author().birthYear();
        if (newAuthorBirthYear != null) {
            if (Validation.authorBirthYearIsValid(author) != null) {
                throw new BadInputException(Validation.authorBirthYearIsValid(author));
            } else {
                originalBook.getAuthor().setBirthYear(newAuthorBirthYear);
            }
        }

        var newTitle = changedBook.title();
        if (newTitle != null) {
            if (Validation.titleIsValid(changedBook) != null) {
                throw new BadInputException(Validation.titleIsValid(changedBook));
            } else {
                originalBook.setTitle(newTitle);
            }
        }

        var newPublicationYear = changedBook.publicationYear();
        if (newPublicationYear != null) {
            if (Validation.publicationYearIsValid(changedBook) != null) {
                throw new BadInputException(Validation.publicationYearIsValid(changedBook));
            } else {
                originalBook.setPublicationYear(newPublicationYear);
            }
        }
        return bookRepository.save(originalBook);
    }

    private void verifyThatBookDoesNotYetExist(String isbn) {
        var possibleExistingBook = findByIsbn(isbn);
        if (possibleExistingBook.isPresent()) {
            throw new BadInputException("A book with that ISBN already exists");
        }
    }

    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> findByTitleIgnoringCaseContaining(String title) {
        return bookRepository.findByTitleIgnoringCaseContaining(title);
    }

    public List<Book> findByAuthorIgnoringCaseContaining(String name) {
        return bookRepository.findByAuthorNameIgnoringCaseContaining(name, name);
    }

    public void deleteByIsbn(String isbn) {
        bookRepository.deleteById(isbn);
    }
}
