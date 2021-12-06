package com.prgrms.monthsub.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class SeriesLikesEvent {

    @Schema(name = "SeriesLike.Response")
    public record Response(
        Long id,
        String likeStatus
    ) {
    }

}
