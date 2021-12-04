package com.prgrms.monthsub.dto.response;

import com.prgrms.monthsub.domain.enumType.Category;
import com.prgrms.monthsub.dto.response.SeriesOneResponse.SeriesObject;
import com.prgrms.monthsub.dto.response.SeriesOneResponse.SubscribeObject;
import com.prgrms.monthsub.dto.response.SeriesOneResponse.WriterObject;

public record SeriesListResponse(
    SeriesObject series,
    SubscribeObject subscribe,
    Category category,
    WriterObject writer
) {
}