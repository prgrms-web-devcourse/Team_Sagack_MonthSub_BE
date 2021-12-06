package com.prgrms.monthsub.series.series.domain.exception;

import com.prgrms.monthsub.common.exception.model.ErrorCodes;
import com.prgrms.monthsub.common.exception.base.BusinessException;
import java.util.Arrays;

public class SeriesException {

    public static class SeriesNotFound extends BusinessException {

        public SeriesNotFound(String... message) {
            super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
        }

    }

}
