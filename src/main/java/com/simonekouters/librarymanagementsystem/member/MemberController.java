package com.simonekouters.librarymanagementsystem.member;

import com.simonekouters.librarymanagementsystem.member.registration.MemberRegistrationDto;
import com.simonekouters.librarymanagementsystem.member.registration.MemberRegistrationService;
import com.simonekouters.librarymanagementsystem.member.registration.MemberUpdateDto;
import com.simonekouters.librarymanagementsystem.transaction.Borrowing;
import com.simonekouters.librarymanagementsystem.transaction.BorrowingDto;
import com.simonekouters.librarymanagementsystem.transaction.Reservation;
import com.simonekouters.librarymanagementsystem.transaction.ReservationDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;
    private final MemberRegistrationService memberRegistrationService;

    @PostMapping
    public ResponseEntity<MemberDto> createNewMember(@Valid @RequestBody MemberRegistrationDto memberRegistrationDTO, UriComponentsBuilder ucb) {
        Member newMember = memberRegistrationService.createMember(memberRegistrationDTO);
        URI locationOfNewMember = ucb.path("books/{memberId}").buildAndExpand(newMember.getMemberId()).toUri();
        return ResponseEntity.created(locationOfNewMember).body(MemberDto.from(newMember));
    }

    @GetMapping("{memberId}/borrowed")
    public Iterable<BorrowingDto> borrowedBooks(@PathVariable Long memberId) {
        Set<Borrowing> borrowedBooks = memberService.getBorrowedBooksByMemberId(memberId);
        return borrowedBooks.stream()
                .map(BorrowingDto::from)
                .toList();
    }

    @GetMapping("{memberId}/reserved")
    public Iterable<ReservationDto> reservedBooks(@PathVariable Long memberId) {
        Set<Reservation> reservedBooks = memberService.getReservedBooksByMemberId(memberId);
        return reservedBooks.stream()
                .map(ReservationDto::from)
                .toList();
    }

    @PatchMapping("{memberId}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long memberId, @Valid @RequestBody MemberUpdateDto changedMember) {
        var existingMember = memberService.findById(memberId);
        if (existingMember.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Member updatedMember = memberService.updateExistingMember(existingMember.get(), changedMember);
        return ResponseEntity.ok().body(MemberDto.from(updatedMember));
    }

    @DeleteMapping("{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        Optional<Member> optionalMember = memberService.findById(memberId);
        if (optionalMember.isPresent()) {
            memberService.delete(optionalMember.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
