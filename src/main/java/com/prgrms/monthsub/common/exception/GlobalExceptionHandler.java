package com.prgrms.monthsub.common.exception;

import com.prgrms.monthsub.common.exception.base.BusinessException;
import com.prgrms.monthsub.common.exception.base.InvalidInputException;
import com.prgrms.monthsub.common.exception.model.ErrorCodes;
import com.prgrms.monthsub.common.exception.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
    MethodArgumentNotValidException e
  ) {
    log.error("handleMethodArgumentNotValidException", e);

    final ErrorResponse response = ErrorResponse.of(
      ErrorCodes.INVALID_INPUT_VALUE(), e.getBindingResult());

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(response);
  }

  @ExceptionHandler(BindException.class)
  protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
    log.error("handleBindException", e);

    final ErrorResponse response = ErrorResponse.of(
      ErrorCodes.INVALID_INPUT_VALUE(), e.getBindingResult());

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(response);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
    MethodArgumentTypeMismatchException e
  ) {
    log.error("handleMethodArgumentTypeMismatchException", e);

    final ErrorResponse response = ErrorResponse.of(e);

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(response);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
    HttpRequestMethodNotSupportedException e
  ) {
    log.error("handleHttpRequestMethodNotSupportedException", e);

    final ErrorResponse response = ErrorResponse.of(ErrorCodes.METHOD_NOT_ALLOWED());

    return ResponseEntity
      .status(HttpStatus.METHOD_NOT_ALLOWED)
      .body(response);
  }

  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
    log.error("handleAccessDeniedException", e);

    final ErrorResponse response = ErrorResponse.of(ErrorCodes.HANDLE_ACCESS_DENIED());

    return ResponseEntity
      .status(HttpStatus.FORBIDDEN)
      .body(response);
  }

  @ExceptionHandler({
    IllegalStateException.class,
    IllegalArgumentException.class,
    TypeMismatchDataAccessException.class,
    HttpMessageNotReadableException.class,
    MissingServletRequestParameterException.class,
    MultipartException.class
  })
  protected ResponseEntity<ErrorResponse> handleBadRequestException(InvalidInputException e) {
    log.error("handleBadRequestException", e);

    final ErrorResponse response = ErrorResponse.of(ErrorCodes.INVALID_INPUT_VALUE());

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(response);
  }

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
    log.error("handleBusinessException", e);

    final ErrorCodes errorCode = e.getErrorCode();
    final ErrorResponse response = ErrorResponse.of(errorCode, e.getClass());

    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(response);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("handleException", e);

    final ErrorResponse response = ErrorResponse.of(ErrorCodes.INTERNAL_SERVER_ERROR());

    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(response);
  }

  @ExceptionHandler(BadCredentialsException.class)
  protected ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
    log.error("BadCredentialsException", e);

    final ErrorResponse response = ErrorResponse.of(ErrorCodes.INVALID_CREDENTIALS_VALUE());

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(response);
  }

}
