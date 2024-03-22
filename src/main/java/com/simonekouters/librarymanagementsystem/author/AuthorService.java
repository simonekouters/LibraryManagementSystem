package com.simonekouters.librarymanagementsystem.author;

import com.simonekouters.librarymanagementsystem.book.Book;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
