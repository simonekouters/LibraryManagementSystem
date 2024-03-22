package com.simonekouters.librarymanagementsystem.book;

import com.simonekouters.librarymanagementsystem.author.AuthorResponseDto;

public record BookResponseDto(String isbn, String title, AuthorResponseDto author, Integer publicationYear) {
    public static BookResponseDto from(Book book) {
        return new BookResponseDto(book.getIsbn(), book.getTitle(), AuthorResponseDto.from(book.getAuthor()), book.getPublicationYear());
    }
}
