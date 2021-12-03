package com.prgrms.monthsub.dto.response;

public record UserLoginResponse(
    Long userId,
    String token,
    String username,
    String group
) {
}
