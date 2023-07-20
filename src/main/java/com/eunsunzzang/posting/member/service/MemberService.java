package com.eunsunzzang.posting.member.service;

import com.eunsunzzang.posting.member.Member;
import com.eunsunzzang.posting.member.dto.MemberSignUpDto;

public interface MemberService {

    /**
     * 회원가입
     * */
    void signUp(MemberSignUpDto memberSignUpDto) throws Exception;

    /**
     * 이메일 인증
     * */
    void emailAuth(Member member) throws Exception;
}
