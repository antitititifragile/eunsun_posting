package com.eunsunzzang.posting.member.service;

import com.eunsunzzang.posting.member.Member;
import com.eunsunzzang.posting.member.MemberRole;
import com.eunsunzzang.posting.member.dto.MemberSignUpDto;
import com.eunsunzzang.posting.member.repository.MemberRepository;
import com.eunsunzzang.posting.member.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    private MemberSignUpDto makeMemberSignUpDto() {
        return new MemberSignUpDto("username","user@email.com","password");
    }

    private void clear() {
        em.flush();
        em.clear();
    }

    /**
     * 회원가입
     * 회원가입 시 이메일, 비밀번호, 이름 입력하지 않으면 오류
     * 이미 존재하는 이메일일 경우 오류
     * 회원가입 후 ROLE은 ROLE_USER
     */

    @Test
    @DisplayName("회원가입 성공")
    public void signUp_success() throws Exception {
        //given
        MemberSignUpDto memberSignUpDto = makeMemberSignUpDto();

        //when
        memberService.signUp(memberSignUpDto);
        clear();

        //then
        Member member = memberRepository.findByEmail(memberSignUpDto.email()).orElseThrow(
                ()-> new Exception("회원이 없습니다.") );
        assertThat(member.getMember_id()).isNotNull();
        assertThat(member.getName()).isEqualTo(memberSignUpDto.name());
        assertThat(member.getEmail()).isEqualTo(memberSignUpDto.email());
        assertThat(member.getRole()).isSameAs(MemberRole.ROLE_USER);
    }

    @Test
    @DisplayName("이메일 중복은 회원가입할 수 없음")
    public void signUp_failed() throws Exception {
        //given
        MemberSignUpDto memberSignUpDto = makeMemberSignUpDto();
        memberService.signUp(memberSignUpDto);
        clear();

        //when, then
        assertThat(assertThrows(Exception.class,
                () -> memberService.signUp(memberSignUpDto)).getMessage()).isEqualTo("이미 존재하는 이메일입니다.");
    }

}