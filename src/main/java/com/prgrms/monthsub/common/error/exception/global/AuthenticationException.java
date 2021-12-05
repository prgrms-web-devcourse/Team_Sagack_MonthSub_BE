package com.prgrms.monthsub.common.error.exception.global;

import com.prgrms.monthsub.common.error.ErrorCodes;
import java.util.Arrays;

public class AuthenticationException {

    public static class UserNotExist extends BusinessException {

        public UserNotExist(String... message) {
            super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
        }

    }

}
