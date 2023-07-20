package com.eunsunzzang.posting.error.response;

import com.eunsunzzang.posting.error.errorcode.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class GlobalErrorResponse {
    private final LocalDateTime timestamp;
    private final HttpStatus status;
    private final String message;

    public static GlobalErrorResponse from(ErrorCode errorCode) {
        return GlobalErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(errorCode.getHttpStatus())
                .message(errorCode.getErrorMessage())
                .build();

    }

//    // errors가 없다면 응답으로 내려가지 않도록
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    private final List<ValidationError> errors;
//
//    @Getter
//    @Builder
//    @RequiredArgsConstructor
//    // @Valid를 사용했을 때 에러가 발생한 경우 어느 필드에서 에러가 발생했는지 응답
//    public static class ValidationError {
//        private final String field;
//        private final String message;
//
//        public static ValidationError of(final FieldError fieldError) {
//            return ValidationError.builder()
//                    .field(fieldError.getField())
//                    .message(fieldError.getDefaultMessage())
//                    .build();
//        }
}
