package com.simonekouters.librarymanagementsystem.member;

import com.simonekouters.librarymanagementsystem.book.Book;
import com.simonekouters.librarymanagementsystem.member.registration.MemberUpdateDto;
import jakarta.transaction.Transactional;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Transactional
    public Member updateExistingMember(Member originalMember, MemberUpdateDto changedMember) {
        if (changedMember.getEmail() != null) {
            originalMember.setEmail(changedMember.getEmail());
        }
        if (changedMember.getFirstName() != null) {
            originalMember.setFirstName(changedMember.getFirstName());
        }
        if (changedMember.getLastName() != null) {
            originalMember.setLastName(changedMember.getLastName());
        }
        if (changedMember.getPassword() != null) {
            originalMember.setPassword(changedMember.getPassword());
        }
        return memberRepository.save(originalMember);
    }


    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }


    public Set<Book> getBorrowedBooksByMemberId(Long memberId) {
        return memberRepository.findBorrowedBooksByMemberId(memberId);
    }

    public Set<Book> getReservedBooksByMemberId(Long memberId) {
        return memberRepository.findReservedBooksByMemberId(memberId);
    }

    public void delete(Member member) {
        member.setHasBeenDeleted(true);
        save(member);
    }

}
