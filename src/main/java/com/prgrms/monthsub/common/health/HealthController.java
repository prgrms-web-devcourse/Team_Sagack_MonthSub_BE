package com.prgrms.monthsub.common.health;

import com.prgrms.monthsub.common.exception.model.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    @Operation(hidden = true)
    public ApiResponse<String> getCheck() {
        return ApiResponse.ok(HttpMethod.GET, "Ok");
    }

}
