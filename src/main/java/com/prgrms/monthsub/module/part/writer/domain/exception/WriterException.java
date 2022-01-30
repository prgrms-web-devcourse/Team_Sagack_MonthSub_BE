package com.prgrms.monthsub.module.part.writer.domain.exception;

import com.prgrms.monthsub.common.exception.base.BusinessException;
import com.prgrms.monthsub.common.exception.model.ErrorCodes;
import java.util.Arrays;

public class WriterException {

  public static class WriterNotFound extends BusinessException {
    public WriterNotFound(String... message) {
      super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
    }
  }

  public static class WriterLikesNotFound extends BusinessException {
    public WriterLikesNotFound(String... message) {
      super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
    }
  }

}
