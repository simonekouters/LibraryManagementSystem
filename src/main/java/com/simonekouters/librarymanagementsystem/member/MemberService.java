package com.simonekouters.librarymanagementsystem.member;

import com.simonekouters.librarymanagementsystem.book.Book;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    public Set<Book> getBorrowedBooksByMemberId(Long memberId) {
        return memberRepository.findBorrowedBooksByMemberId(memberId);
    }

    public Set<Book> getReservedBooksByMemberId(Long memberId) {
        return memberRepository.findReservedBooksByMemberId(memberId);
    }
}
