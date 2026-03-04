package com.aiyl.bank.rest.exception;


import com.aiyl.bank.rest.dto.response.ErrorDtoResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.ZoneOffset;


@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDtoResponse> handleApiException(ApiException exception, HttpServletRequest request) {
        log.warn("Business error [{}]: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return buildResponse(
                exception.getStatus(),
                exception.getStatus().getReasonPhrase(),
                exception.getMessage(),
                request);
    }


    private ResponseEntity<ErrorDtoResponse> buildResponse(
            HttpStatus status,
            String error,
            String message,
            HttpServletRequest request
    ){
        ErrorDtoResponse body = ErrorDtoResponse.builder()
                .status(status.value())
                .message(message)
                .error(error)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .build();
        return ResponseEntity.status(status).body(body);
    }

}
