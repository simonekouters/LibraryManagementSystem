package com.simonekouters.librarymanagementsystem.transaction;

import com.simonekouters.librarymanagementsystem.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/borrowings")
public class BorrowingController {
    private final BorrowingService borrowingService;

    @PostMapping("{memberId}/{isbn}")
    public ResponseEntity<BorrowingDto> addBorrowing(@PathVariable Long memberId, @PathVariable String isbn, UriComponentsBuilder ucb) {
        var borrowing = borrowingService.borrowBook(memberId, isbn);
        URI locationOfNewBorrowing = ucb.path("borrowings/{id}").buildAndExpand(borrowing.getId()).toUri();
        return ResponseEntity.created(locationOfNewBorrowing).body(BorrowingDto.from(borrowing));
    }

    @PatchMapping("return/{id}")
    public ResponseEntity<String> returnBook(@PathVariable Long id) {
        var borrowing = borrowingService.findById(id).orElseThrow(NotFoundException::new);
        borrowingService.returnBook(borrowing);
        return ResponseEntity.ok("Book returned successfully");
    }
}
