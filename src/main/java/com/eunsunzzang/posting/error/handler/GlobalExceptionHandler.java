package com.eunsunzzang.posting.error.handler;

import com.eunsunzzang.posting.error.exception.AuthException;
import com.eunsunzzang.posting.error.response.GlobalErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthException.class)
    protected ResponseEntity<GlobalErrorResponse> handleAuthExceptionHandler (AuthException exception) {
        return ResponseEntity
                .status(exception.getErrorCode().getHttpStatus())
                .body(GlobalErrorResponse.from(exception.getErrorCode()));
    }

}