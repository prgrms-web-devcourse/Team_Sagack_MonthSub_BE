package com.prgrms.monthsub;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiHealthyCheckController {

    @GetMapping("/health")
    public ApiResponse<String> getCheck() {
        return ApiResponse.ok(HttpMethod.GET, "Ok");
    }

}