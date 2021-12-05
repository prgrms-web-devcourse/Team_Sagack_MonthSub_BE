package com.prgrms.monthsub.common.error;

import com.prgrms.monthsub.common.error.ErrorCodes.ErrorCode;
import com.prgrms.monthsub.common.error.exception.global.BusinessException;
import com.prgrms.monthsub.common.error.exception.global.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ApiResponse<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        final ErrorResponse response = ErrorResponse.of(
            ErrorCodes.INVALID_INPUT_VALUE(), e.getBindingResult());
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), response.getCode(), response);
    }

    @ExceptionHandler(BindException.class)
    protected ApiResponse<ErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException", e);
        final ErrorResponse response = ErrorResponse.of(
            ErrorCodes.INVALID_INPUT_VALUE(), e.getBindingResult());
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), response.getCode(), response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ApiResponse<ErrorResponse> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        final ErrorResponse response = ErrorResponse.of(e);
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), response.getCode(), response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ApiResponse<ErrorResponse> handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCodes.METHOD_NOT_ALLOWED());
        return ApiResponse.fail(HttpStatus.METHOD_NOT_ALLOWED.value(), response.getCode(), response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ApiResponse<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCodes.HANDLE_ACCESS_DENIED());
        return ApiResponse.fail(ErrorCode.HANDLE_ACCESS_DENIED.getStatus().value(),
            response.getCode(), response
        );
    }

    @ExceptionHandler({
        IllegalStateException.class,
        IllegalArgumentException.class,
        TypeMismatchDataAccessException.class,
        HttpMessageNotReadableException.class,
        MissingServletRequestParameterException.class,
        MultipartException.class
    })
    protected ApiResponse<ErrorResponse> handleBadRequestException(InvalidInputException e) {
        log.error("handleBadRequestException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCodes.INVALID_INPUT_VALUE());
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), response.getCode(), response);
    }

    @ExceptionHandler(BusinessException.class)
    protected ApiResponse<ErrorResponse> handleBusinessException(final BusinessException e) {
        log.error("handleBusinessException", e);
        final ErrorCodes errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode, e.getClass());
        return ApiResponse.fail(errorCode.errorCode().getStatus().value(), response.getCode(), response);
    }

    @ExceptionHandler(Exception.class)
    protected ApiResponse<ErrorResponse> handleException(Exception e) {
        log.error("handleException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCodes.INTERNAL_SERVER_ERROR());
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getCode(), response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ApiResponse<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        log.error("BadCredentialsException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCodes.INVALID_CREDENTIALS_VALUE());
        return ApiResponse.fail(ErrorCode.INVALID_CREDENTIALS_VALUE.getStatus().value(),
            response.getCode(), response
        );
    }

}
