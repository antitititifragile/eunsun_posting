package com.eunsunzzang.posting.member.dto;

import com.eunsunzzang.posting.member.Member;
import jakarta.validation.constraints.Size;

public record MemberSignUpDto(

        // 향후 valid 추가
        @Size(min=2, max=20)
        String name,

        @Size(min=3)
        String email,

        String password) {

    public Member toEntity() {
        return Member.builder().name(name).email(email).password(password).build();
    }
}
