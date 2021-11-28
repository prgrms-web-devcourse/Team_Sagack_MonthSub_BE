package com.prgrms.monthsub.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "정의되지 않은 서버 에러입니다."),

    //400
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력 방식입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "유효한 타입이 아닙니다"),

    //405
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않은 HTTP 메서드입니다."),

    //403
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "인증 권한을 보유하지 않습니다"),

    //404
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 엔티티를 찾을 수 없습니다.");

    private final HttpStatus status;

    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
