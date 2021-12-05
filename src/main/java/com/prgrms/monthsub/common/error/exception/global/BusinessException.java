package com.prgrms.monthsub.common.error.exception.global;

import com.prgrms.monthsub.common.error.ErrorCodes;

public class BusinessException extends RuntimeException {

    private final ErrorCodes errorCode;

    public BusinessException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCodes errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }

}
