package com.prgrms.monthsub.module.payment.bill.domain;

import com.prgrms.monthsub.common.domain.BaseEntity;
import com.prgrms.monthsub.module.series.series.domain.Series;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payment_state_history")
public class PaymentStateHistory extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "user_id", columnDefinition = "BIGINT", nullable = false)
  private Long userId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "series_id", referencedColumnName = "id")
  private Series series;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payment_id", referencedColumnName = "id")
  private Payment payment;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", columnDefinition = "VARCHAR(50)", nullable = false)
  private Payment.State state;

  @Enumerated(EnumType.STRING)
  @Column(name = "event", columnDefinition = "VARCHAR(50)", nullable = false)
  private Payment.Event event;

  public PaymentStateHistory(
    Payment.State state,
    Payment.Event event,
    Long userId,
    Series series,
    Payment payment
  ) {
    this.state = state;
    this.event = event;
    this.userId = userId;
    this.series = series;
    this.payment = payment;
  }
}
