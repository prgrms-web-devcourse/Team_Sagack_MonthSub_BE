package com.prgrms.monthsub.common.error.exception.domain.series;

import com.prgrms.monthsub.common.error.ErrorCodes;
import com.prgrms.monthsub.common.error.exception.global.BusinessException;
import java.util.Arrays;

public class SeriesException {

    public static class SeriesNotFound extends BusinessException {

        public SeriesNotFound(String... message) {
            super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
        }

    }

}
