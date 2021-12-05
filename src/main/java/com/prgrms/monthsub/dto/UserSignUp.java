package com.prgrms.monthsub.dto;

import javax.validation.constraints.NotBlank;

public class UserSignUp {

    public record Request(

        @NotBlank(message = "이메일이 비어있습니다.")
        String email,

        @NotBlank(message = "이름이 비어있습니다.")
        String userName,

        @NotBlank(message = "비밀번호가 비어있습니다.")
        String password,

        @NotBlank(message = "닉네임이 비어있습니다.")
        String nickName
    ) {}

    public record Response(
        Long userId
    ) {}

}
