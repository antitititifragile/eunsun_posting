package com.eunsunzzang.posting.error.errorcode;

import org.springframework.http.HttpStatus;
public interface ErrorCode {
    HttpStatus getHttpStatus();
    String getErrorMessage();

}
