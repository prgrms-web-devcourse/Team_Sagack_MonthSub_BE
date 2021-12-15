package com.prgrms.monthsub.module.payment.domain.exception;

import com.prgrms.monthsub.common.exception.base.InvalidInputException;
import com.prgrms.monthsub.common.exception.model.ErrorCodes;
import java.util.Arrays;

public class PaymentException {

  public static class PaymentDuplicated extends InvalidInputException {
    public PaymentDuplicated(String... message) {
      super(ErrorCodes.DUPLICATED_PAYMENT(Arrays.stream(message)
        .toList()
        .toString()));
    }
  }

}
