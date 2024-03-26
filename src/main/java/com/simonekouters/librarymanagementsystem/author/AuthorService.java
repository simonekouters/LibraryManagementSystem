package com.simonekouters.librarymanagementsystem.author;

import com.simonekouters.librarymanagementsystem.book.Validation;
import com.simonekouters.librarymanagementsystem.exceptions.BadInputException;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author createAuthor (Author author) {
        return authorRepository.save(author);
    }

    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    public Optional<Author> findByNameAndBirthYear(String firstName, String lastName, Integer birthYear) {
        return authorRepository.findByFirstNameIgnoringCaseAndLastNameIgnoringCaseAndBirthYear(firstName, lastName, birthYear);
    }

    public Page<Author> findAll(Pageable pageable) {
        return authorRepository.findAll(pageable);
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