package com.prgrms.monthsub.module.series.series.dto;

import com.prgrms.monthsub.module.series.series.dto.SeriesLikesList.Response;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SeriesListObject;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

public sealed interface SeriesLikesList permits Response {

  @Schema(name = "SeriesLikesList.Response")
  record Response(
    List<SeriesListObject> seriesList
  ) implements SeriesLikesList {

    @Builder
    public Response {
    }
  }

}
