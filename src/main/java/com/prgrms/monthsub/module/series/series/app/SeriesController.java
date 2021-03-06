package com.prgrms.monthsub.module.series.series.app;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import com.prgrms.monthsub.common.security.jwt.JwtAuthentication;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import com.prgrms.monthsub.module.series.series.dto.SeriesLikesEvent;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeEdit;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeOne;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribePost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/series")
@Tag(name = "Series")
public class SeriesController {

  private final SeriesAssemble seriesAssemble;
  private final SeriesLikesService seriesLikesService;

  public SeriesController(
    SeriesAssemble seriesAssemble,
    SeriesLikesService seriesLikesService
  ) {
    this.seriesAssemble = seriesAssemble;
    this.seriesLikesService = seriesLikesService;
  }

  @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Operation(summary = "????????? ?????? ????????? ??????")
  @Tag(name = "[??????]-?????????")
  public SeriesSubscribePost.Response postSeries(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestPart MultipartFile file,
    @Valid @RequestPart SeriesSubscribePost.Request request
  ) {
    return this.seriesAssemble.createSeries(authentication.userId, file, request);
  }

  @GetMapping("/popular")
  @Operation(summary = "?????? ????????? ????????? ??????")
  @Tag(name = "[??????]-?????? ?????????")
  public SeriesSubscribeList.Response getPopularSeriesList() {
    return this.seriesAssemble.getPopularSeriesList();
  }

  @GetMapping("/recent")
  @Operation(summary = "?????? ????????? ????????? ??????")
  @Tag(name = "[??????]-?????? ?????????")
  public SeriesSubscribeList.Response getRecentSeriesList(
    @AuthenticationPrincipal JwtAuthentication authentication
  ) {
    return ofNullable(authentication)
      .map(authenticate -> this.seriesAssemble.getRecentSeriesList(
        of(authenticate.userId)
      ))
      .orElse(this.seriesAssemble.getRecentSeriesList(Optional.empty()));
  }

  @GetMapping("/{id}")
  @Operation(summary = "????????? ?????? ????????? ?????? ??????")
  @Tag(name = "[??????]-?????????")
  public SeriesSubscribeOne.Response getSeriesById(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) {
    return ofNullable(authentication)
      .map(authenticate -> this.seriesAssemble.getSeriesOne(
        id, of(authenticate.userId)
      ))
      .orElse(this.seriesAssemble.getSeriesOne(id, Optional.empty()));
  }

  @PostMapping("/{id}/likes")
  @Operation(summary = "????????? ?????? ????????? ?????? ?????????")
  @Tag(name = "[??????]-?????????")
  public SeriesLikesEvent.Response likeSeries(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) {
    return this.seriesAssemble.likesEvent(authentication.userId, id);
  }

  @DeleteMapping("/{id}/likes")
  @Operation(summary = "????????? ?????? ????????? ?????? ?????? ?????????")
  @Tag(name = "[??????]-?????????")
  public SeriesLikesEvent.Response cancelSeriesLike(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) {
    return this.seriesAssemble.cancelSeriesLike(authentication.userId, id);
  }

  @GetMapping("/{id}/edit")
  @Operation(summary = "?????? ????????? ????????? ?????? ?????? ??????")
  @Tag(name = "[??????]-?????????")
  public SeriesSubscribeOne.UsageEditResponse getSeriesByIdUsageEdit(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) {
    return this.seriesAssemble.getSeriesUsageEdit(id);
  }

  @PutMapping(path = "/{id}/edit", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Operation(summary = "????????? ?????? ????????? ??????")
  @Tag(name = "[??????]-?????????")
  public SeriesSubscribeEdit.Response editSeries(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id,
    @Valid @RequestPart SeriesSubscribeEdit.Request request,
    @RequestPart(required = false) MultipartFile file
  ) {
    return this.seriesAssemble.editSeries(
      id, request, ofNullable(file), authentication.userId
    );
  }

  @GetMapping("/writer/posts")
  @Operation(summary = "????????? ????????? ????????? ?????????")
  @Tag(name = "[??????]-?????????")
  public SeriesSubscribeList.Response getSeriesPostList(
    @AuthenticationPrincipal JwtAuthentication authentication
  ) {
    return this.seriesAssemble.getSeriesPostList(authentication.userId);
  }

  @GetMapping("/search/title")
  @Operation(summary = "????????? ???????????? ????????? ??????(??????)")
  @Tag(name = "[??????]-?????????")
  public SeriesSubscribeList.Response getSeriesListSearchTitle(
    @RequestParam(value = "title", required = true) String title
  ) {
    return this.seriesAssemble.getSeriesSearchTitle(title);
  }

  @GetMapping("/search/nickname")
  @Operation(summary = "?????? ??????????????? ????????? ??????(??????)")
  @Tag(name = "[??????]-?????????")
  public SeriesSubscribeList.Response getSeriesListSearchNickname(
    @RequestParam(value = "nickname", required = true) String nickname
  ) {
    return this.seriesAssemble.getSeriesSearchNickname(nickname);
  }

  @GetMapping
  @Operation(summary = "????????? ????????? ????????? ??????(?????? ?????????)")
  @Tag(name = "[??????]-?????????")
  public SeriesSubscribeList.Response getSeriesList(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestParam @Positive Integer size,
    @RequestParam(required = false) Long lastSeriesId,
    @RequestParam(required = false) Category[] categories,
    @RequestParam(required = false) SeriesStatus[] status
  ) {
    return ofNullable(authentication)
      .map(authenticate -> this.seriesAssemble.getSeriesList(
        ofNullable(lastSeriesId), size, List.of(categories),
        of(authenticate.userId), List.of(status)
      ))
      .orElse(this.seriesAssemble.getSeriesList(
        ofNullable(lastSeriesId), size, List.of(categories),
        Optional.empty(), List.of(status)
      ));
  }

  @GetMapping("/subscribe")
  @Operation(summary = "????????? ????????? ?????? ????????? ??????")
  @Tags({
    @Tag(name = "[??????]-?????????"),
    @Tag(name = "[??????]-?????? ?????????")
  })
  public SeriesSubscribeList.Response getSeriesSubscribeList(
    @AuthenticationPrincipal JwtAuthentication authentication
  ) {
    return this.seriesAssemble.getSeriesSubscribeList(authentication.userId);
  }

}
