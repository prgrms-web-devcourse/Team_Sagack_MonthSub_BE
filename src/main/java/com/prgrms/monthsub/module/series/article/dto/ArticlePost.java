package com.prgrms.monthsub.module.series.article.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

public class ArticlePost {

  @Schema(name = "ArticlePost.Request")
  public record Request(
    Long seriesId,

    @NotBlank
    String title,

    @NotBlank
    String contents
  ) {
  }

  @Schema(name = "ArticlePost.Response")
  public record Response(
    Long id,
    Boolean isMine
  ) {
  }

}
