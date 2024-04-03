package com.simonekouters.librarymanagementsystem.member;

import com.simonekouters.librarymanagementsystem.book.Book;
import com.simonekouters.librarymanagementsystem.book.BookResponseDto;
import com.simonekouters.librarymanagementsystem.member.registration.MemberRegistrationDto;
import com.simonekouters.librarymanagementsystem.member.registration.MemberRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.beans.PropertyDescriptor;
import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("members")
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
    public Iterable<BookResponseDto> borrowedBooks(@PathVariable Long memberId) {
        Set<Book> borrowedBooks = memberService.getBorrowedBooksByMemberId(memberId);
        return borrowedBooks.stream()
                .map(BookResponseDto::from)
                .toList();
    }

    @GetMapping("{memberId}/reserved")
    public Iterable<BookResponseDto> reservedBooks(@PathVariable Long memberId) {
        Set<Book> borrowedBooks = memberService.getReservedBooksByMemberId(memberId);
        return borrowedBooks.stream()
                .map(BookResponseDto::from)
                .toList();
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long memberId, @Valid @RequestBody MemberRegistrationDto memberDto) {
        var existingMember = memberService.findById(memberId);
        if (existingMember.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        BeanUtils.copyProperties(memberDto, existingMember, getNullPropertyNames(memberDto));

        Member updatedMember = memberService.save(existingMember.get());
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

    // Utility method to get null property names of an object
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        Set<String> propertyNames = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : src.getPropertyDescriptors()) {
            if (src.getPropertyValue(propertyDescriptor.getName()) == null) {
                propertyNames.add(propertyDescriptor.getName());
            }
        }
        return propertyNames.toArray(new String[propertyNames.size()]);
    }
}
