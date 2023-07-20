package com.eunsunzzang.posting.member.service;

import com.eunsunzzang.posting.error.errorcode.AuthErrorCode;
import com.eunsunzzang.posting.error.exception.AuthException;
import com.eunsunzzang.posting.member.EmailAuthStatus;
import com.eunsunzzang.posting.member.Member;
import com.eunsunzzang.posting.member.MemberRole;
import com.eunsunzzang.posting.member.dto.MemberSignUpDto;
import com.eunsunzzang.posting.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private int tmpNum = 123;

    private final JavaMailSender mailSender;

    /**
     * 회원가입
     * */
    @Override
    public void signUp(MemberSignUpDto memberSignUpDto) throws Exception {
        Member member = memberSignUpDto.toEntity();
        member.setRole(MemberRole.ROLE_USER);
        member.setEmailAuthStatus(EmailAuthStatus.VERIFICATION_ING);

        if(memberRepository.findByEmail(memberSignUpDto.email()).isPresent()){
            throw new AuthException(AuthErrorCode.EMAIL_DUPLICATE);
        }
        emailSend(member);
        memberRepository.save(member);
    }

    /**
     * 이메일 인증코드 전송
     * */
    public void emailSend(Member member) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("\n인증번호는 " + tmpNum + "입니다");
        message.setSubject("이메일 인증");
        message.setTo(member.getEmail());

        mailSender.send(message);
    }

    /**
     * 이메일 인증
     * */
    @Override
    @Async
    public void emailAuth(Member member) throws Exception {

    }
}
