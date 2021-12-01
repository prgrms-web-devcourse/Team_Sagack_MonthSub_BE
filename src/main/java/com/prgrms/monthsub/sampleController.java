package com.prgrms.monthsub;

import com.prgrms.monthsub.common.error.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample")
@Tag(name = "Sample(API Controller)", description = "샘플 설명")
public class sampleController {

    @PostMapping
    @Tag(name = "[시리즈]")
    @Operation(summary = "시리즈(모집공고 작성) 기능", description = "이 기능에 대한 자세한 설명")
    public ApiResponse<sampleDto> postCheck(@RequestBody sampleDto request) {
        return ApiResponse.ok(HttpMethod.PUT, request);
    }

    @PutMapping
    @Tags({
        @Tag(name = "[시리즈-아티클 정보]"),
        @Tag(name = "[시리즈]")
    })
    @Operation(summary = "샘플 풋입니다.")
    public ApiResponse<sampleDto> putCheck(@RequestBody sampleDto request) {
        return ApiResponse.ok(HttpMethod.PUT, request);
    }

    @PatchMapping
    @Operation(summary = "샘플 패치입니다.")
    public ApiResponse<sampleDto> patchCheck(@RequestBody sampleDto request) {
        return ApiResponse.ok(HttpMethod.PATCH, request);
    }

    @DeleteMapping
    @Operation(summary = "샘플 딜리트입니다.")
    public ApiResponse<String> deleteCheck() {
        return ApiResponse.ok(HttpMethod.DELETE, "Ok");
    }

}
