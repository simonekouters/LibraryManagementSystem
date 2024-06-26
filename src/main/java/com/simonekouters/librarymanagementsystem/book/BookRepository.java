package com.simonekouters.librarymanagementsystem.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, String> {
    Optional<Book> findByIsbnAndHasBeenDeletedFalse(String isbn);
    Page<Book> findByTitleIgnoringCaseContainingAndHasBeenDeletedFalse(String title, Pageable pageable);

    // Custom query to find a book by author's full name, first name or last name (to avoid a lengthy method name)
    @Query("SELECT b FROM Book b WHERE (LOWER(b.author.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(b.author.lastName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(CONCAT(b.author.firstName, ' ', b.author.lastName)) LIKE LOWER(CONCAT('%', :fullName, '%'))) " +
            "AND b.hasBeenDeleted = false")
    Page<Book> findByAuthorNameIgnoringCaseContaining(@Param("name") String name, @Param("fullName") String fullName, Pageable pageable);
}
