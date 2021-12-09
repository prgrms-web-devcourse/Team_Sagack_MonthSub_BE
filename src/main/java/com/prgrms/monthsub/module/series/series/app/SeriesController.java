package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.common.exception.model.ApiResponse;
import com.prgrms.monthsub.common.jwt.JwtAuthentication;
import com.prgrms.monthsub.module.series.series.domain.type.SortType;
import com.prgrms.monthsub.module.series.series.dto.SeriesLikesEvent;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeEdit;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeOne;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribePost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

  private final SeriesService seriesService;
  private final SeriesLikesService seriesLikesService;

  public SeriesController(
    SeriesService seriesService,
    SeriesLikesService seriesLikesService
  ) {
    this.seriesService = seriesService;
    this.seriesLikesService = seriesLikesService;
  }

  @GetMapping
  @Operation(summary = "시리즈 공고 게시글 리스트 조회")
  @Tag(name = "[화면]-시리즈")
  public ApiResponse<List<SeriesSubscribeList.Response>> getSeriesList() {
    return ApiResponse.ok(HttpMethod.GET, this.seriesService.getSeriesList());
  }

  @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Operation(summary = "시리즈 공고 게시글 작성")
  @Tag(name = "[화면]-시리즈")
  public ApiResponse<SeriesSubscribePost.Response> postSeries(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestPart MultipartFile thumbnail,
    @Valid @RequestPart SeriesSubscribePost.Request request
  ) throws IOException {
    return ApiResponse.ok(
      HttpMethod.POST,
      this.seriesService.createSeries(authentication.userId, thumbnail, request)
    );
  }

  @GetMapping("/{id}")
  @Operation(summary = "시리즈 공고 게시글 단건 조회")
  @Tag(name = "[화면]-시리즈")
  public ApiResponse<SeriesSubscribeOne.Response> getSeriesById(
    @PathVariable Long id
  ) {
    return ApiResponse.ok(HttpMethod.GET, this.seriesService.getSeriesBySeriesId(id));
  }

  @PostMapping("/{id}/likes")
  @Operation(summary = "시리즈 공고 좋아요 클릭 이벤트")
  @Tag(name = "[화면]-시리즈")
  public ApiResponse<SeriesLikesEvent.Response> likeSeries(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) {
    return ApiResponse.ok(
      HttpMethod.POST, this.seriesLikesService.likesEvent(authentication.userId, id));
  }

  @DeleteMapping("/{id}/likes")
  @Operation(summary = "시리즈 공고 좋아요 취소 클릭 이벤트")
  @Tag(name = "[화면]-시리즈")
  public ApiResponse<SeriesLikesEvent.Response> cancelSeriesLike(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) {
    return ApiResponse.ok(
      HttpMethod.POST, this.seriesLikesService.cancelSeriesLike(authentication.userId, id));
  }

  @GetMapping("/{id}/edit")
  @Operation(summary = "수정 요청시 시리즈 공고 단건 조회")
  @Tag(name = "[화면]-시리즈")
  public ApiResponse<SeriesSubscribeOne.ResponseUsageEdit> getSeriesByIdUsageEdit(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) {
    return ApiResponse.ok(HttpMethod.GET, this.seriesService.getSeriesUsageEdit(id));
  }

  @PutMapping(path = "/{id}/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "시리즈 공고 게시글 수정")
  @Tag(name = "[화면]-시리즈")
  public ApiResponse<SeriesSubscribeEdit.Response> editSeries(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id,
    @RequestPart MultipartFile thumbnail,
    @Valid @RequestPart SeriesSubscribeEdit.Request request
  ) throws IOException {
    return ApiResponse.ok(
      HttpMethod.PUT,
      this.seriesService.editSeries(id, authentication.userId, thumbnail, request)
    );
  }

  @PatchMapping(path = "/{id}/thumbnail", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Operation(summary = "시리즈 썸네일 이미지 업데이트")
  @Tag(name = "[화면]-시리즈")
  public ApiResponse<String> registerImage(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable String id,
    @RequestPart MultipartFile image
  ) throws IOException {
    return ApiResponse.ok(
      HttpMethod.POST, this.seriesService.uploadThumbnailImage(image, authentication.userId));
  }

  @GetMapping("/sort")
  @Operation(summary = "인기순/최신순 시리즈 리스트 조회")
  @Tag(name = "[화면]-시리즈")
  public ApiResponse<List<SeriesSubscribeList.Response>> getSeriesListOrderBySort(
    @RequestParam(value = "sort", required = true) SortType sort
  ) {
    return ApiResponse.ok(HttpMethod.GET, this.seriesService.getSeriesListSort(sort));
  }

}
