package com.simonekouters.librarymanagementsystem.book;

import com.simonekouters.librarymanagementsystem.author.Author;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
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
