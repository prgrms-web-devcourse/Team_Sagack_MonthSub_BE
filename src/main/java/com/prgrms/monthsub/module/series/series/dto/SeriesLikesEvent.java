package com.prgrms.monthsub.module.series.series.dto;

import com.prgrms.monthsub.module.series.series.dto.SeriesLikesEvent.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public sealed interface SeriesLikesEvent permits Response {

  @Schema(name = "SeriesLike.Response")
  record Response(
    Long id,
    String likeStatus
  ) implements SeriesLikesEvent {

    @Builder
    public Response {

    }
  }

}
