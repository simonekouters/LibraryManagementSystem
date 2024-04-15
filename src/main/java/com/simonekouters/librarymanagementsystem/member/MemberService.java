package com.simonekouters.librarymanagementsystem.member;

import com.simonekouters.librarymanagementsystem.exceptions.NotFoundException;
import com.simonekouters.librarymanagementsystem.member.registration.MemberUpdateDto;
import com.simonekouters.librarymanagementsystem.transaction.Borrowing;
import com.simonekouters.librarymanagementsystem.transaction.Reservation;
import com.simonekouters.librarymanagementsystem.transaction.ReservationStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member save(Member member) {
        return memberRepository.save(member);
    }

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


    public Set<Borrowing> getBorrowedBooksByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        return member.getBorrowedBooks().stream()
                // only show borrowed books that haven't been returned yet
                .filter(borrowing -> borrowing.getReturnDate() == null)
                .collect(Collectors.toSet());
    }

    public Set<Reservation> getReservedBooksByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        return member.getReservedBooks().stream()
                // only show reserved books with status pending
                .filter(reservation -> reservation.getStatus() == ReservationStatus.PENDING)
                .collect(Collectors.toSet());
    }

    public void delete(Member member) {
        member.setHasBeenDeleted(true);
        save(member);
    }

}
