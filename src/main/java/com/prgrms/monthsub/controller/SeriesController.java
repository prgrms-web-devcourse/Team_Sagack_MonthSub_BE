package com.prgrms.monthsub.controller;

import com.prgrms.monthsub.common.error.ApiResponse;
import com.prgrms.monthsub.dto.SeriesSubscribePost;
import com.prgrms.monthsub.dto.response.SeriesListResponse;
import com.prgrms.monthsub.dto.response.SeriesOneResponse;
import com.prgrms.monthsub.service.SeriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping(path = "/users/{userId}",
        consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "시리즈 공고 게시글 작성")
    @Tag(name = "[화면]-시리즈")
    public ApiResponse<SeriesSubscribePost.Response> postSeries(
        @PathVariable Long userId,
        @RequestPart MultipartFile thumbnail,
        @Valid @RequestPart SeriesSubscribePost.Request request) throws IOException {
        return ApiResponse.ok(HttpMethod.POST, seriesService.createSeries(userId, thumbnail, request));
    }

    @Operation(summary = "시리즈 공고 게시글 단건 조회")
    @GetMapping("/{id}")
    @Tag(name = "[화면]-시리즈")
    public ApiResponse<SeriesOneResponse> getSeriesById(
        @PathVariable Long id) {
        return ApiResponse.ok(HttpMethod.GET, seriesService.getSeriesBySeriesId(id));
    }

    @Operation(summary = "시리즈 공고 게시글 리스트 조회")
    @GetMapping("")
    @Tag(name = "[화면]-시리즈")
    public ApiResponse<List<SeriesListResponse>> getSeriesList() {
        return ApiResponse.ok(HttpMethod.GET, seriesService.getSeriesList());
    }

}
