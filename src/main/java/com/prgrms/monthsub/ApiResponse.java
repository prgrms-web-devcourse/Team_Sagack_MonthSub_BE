package com.prgrms.monthsub;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    private Boolean success;

    private String httpMethod;

    private int statusCode;

    private T data;

    @Builder
    public ApiResponse(boolean success, String httpMethod, int statusCode, T data) {
        this.success = success;
        this.httpMethod = httpMethod;
        this.statusCode = statusCode;
        this.data = data;
    }

    @Builder
    public ApiResponse(boolean success, int statusCode, T data) {
        this.success = success;
        this.statusCode = statusCode;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(HttpMethod httpMethod, T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .httpMethod(httpMethod.name())
            .statusCode(HttpStatus.OK.value())
            .data(data)
            .build();
    }

    public static <T> ApiResponse<T> fail(int statusCode, T data) {
        return ApiResponse.<T>builder()
            .success(false)
            .statusCode(statusCode)
            .data(data)
            .build();
    }

}