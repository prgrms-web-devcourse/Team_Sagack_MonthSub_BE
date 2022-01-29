package com.prgrms.monthsub.module.payment.bill.app;

import com.prgrms.monthsub.module.part.user.app.provider.UserProvider;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.user.domain.exception.UserException.NoPoint;
import com.prgrms.monthsub.module.payment.bill.app.provider.PaymentProvider;
import com.prgrms.monthsub.module.payment.bill.converter.PaymentConverter;
import com.prgrms.monthsub.module.payment.bill.domain.Payment;
import com.prgrms.monthsub.module.payment.bill.domain.Payment.Event;
import com.prgrms.monthsub.module.payment.bill.domain.Payment.State;
import com.prgrms.monthsub.module.payment.bill.domain.PaymentStateHistory;
import com.prgrms.monthsub.module.payment.bill.domain.exception.PaymentException.PaymentDuplicated;
import com.prgrms.monthsub.module.payment.bill.dto.PaymentPost;
import com.prgrms.monthsub.module.payment.bill.dto.PaymentSeries;
import com.prgrms.monthsub.module.series.series.app.Provider.SeriesProvider;
import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate;
import com.prgrms.monthsub.module.series.series.domain.Series;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@EnableRetry
public class PaymentService implements PaymentProvider {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private final SeriesProvider seriesProvider;
  private final UserProvider userProvider;
  private final PaymentConverter paymentConverter;
  private final TransactionTemplate transactionTemplate;
  private final PaymentRepository paymentRepository;
  private final PaymentStateHistoryRepository paymentStateHistoryRepository;

  public PaymentService(
    SeriesProvider seriesProvider,
    UserProvider userProvider,
    PaymentConverter paymentConverter,
    TransactionTemplate transactionTemplate,
    PaymentRepository paymentRepository,
    PaymentStateHistoryRepository paymentStateHistoryRepository
  ) {
    this.seriesProvider = seriesProvider;
    this.userProvider = userProvider;
    this.paymentConverter = paymentConverter;
    this.transactionTemplate = transactionTemplate;
    this.paymentRepository = paymentRepository;
    this.paymentStateHistoryRepository = paymentStateHistoryRepository;
  }

  @Transactional
  public PaymentSeries.Response getSeriesById(Long seriesId) {
    Series series = this.seriesProvider.getById(seriesId);
    List<ArticleUploadDate> uploadDateList = this.seriesProvider.getArticleUploadDate(seriesId);

    return this.paymentConverter.toPaymentForm(series, uploadDateList);
  }

  @Transactional
  @Override
  public List<Payment> findAllMySubscribeByUserId(Long userId) {
    return this.paymentRepository.findAllByUserId(userId);
  }

  @Retryable(maxAttempts = 3, value = ObjectOptimisticLockingFailureException.class)
  public PaymentPost.Response pay(
    Long id,
    Long userId
  ) throws Exception {
    try {
      return this.transactionTemplate.execute(status -> {
        try {
          return this.createPayment(id, userId);
        } catch (NoPoint e) {
          e.printStackTrace();
        }
        return null;
      });
    } catch (ObjectOptimisticLockingFailureException e) {
      log.info("충돌 감지 재시도: {}", e.getMessage());

      throw new ObjectOptimisticLockingFailureException("충돌", Throwable.class);
    }
  }

  @Transactional
  public PaymentPost.Response createPayment(
    Long seriesId,
    Long userId
  ) throws ObjectOptimisticLockingFailureException, NoPoint {
    Series series = this.seriesProvider.getById(seriesId);
    List<ArticleUploadDate> uploadDateList = this.seriesProvider.getArticleUploadDate(seriesId);

    User user = this.userProvider.findById(userId);

    Payment payment = this.paymentConverter.toEntity(series, user);
    payment.transit(Event.PAY_REQUIRED);
    this.savePayment(payment);
    this.saveHistory(payment.getHistories().get(0));

    //이미 구매한 적이 있는 경우
    this.paymentRepository
      .findByUserIdAndSeriesIdAndState(userId, seriesId, State.PAY_COMPLETE)
      .map(pay -> {
          throw new PaymentDuplicated("이미 결제되었습니다.");
        }
      );

    //포인트 확인
    try {
      user.decreasePoint(series.getPrice());
      payment.transit(Event.PAY_ACCEPTED);
      this.saveHistory(payment.getHistories().get(1));
    } catch (NoPoint e) {
      payment.transit(Event.PAY_REJECTED);
      this.saveHistory(payment.getHistories().get(1));
      throw new NoPoint(e.getMessage()); //Checked Exception
    }

    return this.paymentConverter.toPaymentPost(series, uploadDateList, user.getPoint());
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Payment savePayment(Payment payment) {
    return paymentRepository.save(payment);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public PaymentStateHistory saveHistory(PaymentStateHistory paymentStateHistory) {
    return paymentStateHistoryRepository.save(paymentStateHistory);
  }

  @Override
  public Optional<Payment> find(
    Long userId,
    Long seriesId
  ) {
    return this.paymentRepository.findByUserIdAndSeriesIdAndState(
      userId, seriesId, State.PAY_COMPLETE);
  }

}
