package com.prgrms.monthsub.module.part.user.app;

import com.prgrms.monthsub.common.security.jwt.JwtAuthentication;
import com.prgrms.monthsub.common.security.jwt.JwtAuthenticationToken;
import com.prgrms.monthsub.module.part.user.converter.UserConverter;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.user.dto.UserEdit;
import com.prgrms.monthsub.module.part.user.dto.UserLogin;
import com.prgrms.monthsub.module.part.user.dto.UserMe;
import com.prgrms.monthsub.module.part.user.dto.UserSignUp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = "Users")
public class UserController {

  private final UserService userService;
  private final UserAssemble userAssemble;
  private final AuthenticationManager authenticationManager;
  private final UserConverter userConverter;

  public UserController(
    UserService userService,
    UserAssemble userAssemble,
    AuthenticationManager authenticationManager,
    UserConverter userConverter
  ) {
    this.userService = userService;
    this.userAssemble = userAssemble;
    this.authenticationManager = authenticationManager;
    this.userConverter = userConverter;
  }

  @PostMapping(path = "/signin")
  @Operation(summary = "로그인")
  @Tag(name = "[화면]-로그인")
  public UserLogin.Response login(@Valid @RequestBody UserLogin.Request request) {
    JwtAuthenticationToken authToken = new JwtAuthenticationToken(
      request.email(),
      request.password()
    );
    Authentication resultToken = this.authenticationManager.authenticate(authToken);
    JwtAuthentication authentication = (JwtAuthentication) resultToken.getPrincipal();

    User user = (User) resultToken.getDetails();

    return new UserLogin.Response(
      user.getId(),
      authentication.token, authentication.email,
      user.getPart().getName()
    );
  }

  @GetMapping(path = "/users/me")
  @Operation(summary = "내 정보 확인")
  @Tag(name = "[화면]-마이페이지")
  public UserMe.Response me(
    @AuthenticationPrincipal JwtAuthentication authentication
  ) {
    User user = this.userService.findById(authentication.userId);
    return this.userConverter.toUserMe(user);
  }

  @PutMapping(path = "/users/me", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Operation(summary = "내 정보 수정")
  @Tag(name = "[화면]-마이페이지")
  public UserEdit.Response edit(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @Valid @RequestPart UserEdit.Request request,
    @RequestPart(required = false) MultipartFile file
  ) {
    return this.userAssemble.edit(authentication.userId, request, Optional.ofNullable(file));
  }

  @PostMapping(path = "/signup")
  @Operation(summary = "회원가입")
  @Tag(name = "[화면]-회원가입")
  public UserSignUp.Response signUp(@RequestBody UserSignUp.Request request) {
    return this.userService.signUp(request);
  }

}
