package com.prgrms.monthsub.module.series.article.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

public class ArticleEdit {

  @Schema(name = "ArticleEdit.TextChangeRequest")
  public record TextChangeRequest(
    @NotBlank
    String title,

    @NotBlank
    String contents
  ) {
  }

  @Schema(name = "ArticleEdit.TextChangeResponse")
  public record TextChangeResponse(
    Long id
  ) {
  }

}
