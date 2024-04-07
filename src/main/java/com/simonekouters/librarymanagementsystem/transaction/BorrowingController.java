package com.simonekouters.librarymanagementsystem.transaction;

import com.simonekouters.librarymanagementsystem.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("borrowings")
public class BorrowingController {
    private final BorrowingService borrowingService;

    @PostMapping
    public ResponseEntity<BorrowingDto> addBorrowing(@PathVariable Long memberId, @PathVariable String isbn, UriComponentsBuilder ucb) {
        var borrowing = borrowingService.borrowBook(memberId, isbn);
        URI locationOfNewBorrowing = ucb.path("borrowings/{id}").buildAndExpand(borrowing.getId()).toUri();
        return ResponseEntity.created(locationOfNewBorrowing).body(BorrowingDto.from(borrowing));
    }

    @PatchMapping("{id}")
    public ResponseEntity<String> returnBook(@PathVariable Long id) {
        var borrowing = borrowingService.findById(id).orElseThrow(NotFoundException::new);
        borrowingService.returnBook(borrowing);
        return ResponseEntity.ok("Book returned successfully");
    }
}
