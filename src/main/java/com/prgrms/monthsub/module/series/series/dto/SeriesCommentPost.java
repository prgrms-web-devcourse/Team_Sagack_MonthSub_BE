package com.prgrms.monthsub.module.series.series.dto;

import com.prgrms.monthsub.module.series.series.dto.SeriesCommentPost.Request;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentPost.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Builder;

public sealed interface SeriesCommentPost permits Request, Response {

  @Schema(name = "SeriesCommentPost.Request")
  record Request(
    @NotBlank(message = "댓글 내용을 입력해주세요.")
    String comment,
    Long seriesId,
    Long parentId
  ) implements SeriesCommentPost {
    @Builder
    public Request{
    }
  }

  @Schema(name = "SeriesCommentPost.Response")
  record Response(
    Long id
  ) implements SeriesCommentPost {
    @Builder
    public Response{
    }
  }

}
