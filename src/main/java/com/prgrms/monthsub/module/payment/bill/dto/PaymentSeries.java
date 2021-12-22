package com.prgrms.monthsub.module.payment.bill.dto;

import com.prgrms.monthsub.module.payment.bill.dto.PaymentSeries.Response;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;

public sealed interface PaymentSeries permits Response {

  @Schema(name = "PaymentSeries.Response")
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
    String time
  ) implements PaymentSeries {
    @Builder
    public Response {
    }
  }

}
