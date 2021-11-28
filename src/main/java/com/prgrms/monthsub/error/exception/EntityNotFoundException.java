package com.prgrms.monthsub.error.exception;

import com.prgrms.monthsub.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String message, ErrorCode errorCode) {super(message, errorCode);}

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public EntityNotFoundException() {super(ErrorCode.ENTITY_NOT_FOUND);}

}
