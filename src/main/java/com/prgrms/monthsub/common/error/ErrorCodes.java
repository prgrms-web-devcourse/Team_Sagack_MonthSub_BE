package com.prgrms.monthsub.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public record ErrorCodes(ErrorCode errorCode, String message) {

    public static ErrorCodes INTERNAL_SERVER_ERROR() {
        return new ErrorCodes(ErrorCode.INTERNAL_SERVER_ERROR, "정의되지 않은 서버 에러입니다.");
    }

    public static ErrorCodes INVALID_INPUT_VALUE() {
        return new ErrorCodes(ErrorCode.INVALID_INPUT_VALUE, "잘못된 입력 방식입니다.");
    }

    public static ErrorCodes INVALID_TYPE_VALUE() {
        return new ErrorCodes(ErrorCode.INVALID_TYPE_VALUE, "유효한 타입이 아닙니다.");
    }

    public static ErrorCodes INVALID_CREDENTIALS_VALUE() {
        return new ErrorCodes(ErrorCode.INVALID_CREDENTIALS_VALUE, "아이디 혹은 비밀번호가 일치하지 않습니다.");
    }

    public static ErrorCodes INVALID_UPLOAD_FILE_TYPE() {
        return new ErrorCodes(ErrorCode.INVALID_UPLOAD_FILE_TYPE, "아이디 혹은 비밀번호가 일치하지 않습니다.");
    }

    public static ErrorCodes UN_AUTHORIZED() {
        return new ErrorCodes(ErrorCode.UN_AUTHORIZED, "인증 실패하였습니다.");
    }

    public static ErrorCodes METHOD_NOT_ALLOWED() {
        return new ErrorCodes(ErrorCode.METHOD_NOT_ALLOWED, "지원하지 않은 HTTP 메서드입니다.");
    }

    public static ErrorCodes HANDLE_ACCESS_DENIED() {
        return new ErrorCodes(ErrorCode.HANDLE_ACCESS_DENIED, "인증 권한을 보유하지 않습니다");
    }

    public static ErrorCodes ENTITY_NOT_FOUND(String message) {
        return new ErrorCodes(ErrorCode.ENTITY_NOT_FOUND, message);
    }

    public static ErrorCodes USER_NOT_FOUND(String message) {
        return new ErrorCodes(ErrorCode.USER_NOT_FOUND, message);
    }

    @Getter
    public enum ErrorCode {

        //500
        INTERNAL_SERVER_ERROR("S001", HttpStatus.INTERNAL_SERVER_ERROR),

        //400
        INVALID_INPUT_VALUE("U001", HttpStatus.BAD_REQUEST),
        INVALID_TYPE_VALUE("U002", HttpStatus.BAD_REQUEST),
        INVALID_CREDENTIALS_VALUE("A003", HttpStatus.BAD_REQUEST),
        INVALID_UPLOAD_FILE_TYPE("F001", HttpStatus.BAD_REQUEST),

        //401
        UN_AUTHORIZED("A004", HttpStatus.UNAUTHORIZED),

        //405
        METHOD_NOT_ALLOWED("M001", HttpStatus.METHOD_NOT_ALLOWED),

        //403
        HANDLE_ACCESS_DENIED("A001", HttpStatus.FORBIDDEN),

        //404
        ENTITY_NOT_FOUND("U003", HttpStatus.NOT_FOUND),
        USER_NOT_FOUND("A002", HttpStatus.NOT_FOUND);

        private final HttpStatus status;

        private final String code;

        ErrorCode(String code, HttpStatus status) {
            this.code = code;
            this.status = status;
        }
    }


}
