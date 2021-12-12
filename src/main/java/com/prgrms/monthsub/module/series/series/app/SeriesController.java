package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.common.security.jwt.JwtAuthentication;
import com.prgrms.monthsub.module.series.series.domain.type.SortType;
import com.prgrms.monthsub.module.series.series.dto.SeriesLikesEvent;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeEdit;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeOne;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribePost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @RequestPart MultipartFile thumbnail,
    @Valid @RequestPart SeriesSubscribePost.Request request
  ) {
    return this.seriesAssemble.createSeries(authentication.userId, thumbnail, request);
  }

  @GetMapping("/{id}")
  @Operation(summary = "시리즈 공고 게시글 단건 조회")
  @Tag(name = "[화면]-시리즈")
  public SeriesSubscribeOne.Response getSeriesById(
    @PathVariable Long id
  ) {
    return this.seriesAssemble.getSeriesBySeriesId(id);
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
  public SeriesSubscribeOne.ResponseUsageEdit getSeriesByIdUsageEdit(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) {
    return this.seriesAssemble.getSeriesUsageEdit(id);
  }

  @PutMapping(path = "/{id}/edit")
  @Operation(summary = "시리즈 공고 게시글 수정")
  @Tag(name = "[화면]-시리즈")
  public SeriesSubscribeEdit.Response editSeries(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id,
    @Valid @RequestBody SeriesSubscribeEdit.Request request
  ) {
    return this.seriesAssemble.editSeries(id, request);
  }

  @PutMapping(path = "/{id}/thumbnail", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Operation(summary = "시리즈 썸네일 이미지 업데이트")
  @Tag(name = "[화면]-시리즈")
  public String registerImage(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id,
    @RequestPart MultipartFile thumbnail
  ) {
    return this.seriesAssemble.changeThumbnail(thumbnail, id, authentication.userId);
  }

  @GetMapping("/sort")
  @Operation(summary = "인기순/최신순 시리즈 리스트 조회")
  @Tag(name = "[화면]-시리즈")
  public List<SeriesSubscribeList.Response> getSeriesListOrderBySort(
    @RequestParam(value = "sort", required = true) SortType sort
  ) {
    return this.seriesAssemble.getSeriesListSort(sort);
  }

  @GetMapping("/search/title")
  @Operation(summary = "시리즈제목으로 리스트 조회")
  @Tag(name = "[화면]-시리즈")
  public List<SeriesSubscribeList.Response> getSeriesListSearchTitle(
    @RequestParam(value = "title", required = true) String title
  ) {
    return this.seriesAssemble.getSeriesSearchTitle(title);
  }

  @GetMapping("/search/nickname")
  @Operation(summary = "작가 닉네임으로 리스트 조회")
  @Tag(name = "[화면]-시리즈")
  public List<SeriesSubscribeList.Response> getSeriesListSearchNickname(
    @RequestParam(value = "nickname", required = true) String nickname
  ) {
    return this.seriesAssemble.getSeriesSearchNickname(nickname);
  }

}
