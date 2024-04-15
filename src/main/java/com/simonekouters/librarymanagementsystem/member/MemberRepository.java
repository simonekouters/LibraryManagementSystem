package com.simonekouters.librarymanagementsystem.member;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface MemberRepository extends CrudRepository<Member, Long> {
    Member findByEmail(String email);
}
