package com.prgrms.monthsub.common.error.exception;

import com.prgrms.monthsub.common.error.ErrorCode;

public class UnAuthorizedException extends BusinessException {

    public UnAuthorizedException(String message, ErrorCode errorCode) {super(message, errorCode);}

    public UnAuthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnAuthorizedException() {super(ErrorCode.UN_AUTHORIZED);}

}
