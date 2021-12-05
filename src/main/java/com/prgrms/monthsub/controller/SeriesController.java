package com.prgrms.monthsub.controller;

import com.prgrms.monthsub.common.error.ApiResponse;
import com.prgrms.monthsub.dto.SeriesSubscribeEdit;
import com.prgrms.monthsub.dto.SeriesSubscribeList;
import com.prgrms.monthsub.dto.SeriesSubscribeOne;
import com.prgrms.monthsub.dto.SeriesSubscribePost;
import com.prgrms.monthsub.jwt.JwtAuthentication;
import com.prgrms.monthsub.service.SeriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/series")
@Tag(name = "Series")
public class SeriesController {

    private final SeriesService seriesService;

    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "시리즈 공고 게시글 작성")
    @Tag(name = "[화면]-시리즈")
    public ApiResponse<SeriesSubscribePost.Response> postSeries(
        @AuthenticationPrincipal JwtAuthentication authentication,
        @RequestPart MultipartFile thumbnail,
        @Valid @RequestPart SeriesSubscribePost.Request request) throws IOException {
        return ApiResponse.ok(
            HttpMethod.POST, seriesService.createSeries(authentication.userId, thumbnail, request));
    }

    @Operation(summary = "시리즈 공고 게시글 단건 조회")
    @GetMapping("/{id}")
    @Tag(name = "[화면]-시리즈")
    public ApiResponse<SeriesSubscribeOne.Response> getSeriesById(
        @PathVariable Long id) {
        return ApiResponse.ok(HttpMethod.GET, seriesService.getSeriesBySeriesId(id));
    }

    @Operation(summary = "시리즈 공고 게시글 리스트 조회")
    @GetMapping("")
    @Tag(name = "[화면]-시리즈")
    public ApiResponse<List<SeriesSubscribeList.Response>> getSeriesList() {
        return ApiResponse.ok(HttpMethod.GET, seriesService.getSeriesList());
    }

    @PutMapping(path = "/{seriesId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "시리즈 공고 게시글 수정")
    @Tag(name = "[화면]-시리즈")
    public ApiResponse<SeriesSubscribeEdit.Response> editSeries(
        @AuthenticationPrincipal JwtAuthentication authentication,
        @PathVariable Long seriesId,
        @RequestPart(required = false) MultipartFile thumbnail,
        @Valid @RequestPart SeriesSubscribeEdit.Request request) throws IOException {
        return ApiResponse.ok(
            HttpMethod.PUT, seriesService.editSeries(seriesId, thumbnail, request));
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "수정 요청시 시리즈 공고 단건 조회")
    @Tag(name = "[화면]-시리즈")
    public ApiResponse<SeriesSubscribeOne.ResponseUsageEdit> getSeriesByIdUsageEdit(
        @AuthenticationPrincipal JwtAuthentication authentication,
        @PathVariable Long id
    ) {
        return ApiResponse.ok(HttpMethod.GET, seriesService.getSeriesUsageEdit(id));
    }

}
