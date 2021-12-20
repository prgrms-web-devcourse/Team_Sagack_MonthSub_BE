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

  @Builder
  public static class PaymentSeries {
    public String email;
    public String nickname;
    public String title;
    public String thumbnail;
    public Category category;
    public int price;
    public int articleCount;
    public LocalDate startDate;
    public LocalDate endDate;
    public String[] date;
    public LocalTime time;
  }

  @Builder
  public static class UserPoint {
    public int point;
  }

}
