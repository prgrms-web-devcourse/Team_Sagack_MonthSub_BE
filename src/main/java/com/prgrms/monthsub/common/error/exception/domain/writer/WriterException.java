package com.prgrms.monthsub.common.error.exception.domain.writer;

import com.prgrms.monthsub.common.error.ErrorCodes;
import com.prgrms.monthsub.common.error.exception.global.BusinessException;
import java.util.Arrays;

public class WriterException {

    public static class WriterNotFound extends BusinessException {

        public WriterNotFound(String... message) {
            super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
        }

    }

    public static class WriterNotExist extends BusinessException {

        public WriterNotExist(String... message) {
            super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
        }

    }

}
