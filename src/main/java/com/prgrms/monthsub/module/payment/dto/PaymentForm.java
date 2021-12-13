package com.prgrms.monthsub.module.payment.dto;

import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;

public class PaymentForm {

  @Schema(name = "PaymentWindow.Response")
  public record Response(
    Series series
  ) {
  }

  @Builder
  public static class Series {
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
  
}
