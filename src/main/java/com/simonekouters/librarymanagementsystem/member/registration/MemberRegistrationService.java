package com.simonekouters.librarymanagementsystem.member.registration;

import com.simonekouters.librarymanagementsystem.exceptions.BadInputException;
import com.simonekouters.librarymanagementsystem.member.Member;
import com.simonekouters.librarymanagementsystem.member.MemberRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class MemberRegistrationService {
    private final MemberRepository memberRepository;

    public boolean isEmailUnique(String email) {
        return memberRepository.findByEmail(email) == null;
    }

    public Member createMember(@Valid MemberRegistrationDto memberRegistrationDto) {
        if (!isEmailUnique(memberRegistrationDto.getEmail())) {
            throw new BadInputException("Email address already exists");
        }
        Member newMember = new Member(memberRegistrationDto.getFirstName(), memberRegistrationDto.getLastName(), memberRegistrationDto.getPassword(), memberRegistrationDto.getEmail());
        memberRepository.save(newMember);
        return newMember;
    }
}