package com.simonekouters.librarymanagementsystem;

import com.simonekouters.librarymanagementsystem.author.Author;
import com.simonekouters.librarymanagementsystem.book.Book;
import com.simonekouters.librarymanagementsystem.book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
    private final BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        if (bookRepository.count() == 0) {
            var book1 = new Book("9780312305062", "The Hours", new Author("Michael", "Cunningham", 1952), 1998);
            var book2 = new Book("9780156949606", "The Waves", new Author("Virginia", "Woolf", 1882), 1931);
            var book3 = new Book("9780141393391", "Frankenstein", new Author("Mary", "Shelley", 1797), 1818);
            var book4 = new Book("9780374104092", "Annihilation", new Author("Jeff", "VanderMeer", 1968), 2014);
            var book5 = new Book("9781635575637", "Piranesi", new Author("Susanna", "Clark", 1959), 2020);
            var book6 = new Book("9780446675505", "Parable of the Sower", new Author("Octavia", "Butler", 1947), 1993);
            var book7 = new Book("9780330426640", "Stories of Your Life and Others", new Author("Ted", "Chiang", 1967), 2002);
            bookRepository.saveAll(List.of(book1, book2, book3, book4, book5, book6, book7));
        }

    }
}
