package com.prgrms.monthsub.common.error.exception;

import com.prgrms.monthsub.common.error.ErrorCode;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(String message, ErrorCode errorCode) {super(message, errorCode);}

    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserNotFoundException() {super(ErrorCode.USER_NOT_FOUND);}

}
