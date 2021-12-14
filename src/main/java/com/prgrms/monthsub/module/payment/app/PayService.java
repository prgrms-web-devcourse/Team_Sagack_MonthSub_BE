package com.prgrms.monthsub.module.payment.app;

import com.prgrms.monthsub.module.payment.dto.PaymentPost;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class PayService {
  private final PaymentService paymentService;

  public PayService(PaymentService paymentService) {this.paymentService = paymentService;}

  public PaymentPost.Response pay(
    Long id,
    Long userId
  ) {
    try {
      return paymentService.createPayment(id, userId);
    } catch (ObjectOptimisticLockingFailureException e) {
      return paymentService.createPayment(id, userId);
    }
  }
}
