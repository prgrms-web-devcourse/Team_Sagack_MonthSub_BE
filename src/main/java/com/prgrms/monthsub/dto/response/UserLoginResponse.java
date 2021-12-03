package com.prgrms.monthsub.dto.response;

import lombok.Getter;

@Getter
public class UserLoginResponse {

    private final Long userId;

    private final String token;

    private final String username;

    private final String group;

    public UserLoginResponse(Long id, String token, String username, String group) {
        this.userId = id;
        this.token = token;
        this.username = username;
        this.group = group;
    }

}
