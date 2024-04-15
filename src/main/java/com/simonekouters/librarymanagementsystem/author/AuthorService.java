package com.simonekouters.librarymanagementsystem.author;

import com.simonekouters.librarymanagementsystem.book.Book;
import com.simonekouters.librarymanagementsystem.book.BookResponseDto;
import com.simonekouters.librarymanagementsystem.book.Validation;
import com.simonekouters.librarymanagementsystem.exceptions.BadInputException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author save (Author author) {
        return authorRepository.save(author);
    }

    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    public Optional<Author> findByNameAndBirthYear(String firstName, String lastName, Integer birthYear) {
        return authorRepository.findByFirstNameIgnoringCaseAndLastNameIgnoringCaseAndBirthYear(firstName, lastName, birthYear);
    }

    public Page<Author> findAll(Pageable pageable) {
        Pageable pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "lastName")
        );
        return authorRepository.findAllByHasBeenDeletedFalse(pageRequest);
    }

    public Page<Author> findByName(String name, Pageable pageable) {
        Pageable pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
        return authorRepository.findByNameIgnoringCaseContaining(name, name, pageRequest);
    }

    public Page<BookResponseDto> getAllBooksByAuthorId(Author author, Pageable pageable) {
        Pageable pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "publicationYear")
        );
        List<Book> books = new ArrayList<>(author.getBooks());
        Page<Book> bookPage = new PageImpl<>(books, pageRequest, books.size());
        return bookPage.map(BookResponseDto::from);
    }

    public Author updateAuthor(Author originalAuthor, AuthorDto changedAuthor) {
        Author newAuthor = Author.from(changedAuthor);
        if (newAuthor.getId() != null) {
            throw new BadInputException("id cannot be changed, please pass only changeable fields in the body");
        }

        String newAuthorFirstName = newAuthor.getFirstName();
        if (newAuthorFirstName != null) {
            if (Validation.authorFirstNameIsValid(newAuthor) != null) {
                throw new BadInputException(Validation.authorFirstNameIsValid(newAuthor));
            }
            originalAuthor.setFirstName(newAuthorFirstName);
        }

        String newAuthorLastName = newAuthor.getLastName();
        if (newAuthorLastName != null) {
            if (Validation.authorLastNameIsValid(newAuthor) != null) {
                throw new BadInputException(Validation.authorLastNameIsValid(newAuthor));
            }
            originalAuthor.setLastName(newAuthorLastName);
        }

        Integer newAuthorBirthYear = newAuthor.getBirthYear();
        if (newAuthorBirthYear != null) {
            if (Validation.authorBirthYearIsValid(newAuthor) != null) {
                throw new BadInputException(Validation.authorBirthYearIsValid(newAuthor));
            }
            originalAuthor.setBirthYear(newAuthorBirthYear);
        }
        return authorRepository.save(originalAuthor);
    }
}