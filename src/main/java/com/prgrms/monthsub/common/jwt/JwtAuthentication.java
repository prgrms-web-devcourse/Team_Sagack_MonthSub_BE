package com.prgrms.monthsub.common.jwt;

public class JwtAuthentication {

    public final String token;

    public final Long userId;

    public final String username;

    JwtAuthentication(String token, Long userId, String username) {
        this.token = token;
        this.userId = userId;
        this.username = username;
    }

}
