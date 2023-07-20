package com.eunsunzzang.posting.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    EMAIL_DUPLICATE(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일 입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;

}
