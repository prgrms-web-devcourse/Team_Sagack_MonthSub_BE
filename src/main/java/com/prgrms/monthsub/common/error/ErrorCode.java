package com.prgrms.monthsub.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //500
    INTERNAL_SERVER_ERROR("S001", HttpStatus.INTERNAL_SERVER_ERROR, "정의되지 않은 서버 에러입니다."),

    //400
    INVALID_INPUT_VALUE("U001", HttpStatus.BAD_REQUEST, "잘못된 입력 방식입니다."),
    INVALID_TYPE_VALUE("U002", HttpStatus.BAD_REQUEST, "유효한 타입이 아닙니다"),
    INVALID_CREDENTIALS_VALUE("A003", HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 일치하지 않습니다."),

    //405
    METHOD_NOT_ALLOWED("M001", HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않은 HTTP 메서드입니다."),

    //403
    HANDLE_ACCESS_DENIED("A001", HttpStatus.FORBIDDEN, "인증 권한을 보유하지 않습니다"),

    //404
    ENTITY_NOT_FOUND("U003", HttpStatus.NOT_FOUND, "해당 엔티티를 찾을 수 없습니다."),
    USER_NOT_FOUND("A002", HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.");

    private final HttpStatus status;

    private final String message;

    private final String code;

    ErrorCode(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
