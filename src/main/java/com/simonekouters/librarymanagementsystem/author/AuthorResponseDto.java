package com.simonekouters.librarymanagementsystem.author;

public record AuthorResponseDto(Long id, String name, Integer birthYear) {

    public static AuthorResponseDto from(Author author) {
        return new AuthorResponseDto(author.getId(), author.getFormattedAuthorName(), author.getBirthYear());
    }
}