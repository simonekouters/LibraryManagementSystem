package com.simonekouters.librarymanagementsystem.member;

import com.simonekouters.librarymanagementsystem.book.Book;
import com.simonekouters.librarymanagementsystem.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
    Set<Book> findBorrowedBooksByMemberId(Long memberId);

    Set<Book> findReservedBooksByMemberId(Long memberId);
}
