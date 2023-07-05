package com.eunsunzzang.posting.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long member_id;

    @Column(nullable = false, length=30)
    private String name;

    @Column(nullable = false, length=30, unique = true)
    private String email;

    @Column(nullable = false, length=30)
    private String password;

    //@Column(nullable = false, length=30)
    private String emailAuthKey;

    //@Column(nullable = false, length=30)
    LocalDateTime regDt;

   //@Column(nullable = false, length=30)
    LocalDateTime emailAuthDt;

    //@Enumerated(EnumType.STRING)
    EmailAuthStatus emailAuthStatus;

    @Enumerated(EnumType.STRING)
    MemberRole role;

    @Enumerated(EnumType.STRING)
    MemberStatus status;

    // 회원 가입 시 권한 부여
    public void addUserAuthority() {
        this.role = MemberRole.ROLE_USER;
    }
}
