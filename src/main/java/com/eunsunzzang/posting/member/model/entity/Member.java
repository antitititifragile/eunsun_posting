package com.eunsunzzang.posting.member.model.entity;

import com.eunsunzzang.posting.member.model.EmailAuthStatus;
import com.eunsunzzang.posting.member.model.MemberRole;
import com.eunsunzzang.posting.member.model.MemberStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long member_id;

    @Column(nullable = false, length=30)
    private String name;

    @Column(nullable = false, length=30, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    //@Column(nullable = false, length=30)
    private String emailAuthKey;

    //@Column(nullable = false, length=30)
    LocalDateTime regDt;

   //@Column(nullable = false, length=30)
    LocalDateTime emailAuthDt;

    @Enumerated(EnumType.STRING)
    EmailAuthStatus emailAuthStatus;

    @Enumerated(EnumType.STRING)
    MemberRole role;

    @Enumerated(EnumType.STRING)
    MemberStatus status;

    @Column
    private String refreshToken;
}
