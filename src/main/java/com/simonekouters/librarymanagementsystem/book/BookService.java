package com.simonekouters.librarymanagementsystem.book;

import com.simonekouters.librarymanagementsystem.author.Author;
import com.simonekouters.librarymanagementsystem.book.Book;
import com.simonekouters.librarymanagementsystem.book.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(String isbn, Book book) {
        book.setIsbn(isbn); // make sure that the book we create will always have the isbn provide in URL
        return bookRepository.save(book);
    }

    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> findByTitleIgnoringCaseContaining(String title) {
        return bookRepository.findByTitleIgnoringCaseContaining(title);
    }

    public List<Book> findByAuthorIgnoringCaseContaining(Author author) {
        return bookRepository.findByAuthorIgnoringCaseContaining(author);
    }
}
