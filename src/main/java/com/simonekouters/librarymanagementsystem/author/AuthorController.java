package com.simonekouters.librarymanagementsystem.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public Page<AuthorResponseDto> getAll(Pageable pageable) {
        Pageable pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "lastName")
        );
        return authorService.findAll(pageRequest)
                .map(AuthorResponseDto::from);
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
