package com.simonekouters.librarymanagementsystem.member;

public record MemberDto(Long memberId, String firstName, String lastName, String email) {
    public static MemberDto from(Member member) {
        return new MemberDto(member.getMemberId(), member.getFirstName(), member.getLastName(), member.getEmail());
    }
}
