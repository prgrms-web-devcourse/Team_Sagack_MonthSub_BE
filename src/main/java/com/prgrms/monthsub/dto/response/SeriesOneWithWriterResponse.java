package com.prgrms.monthsub.dto.response;

public record SeriesOneWithWriterResponse(
    Long writerId,
    int followCount,
    SeriesOneWithUserResponse user
) {
}