package com.prgrms.monthsub.module.payment.app;

import com.prgrms.monthsub.module.payment.domain.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

  Optional<Payment> findByUserIdAndSeriesId(
    Long userId,
    Long seriesId
  );

}
