package com.prgrms.monthsub.module.series.series.dto;

import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SeriesListObject;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class SeriesLikesList {

  @Schema(name = "SeriesLikesList.Response")
  public record Response(
    List<SeriesListObject> seriesList
  ) {
  }

}
