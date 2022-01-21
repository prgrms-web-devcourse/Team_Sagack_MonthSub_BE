package com.prgrms.monthsub.common.exception.global;

import com.prgrms.monthsub.common.exception.base.InvalidInputException;
import com.prgrms.monthsub.common.exception.model.ErrorCodes;

public class FSMException {
  public static class InvalidEvent extends InvalidInputException {
    public InvalidEvent(String... message) {
      super(ErrorCodes.INVALID_EVENT());
    }
  }
}
