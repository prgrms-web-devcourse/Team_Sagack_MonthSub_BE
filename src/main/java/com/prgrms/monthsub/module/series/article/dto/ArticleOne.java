package com.prgrms.monthsub.module.series.article.dto;

import com.prgrms.monthsub.module.series.article.dto.ArticleOne.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;

public sealed interface ArticleOne permits Response {

  @Schema(name = "ArticleOne.Response")
  record Response(
    Boolean isMine,
    String title,
    String contents,
    String thumbnailKey,
    int round,
    String nickname,
    String profileKey,
    String profileIntroduce,
    LocalDate createdDate,
    LocalDate updatedDate
  ) implements ArticleOne {
    @Builder
    public Response {
    }
  }

}
