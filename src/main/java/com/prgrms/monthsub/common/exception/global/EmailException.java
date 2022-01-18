package com.prgrms.monthsub.common.exception.global;

import com.prgrms.monthsub.common.exception.base.BusinessException;
import com.prgrms.monthsub.common.exception.model.ErrorCodes;

public class EmailException {

  public static class EmailSendFailException extends BusinessException {
    public EmailSendFailException(String message) {
      super(ErrorCodes.EMAIL_SEND_FAIL(message));
    }
  }

}
