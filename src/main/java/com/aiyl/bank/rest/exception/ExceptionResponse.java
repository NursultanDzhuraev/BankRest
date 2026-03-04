package com.aiyl.bank.rest.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ExceptionResponse(HttpStatus httpStatus, String exceptionClassName, String message) {
}
