package com.prgrms.monthsub.controller;

import com.prgrms.monthsub.common.error.ApiResponse;
import com.prgrms.monthsub.dto.request.SeriesSubscribePostRequest;
import com.prgrms.monthsub.dto.response.SeriesSubscribePostResponse;
import com.prgrms.monthsub.service.SeriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import javax.validation.Valid;
import org.springframework.http.HttpMethod;
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

    @PostMapping("/users/{userId}")
    @Operation(summary = "시리즈 공고 게시글 작성")
    @Tag(name = "[화면]-시리즈")
    public ApiResponse<SeriesSubscribePostResponse> postSeries(
        @PathVariable Long userId,
        @RequestPart MultipartFile thumbnail,
        @Valid @RequestPart SeriesSubscribePostRequest request) throws IOException {
        return ApiResponse.ok(HttpMethod.POST, seriesService.createSeries(userId, thumbnail, request));
    }

}
