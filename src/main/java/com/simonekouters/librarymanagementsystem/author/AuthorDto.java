package com.simonekouters.librarymanagementsystem.author;

public record AuthorDto(Long id, String name, Integer birthYear) {
    public static AuthorDto from(Author author) {
        return new AuthorDto(author.getId(), author.getName(), author.getBirthYear());
    }
}
