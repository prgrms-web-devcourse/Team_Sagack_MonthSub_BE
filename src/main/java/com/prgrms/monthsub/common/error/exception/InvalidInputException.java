package com.prgrms.monthsub.common.error.exception;

import com.prgrms.monthsub.common.error.ErrorCode;

public class InvalidInputException extends BusinessException {

    public InvalidInputException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public InvalidInputException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidInputException() {super(ErrorCode.INVALID_INPUT_VALUE);}

}