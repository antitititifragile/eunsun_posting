package com.eunsunzzang.posting.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    EMAIL_DUPLICATE(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일 입니다."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일 입니다."),
    AUTH_CODE_ERROR(HttpStatus.BAD_REQUEST,"잘못된 인증 코드 입니다."),
    PASSWORD_NOT_CORRECT(HttpStatus.BAD_REQUEST, "잘못된 비밀번호 입니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 회원 입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;

}
