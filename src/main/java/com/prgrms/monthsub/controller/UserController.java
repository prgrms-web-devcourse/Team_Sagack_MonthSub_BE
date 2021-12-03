package com.prgrms.monthsub.controller;


import com.prgrms.monthsub.common.error.ApiResponse;
import com.prgrms.monthsub.common.error.exception.UserNotFoundException;
import com.prgrms.monthsub.domain.User;
import com.prgrms.monthsub.dto.request.UserLoginRequest;
import com.prgrms.monthsub.dto.response.UserLoginResponse;
import com.prgrms.monthsub.dto.response.UserMeResponse;
import com.prgrms.monthsub.jwt.JwtAuthentication;
import com.prgrms.monthsub.jwt.JwtAuthenticationToken;
import com.prgrms.monthsub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(path = "/users/login")
    @Operation(summary = "로그인")
    @Tag(name = "[화면]-로그인")
    public ApiResponse<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(
            request.email(), request.password());
        Authentication resultToken = authenticationManager.authenticate(authToken);
        JwtAuthentication authentication = (JwtAuthentication) resultToken.getPrincipal();
        User user = (User) resultToken.getDetails();
        return ApiResponse.ok(HttpMethod.POST, new UserLoginResponse(user.getId(),
            authentication.token, authentication.username, user.getPart().getName()
        ));
    }

    @GetMapping(path = "/users/me")
    @Operation(summary = "내 정보 확인")
    @Tag(name = "[화면]-내정보")
    public ApiResponse<UserMeResponse> me(@AuthenticationPrincipal JwtAuthentication authentication) {
        return ApiResponse.ok(HttpMethod.GET, userService.findByUserName(authentication.username)
            .map(user ->
                new UserMeResponse(
                    user.getId(), authentication.token, user.getNickname(),
                    user.getProfileImage(),
                    user.getProfileIntroduce(), user.getPart().getName()
                )
            )
            .orElseThrow(UserNotFoundException::new));
    }

}
