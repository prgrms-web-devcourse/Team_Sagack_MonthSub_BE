package com.prgrms.monthsub.common.error.exception.domain.user;

import com.prgrms.monthsub.common.error.ErrorCodes;
import com.prgrms.monthsub.common.error.exception.global.BusinessException;
import java.util.Arrays;

public class UserException {

    public static class UserNotFound extends BusinessException {

        public UserNotFound(String... message) {
            super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
        }

    }

    public static class UserNotExist extends BusinessException {

        public UserNotExist(String... message) {
            super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
        }

    }

}
