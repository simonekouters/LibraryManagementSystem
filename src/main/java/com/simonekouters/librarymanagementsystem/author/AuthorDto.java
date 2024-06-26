package com.simonekouters.librarymanagementsystem.author;

public record AuthorDto(String firstName, String lastName, Integer birthYear) {
    public static AuthorDto from(Author author) {
        return new AuthorDto(author.getFirstName(), author.getLastName(), author.getBirthYear());
    }
}
