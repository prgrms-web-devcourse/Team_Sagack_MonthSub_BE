package com.prgrms.monthsub.module.payment.app.provider;

import com.prgrms.monthsub.module.payment.domain.Payment;
import java.util.Optional;

public interface PaymentProvider {
  Optional<Payment> find(
    Long userId,
    Long seriesId
  );
}
