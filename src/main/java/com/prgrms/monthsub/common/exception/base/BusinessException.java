package com.prgrms.monthsub.common.exception.base;

import com.prgrms.monthsub.common.exception.model.ErrorCodes;

public class BusinessException extends RuntimeException {

  private final ErrorCodes errorCode;

  public BusinessException(
    String message,
    ErrorCodes errorCode
  ) {
    super(message);
    this.errorCode = errorCode;
  }

  public BusinessException(ErrorCodes errorCode) {
    super(errorCode.message());
    this.errorCode = errorCode;
  }

  public ErrorCodes getErrorCode() {
    return errorCode;
  }

}
