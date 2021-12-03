package com.prgrms.monthsub.dto.response;

public record SeriesGetWithWriterResponse(
    Long writerId,
    int followCount,
    SeriesGetWithUserResponse user
) {
}