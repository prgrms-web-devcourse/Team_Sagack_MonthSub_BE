package com.prgrms.monthsub.module.payment.app;

import com.prgrms.monthsub.module.part.user.app.provider.UserProvider;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.payment.converter.PaymentConverter;
import com.prgrms.monthsub.module.payment.dto.PaymentForm;
import com.prgrms.monthsub.module.payment.dto.PaymentPost.Response;
import com.prgrms.monthsub.module.series.series.app.Provider.SeriesProvider;
import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate;
import com.prgrms.monthsub.module.series.series.domain.Series;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

  private final SeriesProvider seriesProvider;
  private final UserProvider userProvider;
  private final PaymentConverter paymentConverter;
  private final Logger log = LoggerFactory.getLogger(getClass());


  public PaymentService(
    SeriesProvider seriesProvider,
    UserProvider userProvider,
    PaymentConverter paymentConverter
  ) {
    this.seriesProvider = seriesProvider;
    this.userProvider = userProvider;
    this.paymentConverter = paymentConverter;
  }

  @Transactional
  public PaymentForm.Response getSeriesById(Long seriesId) {

    Series series = this.seriesProvider.getById(seriesId);
    List<ArticleUploadDate> uploadDateList = this.seriesProvider.getArticleUploadDate(seriesId);

    return this.paymentConverter.seriesToPaymentWindowResponse(series, uploadDateList);

  }

  @Transactional
  public Response createPayment(
    Long seriesId,
    Long userId
  ) throws ObjectOptimisticLockingFailureException {

    Series series = this.seriesProvider.getById(seriesId);
    List<ArticleUploadDate> uploadDateList = this.seriesProvider.getArticleUploadDate(seriesId);

    User user = this.userProvider.findById(userId);
    user.decreasePoint(series.getPrice());

    return this.paymentConverter.paymentResponse(series, uploadDateList, user.getPoint());

  }
}
