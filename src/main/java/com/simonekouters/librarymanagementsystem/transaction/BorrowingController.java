package com.simonekouters.librarymanagementsystem.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("borrowings")
public class BorrowingController {
    private final BorrowingService borrowingService;

    @PostMapping
    public ResponseEntity<?> borrowBook(@PathVariable Long memberId, @PathVariable String isbn, UriComponentsBuilder ucb) {
        var borrowing = borrowingService.borrowBook(memberId, isbn);
        URI locationOfNewBorrowing = ucb.path("borrowings/{id}").buildAndExpand(borrowing.getId()).toUri();
        return ResponseEntity.created(locationOfNewBorrowing).body(BorrowingDto.from(borrowing));
    }

    // patchmapping for returning (member, book) -> set returndate to localborrowdate


    // getmapping list of all transactions on a certain date? (pageable)

}
