package com.prgrms.monthsub.module.payment.bill.app;

import com.prgrms.monthsub.module.payment.bill.domain.PaymentStateHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentStateHistoryRepository extends JpaRepository<PaymentStateHistory, Long> {
}
