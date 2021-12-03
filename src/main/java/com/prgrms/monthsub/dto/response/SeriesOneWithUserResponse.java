package com.prgrms.monthsub.dto.response;

public record SeriesOneWithUserResponse(
    Long userId,
    String email,
    String profileImage,
    String profileIntroduce,
    String nickname
) {
}