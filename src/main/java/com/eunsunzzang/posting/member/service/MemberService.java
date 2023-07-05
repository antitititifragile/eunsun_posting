package com.eunsunzzang.posting.member.service;

import com.eunsunzzang.posting.member.dto.MemberSignUpDto;

public interface MemberService {
    /*
    *회원가입
     */
    void signUp(MemberSignUpDto memberSignUpDto) throws Exception;
}
