package com.prgrms.monthsub.common.error.exception.user;

import com.prgrms.monthsub.common.error.ErrorCodes;
import com.prgrms.monthsub.common.error.exception.BusinessException;

public class UserException {

    public static class UserNotFound extends BusinessException {

        public UserNotFound(String message, ErrorCodes errorCode) {super(message, errorCode);}

        public UserNotFound(ErrorCodes errorCode) {
            super(errorCode);
        }

        public UserNotFound(String message) {super(ErrorCodes.USER_NOT_FOUND(message));}

    }

}
