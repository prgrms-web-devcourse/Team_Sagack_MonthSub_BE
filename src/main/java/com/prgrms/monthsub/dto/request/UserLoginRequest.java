package com.prgrms.monthsub.dto.request;

public record UserLoginRequest(
    String email,
    String password
) {
}
