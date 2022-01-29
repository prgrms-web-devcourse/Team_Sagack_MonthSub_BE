package com.prgrms.monthsub.common.exception.base;

import com.prgrms.monthsub.common.exception.model.ErrorCodes;

public class CheckedException extends Exception {

  private final ErrorCodes errorCode;

  public CheckedException(
    String message,
    ErrorCodes errorCode
  ) {
    super(message);
    this.errorCode = errorCode;
  }

  public CheckedException(ErrorCodes errorCode) {
    super(errorCode.message());
    this.errorCode = errorCode;
  }

  public ErrorCodes getErrorCode() {
    return errorCode;
  }

}
