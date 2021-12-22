package com.prgrms.monthsub.module.payment.bill.app;

import com.prgrms.monthsub.module.payment.bill.domain.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

  Optional<Payment> findByUserIdAndSeriesId(
    Long userId,
    Long seriesId
  );

  List<Payment> findAllByUserId(Long userId);

}
