package com.simonekouters.librarymanagementsystem.member;

import com.simonekouters.librarymanagementsystem.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
