package com.eunsunzzang.posting.member.controller;

import com.eunsunzzang.posting.error.errorcode.AuthErrorCode;
import com.eunsunzzang.posting.error.exception.AuthException;
import com.eunsunzzang.posting.member.model.dto.LoginDto;
import com.eunsunzzang.posting.member.model.entity.Member;
import com.eunsunzzang.posting.member.model.dto.EmailAuthDto;
import com.eunsunzzang.posting.member.model.dto.MemberSignUpDto;
import com.eunsunzzang.posting.member.repository.MemberRepository;
import com.eunsunzzang.posting.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    EntityManager em;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    PasswordEncoder passwordEncoder;

    private static String SIGN_UP_URL = "/v1/members/signup";
    private static String EMAIL_AUTH_URL = "/v1/members/email-auth";
    private static String LOGIN_URL = "/v1/members/login";

    private String name = "username";
    private String email = "user@email.com";
    private String password = "1234";

    private void clear() {
        em.flush();
        em.clear();
    }

    private void signUp(String signUpData) throws Exception {
        mockMvc.perform(post(SIGN_UP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpData));
    }

    private ResultActions emailAuth(String emailAuthData) throws Exception {
        return mockMvc.perform(post(EMAIL_AUTH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emailAuthData));
    }

    private ResultActions login(String loginData) throws Exception {
        return mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginData));

    }

    @Test
    @DisplayName("회원가입 성공")
    public void signUp_success() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(name,email,password));

        //when
        signUp(signUpData);

        //then
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new AuthException(AuthErrorCode.EMAIL_NOT_FOUND));
        Assertions.assertThat(member.getName()).isEqualTo(name);
        Assertions.assertThat(memberRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("이메일 인증 성공")
    public void emailAuth_success() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(name,email,password));
        signUp(signUpData);
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new AuthException(AuthErrorCode.EMAIL_NOT_FOUND));
        String key = member.getEmailAuthKey();
        String emailAuthData = objectMapper.writeValueAsString(new EmailAuthDto(email,key));

        //when
        ResultActions resultActions = emailAuth(emailAuthData);

        //then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 성공")
    public void login_success() throws Exception {
        //given
        emailAuth_success();
        String loginData = objectMapper.writeValueAsString(new LoginDto(email, password));

        //when
        ResultActions resultActions = login(loginData);

        //then
        resultActions
                .andExpect(status().isOk());
    }

}