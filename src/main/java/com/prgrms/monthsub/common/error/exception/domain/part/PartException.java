package com.prgrms.monthsub.common.error.exception.domain.part;

import com.prgrms.monthsub.common.error.ErrorCodes;
import com.prgrms.monthsub.common.error.exception.global.BusinessException;
import java.util.Arrays;

public class PartException {

    public static class PartNotFound extends BusinessException {

        public PartNotFound(String... message) {
            super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
        }

    }


}
