package com.prgrms.monthsub.controller;

import com.prgrms.monthsub.ApiResponse;
import com.prgrms.monthsub.dto.request.SeriesSubscribePostRequest;
import com.prgrms.monthsub.dto.response.SeriesSubscribePostResponse;
import com.prgrms.monthsub.service.S3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/series")
public class SeriesController {

    private static final String DIRECTORY = "seriesThumbnails";

    private final S3Uploader s3Uploader;

    public SeriesController(S3Uploader s3Uploader) {this.s3Uploader = s3Uploader;}

    @Operation(summary = "시리즈 공고 게시글을 작성할 수 있습니다.")
    @PostMapping
    @RequestMapping("/writer/{writerId}")
    public ApiResponse<SeriesSubscribePostResponse> postSeries(@PathVariable Long writerId,
        @RequestPart MultipartFile thumbnail,
        @RequestPart SeriesSubscribePostRequest request) throws IOException {
        s3Uploader.upload(thumbnail, DIRECTORY);

        return ApiResponse.ok(HttpMethod.POST, null);
    }

}
