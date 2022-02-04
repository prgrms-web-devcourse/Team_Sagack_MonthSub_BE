package com.prgrms.monthsub.module.payment.bill.domain.exception;

import com.prgrms.monthsub.common.exception.base.BusinessException;
import com.prgrms.monthsub.common.exception.base.InvalidInputException;
import com.prgrms.monthsub.common.exception.model.ErrorCodes;
import java.util.Arrays;

public class PaymentException {

  public static class PaymentDuplicated extends InvalidInputException {
    public PaymentDuplicated(String... message) {
      super(ErrorCodes.DUPLICATED_PAYMENT(Arrays.stream(message).toList().toString()));
    }

    public static class PaymentNotAccepted extends Exception {
      public PaymentNotAccepted(String... message) {
        super(String.valueOf(ErrorCodes.NO_POINT(Arrays.stream(message).toList().toString())));
      }
    }
  }

  public static class FailedPayment extends BusinessException {
    public FailedPayment(String... message) {
      super(ErrorCodes.FAILED_PAYMENT(Arrays.stream(message).toList().toString()));
    }
  }

}
