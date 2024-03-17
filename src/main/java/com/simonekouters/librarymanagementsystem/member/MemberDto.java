package com.simonekouters.librarymanagementsystem.member;

public record MemberDto(Long id, String name) {
    public static MemberDto from(Member member) {
        return new MemberDto(member.getId(), member.getName());
    }
}
