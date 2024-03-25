package com.simonekouters.librarymanagementsystem.book;

import com.simonekouters.librarymanagementsystem.author.Author;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Validation {
    public static List<String> isValid(BookDto bookDto) {
        var allErrors = new ArrayList<String>(Arrays.asList(isbnIsValid(bookDto), publicationYearIsValid(bookDto),
                titleIsValid(bookDto)));
        allErrors.addAll(authorIsValid(bookDto));
        return allErrors.stream()
                .filter(value -> value != null)
                .toList();
    }

    public static String isbnIsValid(BookDto bookDto) {
        var isbn = bookDto.isbn();
        if (isbn == null) {
            return "A book requires an ISBN.";
        }
        // Remove any hyphens or spaces
        isbn = isbn.replaceAll("[\\s-]+", "");
        // Check if it's 13 characters long
        if (isbn.length() != 13 || !isbn.matches("[0-9]+")) {
            return "A book requires an ISBN of 13 characters.";
        }
        // Validate ISBN-13 using Luhn algorithm
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += digit * (i % 2 == 0 ? 1 : 3);
        }
        int checkDigit = Character.getNumericValue(isbn.charAt(12));
        int remainder = sum % 10;
        int checksum = (10 - remainder) % 10;

        if (checksum != checkDigit) {
            return "A book requires a valid ISBN.";
        }
        return null;
    }

    public static String publicationYearIsValid(BookDto bookDto) {
        var publicationYear = bookDto.publicationYear();
        if (publicationYear == null || publicationYear < 0) {
            return "A book requires a positive publication year";
        }
        return null;
    }

    public static String titleIsValid(BookDto bookDto) {
        var title = bookDto.title();
        if (title == null || title.isBlank()) {
            return "A book requires a (non blank) title";
        }
        return null;
    }

    public static List<String> authorIsValid(BookDto bookDto) {
        var author = Author.from(bookDto.author());
        if (author.getId() != null) {
            return List.of("Author should not contain an id value, as that is assigned by the database.");
        }
        return Arrays.asList(authorFirstNameIsValid(author), authorLastNameIsValid(author), authorBirthYearIsValid(author));
    }

    public static String authorFirstNameIsValid(Author author) {
        if (author.getFirstName() == null || author.getFirstName().isBlank()) {
            return "A book requires a (non blank) author's first name.";
        }
        return null;
    }

    public static String authorLastNameIsValid(Author author) {
        if (author.getLastName() == null || author.getLastName().isBlank()) {
            return "A book requires a (non blank) author's last name.";
        }
        return null;
    }

    public static String authorBirthYearIsValid(Author author) {
        if (author.getBirthYear() == null || author.getBirthYear() < 0) {
            return "An author requires a valid birth year.";
        }
        return null;
    }
}
