package com.prgrms.monthsub.module.payment.dto;

import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public class PaymentSeries {

  @Schema(name = "PaymentSeries.Response")
  public record Response(
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
    String time
  ) {}

}
