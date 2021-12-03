package com.prgrms.monthsub.dto.response;

public record UserMeResponse(
    Long userId,
    String token,
    String nicName,
    String profileImage,
    String profileIntroduce,
    String group
) {
}
