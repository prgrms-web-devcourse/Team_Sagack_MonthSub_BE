package com.prgrms.monthsub;

import com.prgrms.monthsub.common.error.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "ApiHealthyCheck", description = "컨트롤러 설명")
public class ApiHealthyCheckController {

    @GetMapping("/health")
    @Tag(name = "메인화면")
    @Operation(summary = "어떤 API인지 상태 설명")
    public ApiResponse<String> getCheck() {
        return ApiResponse.ok(HttpMethod.GET, "Ok");
    }

}