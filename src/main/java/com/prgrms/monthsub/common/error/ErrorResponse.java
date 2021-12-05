package com.prgrms.monthsub.common.error;

import com.prgrms.monthsub.common.error.ErrorCodes.ErrorCode;
import com.prgrms.monthsub.common.error.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String exception;

    private String message;

    private int status;

    private List<FieldError> errors;

    private String code;

    private LocalDateTime severTime;

    private ErrorResponse(final ErrorCodes exceptions, final List<FieldError> errors) {
        ErrorCode code = exceptions.errorCode();
        this.message = exceptions.message();
        this.status = code.getStatus().value();
        this.errors = errors;
        this.code = code.getCode();
        this.severTime = LocalDateTime.now();
    }

    private ErrorResponse(final ErrorCodes exceptions) {
        ErrorCode code = exceptions.errorCode();
        this.message = exceptions.message();
        this.status = code.getStatus().value();
        this.errors = new ArrayList<>();
        this.code = code.getCode();
        this.severTime = LocalDateTime.now();
    }

    private ErrorResponse(final ErrorCodes exceptions,
        Class<? extends BusinessException> exceptionClass) {
        ErrorCode code = exceptions.errorCode();
        this.message = exceptions.message();
        this.status = code.getStatus().value();
        this.errors = new ArrayList<>();
        this.code = code.getCode();
        this.exception = exceptionClass.getSimpleName();
        this.severTime = LocalDateTime.now();
    }

    public static ErrorResponse of(final ErrorCodes code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(final ErrorCodes code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCodes code,
        Class<? extends BusinessException> exceptionClass) {
        return new ErrorResponse(code, exceptionClass);
    }

    public static ErrorResponse of(final ErrorCodes code, final List<FieldError> errors) {
        return new ErrorResponse(code, errors);
    }

    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<ErrorResponse.FieldError> errors = ErrorResponse.FieldError.of(
            e.getName(), value, e.getErrorCode());
        return new ErrorResponse(ErrorCodes.INVALID_TYPE_VALUE(), errors);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {

        private String field;

        private String value;

        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                .map(error -> new FieldError(
                    error.getField(),
                    error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                    error.getDefaultMessage()
                ))
                .collect(Collectors.toList());
        }

    }

}
