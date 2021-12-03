package com.prgrms.monthsub.dto.response;

import com.prgrms.monthsub.domain.enumType.Category;
import com.prgrms.monthsub.domain.enumType.SeriesStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public record SeriesGetListResponse(
    Long seriesId,
    String thumbnail,
    String title,
    String introduceSentence,
    LocalDate subscribeStartDate,
    LocalDate subscribeEndDate,
    LocalDate seriesStartDate,
    LocalDate seriesEndDate,
    int articleCount,
    SeriesStatus subscribeStatus,
    int likes,
    Category category,
    String uploadDate,
    LocalTime uploadTime
) {
}