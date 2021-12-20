package com.prgrms.monthsub.module.series.article.dto;

import com.prgrms.monthsub.module.series.article.dto.ArticleEdit.ChangeRequest;
import com.prgrms.monthsub.module.series.article.dto.ArticleEdit.ChangeResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Builder;

public sealed interface ArticleEdit permits ChangeRequest, ChangeResponse {

  @Schema(name = "ArticleEdit.ChangeRequest")
  record ChangeRequest(
    @NotBlank
    String title,

    @NotBlank
    String contents,

    Long seriesId
  ) implements ArticleEdit {

    @Builder
    public ChangeRequest {
    }
  }

  @Schema(name = "ArticleEdit.ChangeResponse")
  record ChangeResponse(
    Long id,
    Boolean isMine
  ) implements ArticleEdit {

    @Builder
    public ChangeResponse {
    }
  }

}
