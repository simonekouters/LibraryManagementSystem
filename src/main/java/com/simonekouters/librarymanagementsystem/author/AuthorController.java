package com.simonekouters.librarymanagementsystem.author;

import com.simonekouters.librarymanagementsystem.book.Book;
import com.simonekouters.librarymanagementsystem.book.BookResponseDto;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public Page<AuthorResponseDto> getAll(Pageable pageable) {
        return authorService.findAll(pageable)
                .map(AuthorResponseDto::from);
    }

    @GetMapping("{id}/books")
    public ResponseEntity<Page<BookResponseDto>> getAllBooksByAuthorId(@PathVariable("id") Long id, Pageable pageable) {
        Optional<Author> optionalAuthor = authorService.findById(id);
        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();
            Page<BookResponseDto> bookResponsePage = authorService.getAllBooksByAuthorId(author, pageable);
            return ResponseEntity.ok(bookResponsePage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<AuthorDto> patch(@PathVariable Long id, @RequestBody AuthorDto changedAuthor) {
        var possibleOriginalAuthor = authorService.findById(id);
        if (possibleOriginalAuthor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var originalAuthor = possibleOriginalAuthor.get();
        Author updatedAuthor = authorService.updateAuthor(originalAuthor, changedAuthor);
        return ResponseEntity.ok(AuthorDto.from(updatedAuthor));
    }
}
