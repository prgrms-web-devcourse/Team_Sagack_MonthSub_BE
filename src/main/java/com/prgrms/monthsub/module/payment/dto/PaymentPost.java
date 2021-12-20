package com.prgrms.monthsub.module.payment.dto;

import com.prgrms.monthsub.module.payment.dto.PaymentPost.PaymentSeries;
import com.prgrms.monthsub.module.payment.dto.PaymentPost.Response;
import com.prgrms.monthsub.module.payment.dto.PaymentPost.UserPoint;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;

public sealed interface PaymentPost permits PaymentSeries, Response, UserPoint {
  @Schema(name = "PaymentPost.Response")
  record Response(
    PaymentSeries series,
    UserPoint user
  ) implements PaymentPost {
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
  ) implements PaymentPost {
    @Builder
    public PaymentSeries {
    }
  }

  @Schema(name = "PaymentPost.UserPoint")
  record UserPoint(
    int point
  ) implements PaymentPost {
    @Builder
    public UserPoint {
    }
  }

}
