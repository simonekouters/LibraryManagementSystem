package com.simonekouters.librarymanagementsystem.author;

public record AuthorResponseDto(String name, Integer birthYear) {

    public static AuthorResponseDto from(Author author) {
        return new AuthorResponseDto(author.getFormattedAuthorName(), author.getBirthYear());
    }
}