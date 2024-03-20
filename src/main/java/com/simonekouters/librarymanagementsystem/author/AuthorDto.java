package com.simonekouters.librarymanagementsystem.author;

public record AuthorDto(Long id, String firstName, String lastName, Integer birthYear) {
    public static AuthorDto from(Author author) {
        return new AuthorDto(author.getId(), author.getFirstName(), author.getLastName(), author.getBirthYear());
    }
}
