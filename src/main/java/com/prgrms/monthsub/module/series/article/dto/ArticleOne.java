package com.prgrms.monthsub.module.series.article.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ArticleOne {

  @Schema(name = "ArticleOne.Response")
  public record Response(
    String title,
    String contents,
    String thumbnailKey,
    int round,
    String createdAt
  ) {
  }

}
