package com.prgrms.monthsub.module.series.article.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

public class ArticleEdit {

  @Schema(name = "ArticleEdit.ChangeRequest")
  public record ChangeRequest(
    @NotBlank
    String title,

    @NotBlank
    String contents,

    Long seriesId
  ) {
  }

  @Schema(name = "ArticleEdit.ChangeResponse")
  public record ChangeResponse(
    Long id
  ) {
  }

}
