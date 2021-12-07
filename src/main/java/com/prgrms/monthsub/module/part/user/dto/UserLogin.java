package com.prgrms.monthsub.module.part.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserLogin {

    @Schema(name = "UserLogin.Request")
    public record Request(
        String email,
        String password
    ) {}

    @Schema(name = "UserLogin.Response")
    public record Response(
        Long userId,
        String token,
        String username,
        String group
    ) {}

}
