package com.prgrms.monthsub.common.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prgrms.monthsub.common.error.ErrorCodes.ErrorCode;
import com.prgrms.monthsub.common.error.exception.UnAuthorizedException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class FilterExceptionHandler extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (UnAuthorizedException ex) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, ex);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCodes.UN_AUTHORIZED());
        ApiResponse<ErrorResponse> json = ApiResponse.fail(
            ErrorCode.UN_AUTHORIZED.getStatus().value(),
            errorResponse.getCode(), errorResponse
        );
        try {
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String objectToJson = objectMapper.writeValueAsString(json);
            response.getWriter().write(objectToJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
