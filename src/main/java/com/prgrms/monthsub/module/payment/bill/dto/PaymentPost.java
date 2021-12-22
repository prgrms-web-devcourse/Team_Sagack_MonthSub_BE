package com.prgrms.monthsub.module.payment.bill.dto;

import com.prgrms.monthsub.module.payment.bill.dto.PaymentPost.Response;
import com.prgrms.monthsub.module.payment.bill.dto.PaymentPost.UserPoint;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;

public sealed interface PaymentPost permits Response, UserPoint {
  @Schema(name = "PaymentPost.Response")
  record Response(
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
    LocalTime time,
    UserPoint user
  ) implements PaymentPost {
    @Builder
    public Response {
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
