package com.prgrms.monthsub.controller;


import com.prgrms.monthsub.common.error.ApiResponse;
import com.prgrms.monthsub.domain.User;
import com.prgrms.monthsub.dto.request.UserLoginRequest;
import com.prgrms.monthsub.dto.response.UserLoginResponse;
import com.prgrms.monthsub.jwt.JwtAuthentication;
import com.prgrms.monthsub.jwt.JwtAuthenticationToken;
import com.prgrms.monthsub.service.UserService;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    public UserRestController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(path = "/user/login")
    public ApiResponse<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(
            request.getEmail(), request.getPassword());
        Authentication resultToken = authenticationManager.authenticate(authToken);
        JwtAuthentication authentication = (JwtAuthentication) resultToken.getPrincipal();
        User user = (User) resultToken.getDetails();
        return ApiResponse.ok(HttpMethod.POST, new UserLoginResponse(user.getId(),
            authentication.token, authentication.username, user.getPart().getName()
        ));
    }

}
