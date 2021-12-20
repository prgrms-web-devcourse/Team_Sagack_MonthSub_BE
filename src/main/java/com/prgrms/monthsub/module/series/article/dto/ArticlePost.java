package com.prgrms.monthsub.module.series.article.dto;

import com.prgrms.monthsub.module.series.article.dto.ArticlePost.Request;
import com.prgrms.monthsub.module.series.article.dto.ArticlePost.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Builder;

public sealed interface ArticlePost permits Request, Response {

  @Schema(name = "ArticlePost.Request")
  record Request(
    Long seriesId,

    @NotBlank
    String title,

    @NotBlank
    String contents
  ) implements ArticlePost {
    @Builder
    public Request {
    }
  }

  @Schema(name = "ArticlePost.Response")
  record Response(
    Long id,
    Boolean isMine
  ) implements ArticlePost {
    @Builder
    public Response {
    }
  }

}
