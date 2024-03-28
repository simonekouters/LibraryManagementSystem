package com.simonekouters.librarymanagementsystem.author;

import com.simonekouters.librarymanagementsystem.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByFirstNameIgnoringCaseAndLastNameIgnoringCaseAndBirthYear(String firstName, String lastName, int birthYear);
    Page<Author> findAllByHasBeenDeletedFalse(Pageable pageable);

    @Query("SELECT DISTINCT a FROM Author a WHERE (LOWER(a.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(CONCAT(a.firstName, ' ', a.lastName)) LIKE LOWER(CONCAT('%', :fullName, '%')))")
    Page<Author> findByNameIgnoringCaseContaining(@Param("name") String name, @Param("fullName") String fullName, Pageable pageable);
}

