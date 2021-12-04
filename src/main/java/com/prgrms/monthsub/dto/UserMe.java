package com.prgrms.monthsub.dto;

public class UserMe {

    public record Response(
        Long userId,
        String token,
        String nicName,
        String profileImage,
        String profileIntroduce,
        String group
    ) {}

}
