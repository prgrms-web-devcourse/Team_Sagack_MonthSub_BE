package com.prgrms.monthsub.module.series.series.app;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import com.prgrms.monthsub.common.security.jwt.JwtAuthentication;
import com.prgrms.monthsub.module.series.series.dto.MyChannel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/channel")
@Tag(name = "MyChannel")
public class MyChannelController {

  private final MyChannelAssemble channelAssemble;

  public MyChannelController(MyChannelAssemble myChannelService) {
    this.channelAssemble = myChannelService;
  }

  @GetMapping("/me")
  @Operation(summary = "내 채널 조회")
  @Tag(name = "[화면]-채널")
  public MyChannel.Response getMyChannel(
    @AuthenticationPrincipal JwtAuthentication authentication
  ) {
    return this.channelAssemble.getMyChannel(authentication.userId);
  }

  @GetMapping
  @Operation(summary = "다른 유저 채널 조회")
  @Tag(name = "[화면]-채널")
  public MyChannel.OtherResponse getOtherChannel(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestParam(value = "user") Long userId
  ) {
    return ofNullable(authentication)
      .map(authenticate -> this.channelAssemble.getOtherChannel(
        userId, of(authenticate.userId)
      ))
      .orElse(this.channelAssemble.getOtherChannel(
        userId, Optional.empty()
      ));
  }

}
