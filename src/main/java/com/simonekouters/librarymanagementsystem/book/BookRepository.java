package com.simonekouters.librarymanagementsystem.book;

import com.simonekouters.librarymanagementsystem.author.Author;
import com.simonekouters.librarymanagementsystem.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByTitleIgnoringCaseContaining(String title);

    // Custom query to find a book by either author's full name, first name or last name
    @Query("SELECT b FROM Book b WHERE b.author.firstName LIKE %:name% OR b.author.lastName LIKE %:name%")
    List<Book> findByAuthorNameIgnoringCaseContaining(@Param("name") String name);
}
