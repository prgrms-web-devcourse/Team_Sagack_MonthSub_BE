package com.prgrms.monthsub.common.error.exception.global;

import com.prgrms.monthsub.common.error.ErrorCodes;

public class InvalidInputException extends BusinessException {

    public InvalidInputException(String message, ErrorCodes errorCode) {
        super(message, errorCode);
    }

    public InvalidInputException(ErrorCodes errorCode) {
        super(errorCode);
    }

    public InvalidInputException() {super(ErrorCodes.INVALID_INPUT_VALUE());}

}
