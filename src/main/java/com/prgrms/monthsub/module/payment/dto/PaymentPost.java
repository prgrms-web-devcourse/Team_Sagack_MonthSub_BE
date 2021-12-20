package com.prgrms.monthsub.module.payment.dto;

import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;

public class PaymentPost {
  @Schema(name = "PaymentPost.Response")
  public record Response(
    PaymentSeries series,
    UserPoint user
  ) {
  }

  @Schema(name = "PaymentPost.PaymentSeries")
  record PaymentSeries(
    String email,
    String nickname,
    String title,
    String thumbnail,
    Category category,
    int price,
    int articleCount,
    LocalDate startDate,
    LocalDate endDate,
    String[] date,
    LocalTime time
  ) {

    @Builder()
    public PaymentSeries {
    }
  }

  @Schema(name = "PaymentPost.UserPoint")
  record UserPoint(
    int point
  ) {

    @Builder
    public UserPoint {
    }
  }

}
