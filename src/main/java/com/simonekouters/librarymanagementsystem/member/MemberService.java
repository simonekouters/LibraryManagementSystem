package com.simonekouters.librarymanagementsystem.member;

import com.simonekouters.librarymanagementsystem.book.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member save(Member member) {
        return memberRepository.save(member);
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
