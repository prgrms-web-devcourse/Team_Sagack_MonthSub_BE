package com.prgrms.monthsub.common.error.exception.global;

import com.prgrms.monthsub.common.error.ErrorCodes;

public class UnAuthorizedException extends BusinessException {

    public UnAuthorizedException(String message, ErrorCodes errorCode) {super(message, errorCode);}

    public UnAuthorizedException(ErrorCodes errorCode) {
        super(errorCode);
    }

    public UnAuthorizedException() {super(ErrorCodes.UN_AUTHORIZED());}

}
