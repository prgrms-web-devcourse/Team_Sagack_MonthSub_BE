package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.common.security.jwt.JwtAuthentication;
import com.prgrms.monthsub.module.series.series.dto.SeriesLikesList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/likes")
@Tag(name = "Likes")
public class SeriesLikesController {

  private final SeriesLikesService seriesLikesService;

  public SeriesLikesController(SeriesLikesService seriesLikesService) {
    this.seriesLikesService = seriesLikesService;
  }

  @GetMapping
  @Operation(summary = "내가 좋아요한 시리즈 리스트 조회")
  @Tag(name = "[화면]-좋아요 시리즈")
  public SeriesLikesList.Response getSeriesLikesList(
    @AuthenticationPrincipal JwtAuthentication authentication
  ) {
    return this.seriesLikesService.findAllMySeriesLikeByUserId(authentication.userId);
  }

}
