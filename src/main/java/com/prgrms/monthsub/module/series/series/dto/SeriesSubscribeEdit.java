package com.prgrms.monthsub.module.series.series.dto;

import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeEdit.Request;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeEdit.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Builder;

public sealed interface SeriesSubscribeEdit permits Request, Response {

  @Schema(name = "SeriesSubscribeEdit.Request")
  record Request(
    Long writerId,

    @NotBlank(message = "제목이 비어있습니다.")
    String title,

    @NotBlank(message = "시리즈 소개문장이 비어있습니다.")
    String introduceSentence,

    @NotBlank(message = "시리즈 글이 비어있습니다.")
    String introduceText,

    String[] uploadDate,

    @NotBlank(message = "업로드 시간이 비어있습니다.")
    String uploadTime
  ) implements SeriesSubscribeEdit {
    @Builder
    public Request {
    }
  }

  @Schema(name = "SeriesSubscribeEdit.Response")
  record Response(
    Long seriesId,
    Boolean isMine
  ) implements SeriesSubscribeEdit {
    @Builder
    public Response {
    }
  }

}
