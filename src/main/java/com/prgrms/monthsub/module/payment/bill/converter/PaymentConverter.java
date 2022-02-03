package com.prgrms.monthsub.module.payment.bill.converter;

import com.prgrms.monthsub.common.s3.config.S3;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.payment.bill.domain.Payment;
import com.prgrms.monthsub.module.payment.bill.domain.Payment.State;
import com.prgrms.monthsub.module.payment.bill.dto.PaymentPost;
import com.prgrms.monthsub.module.payment.bill.dto.PaymentPost.UserPoint;
import com.prgrms.monthsub.module.payment.bill.dto.PaymentSeries.Response;
import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate;
import com.prgrms.monthsub.module.series.series.domain.Series;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PaymentConverter {
  private final S3 s3;

  public PaymentConverter(S3 s3) {
    this.s3 = s3;
  }

  public Response toPaymentForm(
    Series series,
    List<ArticleUploadDate> uploadDateList
  ) {
    return new Response(
      series.getWriter().getUser().getEmail(),
      series.getWriter().getUser().getNickname(),
      series.getTitle(),
      this.s3.getDomain() + "/" + series.getThumbnailKey(),
      series.getCategory(),
      series.getPrice(),
      series.getArticleCount(),
      series.getSubscribeStartDate(),
      series.getSubscribeEndDate(),
      uploadDateList.stream().map(uploadDate ->
        uploadDate.getUploadDate().toString().toLowerCase()).toArray(String[]::new),
      series.getUploadTime().toString()
    );
  }

  public Payment toEntity(
    Series series,
    User user
  ) {
    return Payment.builder()
      .series(series)
      .userId(user.getId())
      .state(State.NULL)
      .build();
  }

  public PaymentPost.Response toPaymentPost(
    Series series,
    List<ArticleUploadDate> uploadDateList,
    int point
  ) {
    return PaymentPost.Response.builder()
      .email(series.getWriter().getUser().getEmail())
      .nickname(series.getWriter().getUser().getNickname())
      .title(series.getTitle())
      .thumbnail(this.s3.getDomain() + "/" + series.getThumbnailKey())
      .category(series.getCategory())
      .price(series.getPrice())
      .articleCount(series.getArticleCount())
      .startDate(series.getSubscribeStartDate())
      .endDate(series.getSubscribeEndDate())
      .date(uploadDateList.stream()
        .map(uploadDate ->
          uploadDate
            .getUploadDate()
            .toString()
            .toLowerCase()
        )
        .toArray(String[]::new)
      )
      .time(series.getUploadTime())
      .user(UserPoint.builder().point(point).build())
      .build();
  }

}
