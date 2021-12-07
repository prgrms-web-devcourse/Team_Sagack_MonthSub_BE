package com.prgrms.monthsub.module.part.user.domain.exception;

import com.prgrms.monthsub.common.exception.base.BusinessException;
import com.prgrms.monthsub.common.exception.model.ErrorCodes;
import java.util.Arrays;

public class PartException {

    public static class PartNotFound extends BusinessException {

        public PartNotFound(String... message) {
            super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
        }

    }


}
