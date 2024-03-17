package com.simonekouters.librarymanagementsystem.book;

import com.simonekouters.librarymanagementsystem.author.AuthorDto;

public record BookDto(String isbn, String title, AuthorDto authorDto) {
    public static BookDto from(Book book) {
        return new BookDto(book.getIsbn(), book.getTitle(), AuthorDto.from(book.getAuthor()));
    }
}
