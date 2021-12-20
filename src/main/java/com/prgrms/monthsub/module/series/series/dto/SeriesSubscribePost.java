package com.prgrms.monthsub.module.series.series.dto;

import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribePost.Request;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribePost.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.Builder;

public sealed interface SeriesSubscribePost permits Request, Response {

  @Schema(name = "SeriesSubscribePost.Request")
  record Request(
    @NotBlank(message = "제목이 비어있습니다.")
    String title,

    @NotBlank(message = "닉네임이 비어있습니다.")
    String introduceSentence,

    @NotBlank(message = "소개문장이 비어있습니다.")
    String introduceText,

    @NotBlank(message = "구독 시작 날짜가 비어있습니다.")
    String subscribeStartDate,

    @NotBlank(message = "구독 종료 날짜가 비어있습니다.")
    String subscribeEndDate,

    @NotBlank(message = "연재 시작 날짜가 비어있습니다.")
    String seriesStartDate,

    @NotBlank(message = "연재 종료 날짜가 비어있습니다.")
    String seriesEndDate,

    @NotBlank(message = "카테고리가 비어있습니다.")
    String category,

    String[] uploadDate,

    @NotBlank(message = "업로드 시간이 비어있습니다.")
    String uploadTime,

    @Positive
    int articleCount,

    @PositiveOrZero
    int price
  ) implements SeriesSubscribePost {
    @Builder
    public Request {
    }
  }

  @Schema(name = "SeriesSubscribePost.Response")
  record Response(
    Long seriesId
  ) implements SeriesSubscribePost {
    @Builder
    public Response {
    }
  }

}
