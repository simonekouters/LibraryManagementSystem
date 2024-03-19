package com.simonekouters.librarymanagementsystem.author;

public class AuthorMapper {
    public static AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getName(), author.getBirthYear());
    }

    public static Author toEntity(AuthorDto authorDto) {
        return new Author(authorDto.name(), authorDto.birthYear());
    }
}
