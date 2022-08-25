package com.prgrms.monthsub.module.payment.bill.app.provider;

import com.prgrms.monthsub.module.payment.bill.domain.Payment;
import java.util.List;
import java.util.Optional;

public interface PaymentProvider {

  Optional<Payment> find(
    Long userId,
    Long seriesId
  );

  void deleteBySeriesId(
    Long seriesId
  );

  void deleteAllHistoryBySeriesId(
    Long seriesId
  );

  List<Payment> findAllMySubscribeByUserId(Long userId);

}
