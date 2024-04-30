package com.simonekouters.librarymanagementsystem.member.registration;

import com.simonekouters.librarymanagementsystem.authority.Authority;
import com.simonekouters.librarymanagementsystem.authority.AuthorityRepository;
import com.simonekouters.librarymanagementsystem.exceptions.BadInputException;
import com.simonekouters.librarymanagementsystem.member.Member;
import com.simonekouters.librarymanagementsystem.member.MemberRepository;
import com.simonekouters.librarymanagementsystem.member.UserRole;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class MemberRegistrationService {
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isEmailUnique(String email) {
        return memberRepository.findByEmail(email) == null;
    }

    public Member createMember(@Valid MemberRegistrationDto memberRegistrationDto, UserRole role) {
        if (!isEmailUnique(memberRegistrationDto.getEmail())) {
            throw new BadInputException("Email address already exists");
        }
        Member newMember = new Member(memberRegistrationDto.getFirstName(), memberRegistrationDto.getLastName(), passwordEncoder.encode(memberRegistrationDto.getPassword()), memberRegistrationDto.getEmail());
        authorityRepository.save(new Authority(newMember.getMemberId(), role.toString()));
        return memberRepository.save(newMember);
    }
}