package com.eunsunzzang.posting.member.model.dto;

import com.eunsunzzang.posting.member.model.entity.Member;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;


public record MemberSignUpDto(


        // 향후 valid 추가
        @Size(min=2, max=20)
        String name,
        @Size(min=3)
        String email,
        String password) {
}
