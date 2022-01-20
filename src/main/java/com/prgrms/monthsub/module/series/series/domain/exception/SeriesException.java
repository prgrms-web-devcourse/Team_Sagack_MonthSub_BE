package com.prgrms.monthsub.module.series.series.domain.exception;

import com.prgrms.monthsub.common.exception.base.BusinessException;
import com.prgrms.monthsub.common.exception.model.ErrorCodes;
import java.util.Arrays;

public class SeriesException {

  public static class SeriesNotFound extends BusinessException {
    public SeriesNotFound(String... message) {
      super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
    }
  }

  public static class SeriesLikesNotFound extends BusinessException {
    public SeriesLikesNotFound(String... message) {
      super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
    }
  }

  public static class SeriesCommentNotFound extends BusinessException {
    public SeriesCommentNotFound(String... message) {
      super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
    }
  }

}
