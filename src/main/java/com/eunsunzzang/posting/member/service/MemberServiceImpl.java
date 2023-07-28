package com.eunsunzzang.posting.member.service;

import com.eunsunzzang.posting.error.errorcode.AuthErrorCode;
import com.eunsunzzang.posting.error.exception.AuthException;
import com.eunsunzzang.posting.member.model.EmailAuthStatus;
import com.eunsunzzang.posting.member.model.entity.Member;
import com.eunsunzzang.posting.member.model.MemberRole;
import com.eunsunzzang.posting.member.model.dto.EmailAuthDto;
import com.eunsunzzang.posting.member.model.dto.LoginDto;
import com.eunsunzzang.posting.member.model.dto.MemberSignUpDto;
import com.eunsunzzang.posting.member.repository.MemberRepository;
import com.eunsunzzang.posting.member.security.JwtProvider;
import com.eunsunzzang.posting.member.security.Token;
import com.eunsunzzang.posting.member.security.TokenDto;
import com.eunsunzzang.posting.member.security.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

import static com.eunsunzzang.posting.error.errorcode.AuthErrorCode.MEMBER_NOT_FOUND;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final JwtProvider jwtProvider;

    /**
     * 회원가입
     * */
    @Override
    public void signUp(MemberSignUpDto memberSignUpDto) throws Exception {
        if(memberRepository.findByEmail(memberSignUpDto.email()).isPresent()){
            throw new AuthException(AuthErrorCode.EMAIL_DUPLICATE);
        }

        Member member = Member.builder()
                .name(memberSignUpDto.name())
                .email(memberSignUpDto.email())
                .password(passwordEncoder.encode(memberSignUpDto.password()))
                .role(MemberRole.ROLE_USER)
                .emailAuthStatus(EmailAuthStatus.VERIFICATION_ING)
                .emailAuthKey(UUID.randomUUID().toString())
                .build();

        emailSend(member);
        memberRepository.save(member);
    }

    /**
     * 이메일 인증코드 전송
     */
    public void emailSend(Member member) throws Exception {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("\n인증번호는 " + member.getEmailAuthKey() + "입니다");
        message.setSubject("이메일 인증");
        message.setTo(member.getEmail());

        mailSender.send(message);
    }

    /**
     * 이메일 인증
     */
    @Override
    @Async
    public void emailAuth(EmailAuthDto emailAuthDto) throws Exception {
        Member member = memberRepository.findByEmail(emailAuthDto.email()).orElseThrow(
                () -> new AuthException(AuthErrorCode.EMAIL_NOT_FOUND));

        if (member.getEmailAuthKey().equals(emailAuthDto.key())) {
            member.setEmailAuthStatus(EmailAuthStatus.VERIFICATION_COMPLETE);
            memberRepository.save(member);
        } else throw new AuthException(AuthErrorCode.AUTH_CODE_ERROR);
    }

    /**
     * 로그인
     */
    @Override
    public void login(LoginDto loginDto) throws Exception {
        Member member = memberRepository.findByEmail(loginDto.email()).orElseThrow(() -> new AuthException(MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(loginDto.password(), member.getPassword())) {
            throw new AuthException(AuthErrorCode.PASSWORD_NOT_CORRECT);
        }

        member.setRefreshToken(createRefreshToken(member));
        memberRepository.save(member);
    }

    /**
     * Refresh 토큰 생성
     */
    public String createRefreshToken(Member member) {
        Token token = tokenRepository.save(
                Token.builder()
                        .id(member.getEmail())
                        .refresh_token(UUID.randomUUID().toString())
                        .expiration(120)
                        .build()
        );
        return token.getRefresh_token();
    }

    public TokenDto refreshAccessToken(TokenDto token) throws Exception {
        String email = jwtProvider.getEmail(token.getAccess_token());
        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new AuthException(AuthErrorCode.EMAIL_NOT_FOUND));
        Token refreshToken = validRefreshToken(member, token.getRefresh_token());

        if (refreshToken != null) {
            return TokenDto.builder()
                    .access_token(jwtProvider.createToken(email, Collections.singletonList(member.getRole())))
                    .refresh_token(refreshToken.getRefresh_token())
                    .build();
        } else {
            throw new Exception("로그인을 해주세요");
        }
    }

    public Token validRefreshToken(Member member, String refreshToken) throws Exception {
        Token token = tokenRepository.findById(member.getMember_id()).orElseThrow(() -> new Exception("만료된 계정입니다. 로그인을 다시 시도하세요"));
        // 해당유저의 Refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
        if (token.getRefresh_token() == null) {
            return null;
        } else {
            // 리프레시 토큰 만료일자가 얼마 남지 않았을 때 만료시간 연장
            if(token.getExpiration() < 10) {
                token.setExpiration(1000);
                tokenRepository.save(token);
            }

            // 토큰이 같은지 비교
            if(!token.getRefresh_token().equals(refreshToken)) {
                return null;
            } else {
                return token;
            }
        }
    }

}
