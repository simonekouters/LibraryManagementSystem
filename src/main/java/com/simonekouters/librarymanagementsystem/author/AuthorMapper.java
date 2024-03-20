package com.simonekouters.librarymanagementsystem.author;

public class AuthorMapper {
    public static AuthorDto toDto(Author author) {
        return new AuthorDto(author.getFirstName(), author.getLastName(), author.getBirthYear());
    }

    public static Author toEntity(AuthorDto authorDto) {
        return new Author(authorDto.firstName(), authorDto.lastName(), authorDto.birthYear());
    }
}
