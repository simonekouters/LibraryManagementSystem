package com.simonekouters.librarymanagementsystem.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByFirstNameIgnoringCaseAndLastNameIgnoringCaseAndBirthYear(String firstName, String lastName, int birthYear);
    Page<Author> findAllByHasBeenDeletedFalse(Pageable pageable);
}

