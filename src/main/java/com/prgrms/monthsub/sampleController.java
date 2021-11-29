package com.prgrms.monthsub;

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
public class sampleController {

    @PostMapping
    public ApiResponse<sampleDto> postCheck(@RequestBody sampleDto request) {
        return ApiResponse.ok(HttpMethod.PUT, request);
    }

    @PutMapping
    public ApiResponse<sampleDto> putCheck(@RequestBody sampleDto request) {
        return ApiResponse.ok(HttpMethod.PUT, request);
    }

    @PatchMapping
    public ApiResponse<sampleDto> patchCheck(@RequestBody sampleDto request) {
        return ApiResponse.ok(HttpMethod.PATCH, request);
    }

    @DeleteMapping
    public ApiResponse<String> deleteCheck() {
        return ApiResponse.ok(HttpMethod.DELETE, "Ok");
    }

}
