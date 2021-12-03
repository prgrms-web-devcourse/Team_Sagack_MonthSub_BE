package com.prgrms.monthsub.dto.response;

import com.prgrms.monthsub.domain.enumType.Category;
import com.prgrms.monthsub.domain.enumType.SeriesStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record SeriesSubscribeGetResponse(
    Long seriesId,
    String thumbnail,
    String title,
    String introduceText,
    String introduceSentence,
    int price,
    LocalDate subscribeStartDate,
    LocalDate subscribeEndDate,
    LocalDate seriesStartDate,
    LocalDate seriesEndDate,
    int articleCount,
    SeriesStatus subscribeStatus,
    int likes,
    Category category,
    String uploadDate,
    LocalTime uploadTime,
    SeriesGetWithWriterResponse writer,
    List<ArticleGetListBySeriesIdResponse> articleList
) {
}