package com.prgrms.monthsub.dto.response;

public record SeriesGetWithUserResponse(
    Long userId,
    String email,
    String profileImage,
    String profileIntroduce,
    String nickname
) {
}