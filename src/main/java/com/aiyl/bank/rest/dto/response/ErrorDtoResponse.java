package com.aiyl.bank.rest.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record ErrorDtoResponse(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp
) {
}
