package com.prgrms.monthsub.module.series.series.dto;

import com.prgrms.monthsub.module.series.series.dto.SeriesCommentEdit.Request;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentEdit.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Builder;

public sealed interface SeriesCommentEdit permits Request, Response {

  @Schema(name = "SeriesCommentEdit.Request")
  record Request(
    @NotBlank(message = "댓글 내용을 입력해주세요.")
    String comment,
    Long id
  ) implements SeriesCommentEdit {
    @Builder
    public Request{
    }
  }

  @Schema(name = "SeriesCommentEdit.Response")
  record Response(
    Long id,
    Boolean isMine
  ) implements SeriesCommentEdit {
    @Builder
    public Response{
    }
  }

}
