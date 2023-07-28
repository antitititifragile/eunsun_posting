package com.eunsunzzang.posting.member.service;

import com.eunsunzzang.posting.member.model.dto.EmailAuthDto;
import com.eunsunzzang.posting.member.model.dto.LoginDto;
import com.eunsunzzang.posting.member.model.dto.MemberSignUpDto;

public interface MemberService {

    /**
     * 회원가입
     * */
    void signUp(MemberSignUpDto memberSignUpDto) throws Exception;

    /**
     * 이메일 인증
     * */
    void emailAuth(EmailAuthDto emailAuthDto) throws Exception;

    /**
     * 로그인
     */
    void login(LoginDto loginDto) throws Exception;
}
