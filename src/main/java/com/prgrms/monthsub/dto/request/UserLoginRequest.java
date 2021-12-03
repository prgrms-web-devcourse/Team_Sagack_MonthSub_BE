package com.prgrms.monthsub.dto.request;

import lombok.Getter;

@Getter
public class UserLoginRequest {

    private String email;

    private String password;

    protected UserLoginRequest() {}

    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
