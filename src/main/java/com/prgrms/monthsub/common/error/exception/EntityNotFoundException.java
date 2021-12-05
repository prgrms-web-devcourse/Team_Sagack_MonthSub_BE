package com.prgrms.monthsub.common.error.exception;

import com.prgrms.monthsub.common.error.ErrorCodes;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String message, ErrorCodes errorCode) {super(message, errorCode);}

    public EntityNotFoundException(ErrorCodes errorCode) {
        super(errorCode);
    }

    public EntityNotFoundException(Class<?> domainClass, String message) {
        super(ErrorCodes.ENTITY_NOT_FOUND("domainClass=" + domainClass.getSimpleName() + "," + message));
    }

}
