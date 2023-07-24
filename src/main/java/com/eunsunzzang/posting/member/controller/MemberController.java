package com.eunsunzzang.posting.member.controller;

import com.eunsunzzang.posting.member.Member;
import com.eunsunzzang.posting.member.dto.EmailAuthDto;
import com.eunsunzzang.posting.member.dto.MemberSignUpDto;
import com.eunsunzzang.posting.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     * */
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public void signUp(@Valid @RequestBody MemberSignUpDto memberSignUpDto) throws Exception {
        memberService.signUp(memberSignUpDto);
    }

    /**
     * 이메일 인증
     * */
    @PostMapping("/email-auth")
    @ResponseStatus(HttpStatus.OK)
    public void emailAuth(@RequestBody EmailAuthDto emailAuthDto) throws Exception{
        memberService.emailAuth(emailAuthDto);
    }
}
