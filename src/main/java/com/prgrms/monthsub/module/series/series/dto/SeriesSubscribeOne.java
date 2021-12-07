package com.prgrms.monthsub.module.series.series.dto;

import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.BriefArticleBySeriesIdResponse;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SeriesObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SubscribeObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.UploadObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.WriterObject;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class SeriesSubscribeOne {

    @Schema(name = "SeriesSubscribeOne.Response")
    public record Response(
        SeriesObject series,
        UploadObject upload,
        SubscribeObject subscribe,
        Category category,
        WriterObject writer,
        List<BriefArticleBySeriesIdResponse> articleList
    ) {

    }

    @Schema(name = "SeriesSubscribeOne.ResponseUsageEdit")
    public record ResponseUsageEdit(
        SeriesObject series,
        Category category,
        UploadObject upload,
        SubscribeObject subscribe
    ) {

    }

}
