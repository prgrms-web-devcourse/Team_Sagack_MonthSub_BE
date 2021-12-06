package com.prgrms.monthsub.common.exception.global;

import com.prgrms.monthsub.common.exception.base.BusinessException;
import com.prgrms.monthsub.common.exception.model.ErrorCodes;
import java.util.Arrays;

public class AuthenticationException {

    public static class UserNotExist extends BusinessException {

        public UserNotExist(String... message) {
            super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
        }

    }

    public static class UnAuthorize extends BusinessException {

        public UnAuthorize(String... message) {
            super(ErrorCodes.UN_AUTHORIZED());
        }

    }

}
