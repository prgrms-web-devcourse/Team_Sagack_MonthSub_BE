package com.prgrms.monthsub.controller;

import com.prgrms.monthsub.common.error.ApiResponse;
import com.prgrms.monthsub.dto.SeriesLikesEvent;
import com.prgrms.monthsub.jwt.JwtAuthentication;
import com.prgrms.monthsub.service.SeriesLikesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/series")
@Tag(name = "Series")
public class SeriesLikesController {

    private final SeriesLikesService seriesLikesService;

    public SeriesLikesController(SeriesLikesService seriesLikesService) {
        this.seriesLikesService = seriesLikesService;
    }

    @PostMapping("/likes/{seriesId}")
    @Operation(summary = "시리즈 공고 좋아요 클릭 이벤트")
    @Tag(name = "[화면]-시리즈")
    public ApiResponse<SeriesLikesEvent.Response> postSeriesLikes(
        @AuthenticationPrincipal JwtAuthentication authentication,
        @PathVariable Long seriesId
    ) {
        return ApiResponse.ok(
            HttpMethod.POST, seriesLikesService.likesEvent(authentication.userId, seriesId));
    }

}
