package com.prgrms.monthsub.module.payment.app;

import com.prgrms.monthsub.module.payment.converter.PaymentConverter;
import com.prgrms.monthsub.module.payment.dto.PaymentForm;
import com.prgrms.monthsub.module.series.series.app.Provider.SeriesProvider;
import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate;
import com.prgrms.monthsub.module.series.series.domain.Series;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

  private final SeriesProvider seriesProvider;
  private final PaymentConverter paymentConverter;

  public PaymentService(
    SeriesProvider seriesProvider,
    PaymentConverter paymentConverter
  ) {
    this.seriesProvider = seriesProvider;
    this.paymentConverter = paymentConverter;
  }

  @Transactional
  public PaymentForm.Response getSeriesById(Long seriesId) {
    Series series = this.seriesProvider.getById(seriesId);
    List<ArticleUploadDate> uploadDateList = this.seriesProvider.getArticleUploadDate(seriesId);

    return this.paymentConverter.seriesToPaymentWindowResponse(series, uploadDateList);
  }

}
