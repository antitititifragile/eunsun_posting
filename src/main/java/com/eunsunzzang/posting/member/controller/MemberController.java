package com.eunsunzzang.posting.member.controller;

import com.eunsunzzang.posting.member.Member;
import com.eunsunzzang.posting.member.dto.EmailAuthDto;
import com.eunsunzzang.posting.member.dto.MemberSignUpDto;
import com.eunsunzzang.posting.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member API", description = "회원가입 및 로그인 관리")
@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     * */
    @Operation(summary = "회원가입", description = "name, email, password를 통해 회원가입을 진행합니다.")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public void signUp(@Valid @RequestBody MemberSignUpDto memberSignUpDto) throws Exception {
        memberService.signUp(memberSignUpDto);
    }

    /**
     * 이메일 인증
     * */
    @Operation(summary = "이메일 인증", description = "이메일로 전송된 코드를 통해 사용자의 이메일 인증을 진행합니다.")
    @PostMapping("/email-auth")
    @ResponseStatus(HttpStatus.OK)
    public void emailAuth(@RequestBody EmailAuthDto emailAuthDto) throws Exception{
        memberService.emailAuth(emailAuthDto);
    }
}
