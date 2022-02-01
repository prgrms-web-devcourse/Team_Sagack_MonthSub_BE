package com.prgrms.monthsub.module.payment.bill.app;

import com.prgrms.monthsub.module.part.user.app.provider.UserProvider;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.payment.bill.app.provider.PaymentProvider;
import com.prgrms.monthsub.module.payment.bill.converter.PaymentConverter;
import com.prgrms.monthsub.module.payment.bill.domain.Payment;
import com.prgrms.monthsub.module.payment.bill.domain.Payment.Event;
import com.prgrms.monthsub.module.payment.bill.domain.Payment.State;
import com.prgrms.monthsub.module.payment.bill.domain.exception.PaymentException.FailedPayment;
import com.prgrms.monthsub.module.payment.bill.domain.exception.PaymentException.PaymentDuplicated;
import com.prgrms.monthsub.module.payment.bill.dto.PaymentPost.Response;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@EnableRetry
public class PaymentService implements PaymentProvider {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private final SeriesProvider seriesProvider;
  private final UserProvider userProvider;
  private final PaymentConverter paymentConverter;
  private final PaymentRepository paymentRepository;

  public PaymentService(
    SeriesProvider seriesProvider,
    UserProvider userProvider,
    PaymentConverter paymentConverter,
    PaymentRepository paymentRepository
  ) {
    this.seriesProvider = seriesProvider;
    this.userProvider = userProvider;
    this.paymentConverter = paymentConverter;
    this.paymentRepository = paymentRepository;
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
  @Transactional(noRollbackFor = FailedPayment.class)
  public Response pay(
    Long seriesId,
    Long userId
  ) throws FailedPayment, ObjectOptimisticLockingFailureException {
    Series series = this.seriesProvider.getById(seriesId);
    User user = this.userProvider.findById(userId);

    final String message = "seriesId=" + seriesId + ", userId=" + userId;

    //이미 구매한 적이 있는 경우
    this.paymentRepository
      .findByUserIdAndSeriesIdAndState(user.getId(), series.getId(), State.PAY_COMPLETE)
      .map(pay -> {throw new PaymentDuplicated(message + ":이미 결제되었습니다.");});

    Payment payment = this.save(
      this.paymentConverter
        .toEntity(series, user)
        .transit(Event.PAY_REQUIRED)
    );

    if (!user.isPayable(series.getPrice())) {
      payment.transit(Event.PAY_REJECTED);

      throw new FailedPayment(message);
    }

    user.decreasePoint(series.getPrice());
    payment.transit(Event.PAY_ACCEPTED);

    List<ArticleUploadDate> uploadDateList = this.seriesProvider.getArticleUploadDate(
      series.getId());

    return this.paymentConverter.toPaymentPost(series, uploadDateList, user.getPoint());
  }

  private Payment save(Payment payment) {
    return paymentRepository.save(payment);
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
