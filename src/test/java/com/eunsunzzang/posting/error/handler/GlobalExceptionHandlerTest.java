package com.eunsunzzang.posting.error.handler;

import com.eunsunzzang.posting.error.errorcode.AuthErrorCode;
import com.eunsunzzang.posting.error.exception.AuthException;
import com.eunsunzzang.posting.member.Member;
import com.eunsunzzang.posting.member.dto.MemberSignUpDto;
import com.eunsunzzang.posting.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class GlobalExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    private static String SIGN_UP_URL = "/v1/members/signup";

    private ResultActions signUp(String signUpData) throws Exception {
        return mockMvc.perform(
                        post(SIGN_UP_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(signUpData));
    }

    @Test
    @DisplayName("이메일 중복 예외처리")
    public void signUp_Exception_EMAIL_DUPLICATE() throws Exception {
        // given
        String signUpData1 = objectMapper.writeValueAsString(new MemberSignUpDto("user1","user@email.com","1234"));
        String signUpData2 = objectMapper.writeValueAsString(new MemberSignUpDto("user2","user@email.com","5678"));

        // when
        signUp(signUpData1);
        ResultActions resultActions = signUp(signUpData2);

        // then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AuthException))
                .andExpect(jsonPath("message").value(AuthErrorCode.EMAIL_DUPLICATE.getErrorMessage()));
    }
}