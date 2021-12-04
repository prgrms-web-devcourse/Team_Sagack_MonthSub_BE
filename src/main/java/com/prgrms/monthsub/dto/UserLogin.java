package com.prgrms.monthsub.dto;

public class UserLogin {

    public record Request(
        String email,
        String password
    ) {}

    public record Response(
        Long userId,
        String token,
        String username,
        String group
    ) {}

}
