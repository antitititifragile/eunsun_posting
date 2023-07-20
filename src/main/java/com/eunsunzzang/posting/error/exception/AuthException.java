package com.eunsunzzang.posting.error.exception;

import com.eunsunzzang.posting.error.errorcode.AuthErrorCode;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException{
    private final AuthErrorCode errorCode;

    public AuthException(AuthErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
