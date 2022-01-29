package com.prgrms.monthsub.module.part.user.domain.exception;

import com.prgrms.monthsub.common.exception.base.BusinessException;
import com.prgrms.monthsub.common.exception.base.InvalidInputException;
import com.prgrms.monthsub.common.exception.model.ErrorCodes;
import java.util.Arrays;

public class UserException {

  public static class UserNotFound extends BusinessException {
    public UserNotFound(String... message) {
      super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
    }
  }

  public static class EmailDuplicated extends InvalidInputException {
    public EmailDuplicated(String... message) {
      super(ErrorCodes.DUPLICATED_EMAIL_VALUE(Arrays.stream(message).toList().toString()));
    }
  }

  public static class NickNameDuplicated extends InvalidInputException {
    public NickNameDuplicated(String... message) {
      super(ErrorCodes.DUPLICATED_NICKNAME_VALUE(Arrays.stream(message).toList().toString()));
    }
  }

  public static class NoPoint extends Exception {
    public NoPoint(String... message) {
      super(String.valueOf(ErrorCodes.NO_POINT(Arrays.stream(message).toList().toString())));
    }
  }

}
