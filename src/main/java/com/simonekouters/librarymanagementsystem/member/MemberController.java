package com.simonekouters.librarymanagementsystem.member;

import com.simonekouters.librarymanagementsystem.member.registration.MemberRegistrationDto;
import com.simonekouters.librarymanagementsystem.member.registration.MemberRegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("members")
public class MemberController {

    private final MemberService memberService;
    private final MemberRegistrationService memberRegistrationService;

    public MemberController(MemberService memberService, MemberRegistrationService memberRegistrationService) {
        this.memberService = memberService;
        this.memberRegistrationService = memberRegistrationService;
    }

    @PostMapping
    public ResponseEntity<MemberDto> createNewMember(@Valid @RequestBody MemberRegistrationDto memberRegistrationDTO, UriComponentsBuilder ucb) {
        Member newMember = memberRegistrationService.createMember(memberRegistrationDTO);
        URI locationOfNewMember = ucb.path("books/{memberId}").buildAndExpand(newMember.getMemberId()).toUri();
        return ResponseEntity.created(locationOfNewMember).body(MemberDto.from(newMember));
    }
}
