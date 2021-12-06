package com.prgrms.monthsub.common.exception.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ApiResponse<T> {

    private Boolean success;

    private String httpMethod;

    private int statusCode;

    private String code;

    private T data;

    @Builder
    public ApiResponse(boolean success, String httpMethod, int statusCode, String code, T data) {
        this.success = success;
        this.httpMethod = httpMethod;
        this.statusCode = statusCode;
        this.data = data;
        this.code = code;
    }

    @Builder
    public ApiResponse(boolean success, int statusCode, String code, T data) {
        this.success = success;
        this.statusCode = statusCode;
        this.data = data;
        this.code = code;
    }

    public static <T> ApiResponse<T> ok(HttpMethod httpMethod, T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .httpMethod(httpMethod.name())
            .statusCode(HttpStatus.OK.value())
            .data(data)
            .build();
    }

    public static <T> ApiResponse<T> fail(int statusCode, String code, T data) {
        return ApiResponse.<T>builder()
            .success(false)
            .statusCode(statusCode)
            .code(code)
            .data(data)
            .build();
    }

}
