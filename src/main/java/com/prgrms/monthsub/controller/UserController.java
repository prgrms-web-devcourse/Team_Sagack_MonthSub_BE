package com.prgrms.monthsub.controller;

import com.prgrms.monthsub.common.error.ApiResponse;
import com.prgrms.monthsub.domain.User;
import com.prgrms.monthsub.dto.UserLogin;
import com.prgrms.monthsub.dto.UserMe;
import com.prgrms.monthsub.dto.UserSignUp;
import com.prgrms.monthsub.jwt.JwtAuthentication;
import com.prgrms.monthsub.jwt.JwtAuthenticationToken;
import com.prgrms.monthsub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {

    private static final String PROFILE = "profile";

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(path = "/login")
    @Operation(summary = "로그인")
    @Tag(name = "[화면]-로그인")
    public ApiResponse<UserLogin.Response> login(@RequestBody UserLogin.Request request) {
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(
            request.email(), request.password());
        Authentication resultToken = authenticationManager.authenticate(authToken);
        JwtAuthentication authentication = (JwtAuthentication) resultToken.getPrincipal();
        User user = (User) resultToken.getDetails();
        return ApiResponse.ok(HttpMethod.POST, new UserLogin.Response(user.getId(),
            authentication.token, authentication.username, user.getPart().getName()
        ));
    }

    @GetMapping(path = "/me")
    @Operation(summary = "내 정보 확인")
    @Tag(name = "[화면]-내정보")
    public ApiResponse<UserMe.Response> me(
        @AuthenticationPrincipal JwtAuthentication authentication) {
        User user = userService.findByUserId(authentication.userId);
        UserMe.Response me = new UserMe.Response(
            user.getId(), user.getEmail(), user.getUsername(), user.getNickname(),
            user.getProfileImage(),
            user.getProfileIntroduce(), user.getPart().getName()
        );

        return ApiResponse.ok(HttpMethod.GET, me);
    }

    @PostMapping(path = "/signup")
    @Operation(summary = "회원가입")
    @Tag(name = "[화면]-회원가입")
    public ApiResponse<UserSignUp.Response> signUp(@RequestBody UserSignUp.Request request) {
        return ApiResponse.ok(HttpMethod.POST, userService.signUp(request));
    }

    @PostMapping(path = "/profile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "유저 프로필 이미지 업로드")
    @Tag(name = "[사진 업로드]")
    public ApiResponse<String> registerImage(
        @AuthenticationPrincipal JwtAuthentication authentication,
        @RequestPart(required = false) MultipartFile image) throws IOException {
        return ApiResponse.ok(
            HttpMethod.POST, userService.uploadImage(image, authentication.userId, PROFILE));
    }

}
