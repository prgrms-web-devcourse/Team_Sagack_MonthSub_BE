package com.prgrms.monthsub.module.series.article.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public class ArticleOne {

  @Schema(name = "ArticleOne.Response")
  public record Response(
    String title,
    String contents,
    String thumbnailKey,
    int round,
    String nickname,
    String profileKey,
    String profileIntroduce,
    LocalDate createdDate,
    LocalDate updatedDate
  ) {
  }

}
