package com.prgrms.monthsub.module.series.series.app;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import com.prgrms.monthsub.common.security.jwt.JwtAuthentication;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import com.prgrms.monthsub.module.series.series.domain.type.SortType;
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
  @Operation(summary = "시리즈 공고 게시글 작성")
  @Tag(name = "[화면]-시리즈")
  public SeriesSubscribePost.Response postSeries(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestPart MultipartFile file,
    @Valid @RequestPart SeriesSubscribePost.Request request
  ) {
    return this.seriesAssemble.createSeries(authentication.userId, file, request);
  }

  @GetMapping("/popular")
  @Operation(summary = "인기 시리즈 리스트 조회")
  @Tag(name = "[화면]-메인 페이지")
  public SeriesSubscribeList.Response getPopularSeriesList() {
    return this.seriesAssemble.getPopularSeriesList();
  }

  @GetMapping("/recent")
  @Operation(summary = "최신 시리즈 리스트 조회")
  @Tag(name = "[화면]-메인 페이지")
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
  @Operation(summary = "시리즈 공고 게시글 단건 조회")
  @Tag(name = "[화면]-시리즈")
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
  @Operation(summary = "시리즈 공고 좋아요 클릭 이벤트")
  @Tag(name = "[화면]-시리즈")
  public SeriesLikesEvent.Response likeSeries(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) {
    return this.seriesLikesService.likesEvent(authentication.userId, id);
  }

  @DeleteMapping("/{id}/likes")
  @Operation(summary = "시리즈 공고 좋아요 취소 클릭 이벤트")
  @Tag(name = "[화면]-시리즈")
  public SeriesLikesEvent.Response cancelSeriesLike(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) {
    return this.seriesLikesService.cancelSeriesLike(authentication.userId, id);
  }

  @GetMapping("/{id}/edit")
  @Operation(summary = "수정 요청시 시리즈 공고 단건 조회")
  @Tag(name = "[화면]-시리즈")
  public SeriesSubscribeOne.UsageEditResponse getSeriesByIdUsageEdit(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) {
    return this.seriesAssemble.getSeriesUsageEdit(id);
  }

  @PutMapping(path = "/{id}/edit", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Operation(summary = "시리즈 공고 게시글 수정")
  @Tag(name = "[화면]-시리즈")
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
  @Operation(summary = "작가가 발행한 시리즈 리스트")
  @Tag(name = "[화면]-시리즈")
  public SeriesSubscribeList.Response getSeriesPostList(
    @AuthenticationPrincipal JwtAuthentication authentication
  ) {
    return this.seriesAssemble.getSeriesPostList(authentication.userId);
  }

  @GetMapping("/search/title")
  @Operation(summary = "시리즈 제목으로 리스트 조회(검색)")
  @Tag(name = "[화면]-시리즈")
  public SeriesSubscribeList.Response getSeriesListSearchTitle(
    @RequestParam(value = "title", required = true) String title
  ) {
    return this.seriesAssemble.getSeriesSearchTitle(title);
  }

  @GetMapping("/search/nickname")
  @Operation(summary = "작가 닉네임으로 리스트 조회(검색)")
  @Tag(name = "[화면]-시리즈")
  public SeriesSubscribeList.Response getSeriesListSearchNickname(
    @RequestParam(value = "nickname", required = true) String nickname
  ) {
    return this.seriesAssemble.getSeriesSearchNickname(nickname);
  }

  @GetMapping
  @Operation(summary = "최신순 시리즈 리스트 조회(무한 스크롤)")
  @Tag(name = "[화면]-시리즈")
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

  @GetMapping("/sort")
  //@Operation(summary = "인기순/최신순 시리즈 리스트 조회") //추후에 쓸 기능
  @Operation(hidden = true)
  @Tag(name = "[화면]-시리즈")
  public SeriesSubscribeList.Response getSeriesList(
    @RequestParam(value = "sort", required = true) SortType sort
  ) {
    return this.seriesAssemble.getSeriesListSort(sort);
  }

  @GetMapping("/subscribe")
  @Operation(summary = "사용자 시리즈 구독 리스트 조회")
  @Tags({
    @Tag(name = "[화면]-시리즈"),
    @Tag(name = "[화면]-메인 페이지")
  })
  public SeriesSubscribeList.Response getSeriesSubscribeList(
    @AuthenticationPrincipal JwtAuthentication authentication
  ) {
    return this.seriesAssemble.getSeriesSubscribeList(authentication.userId);
  }

}
