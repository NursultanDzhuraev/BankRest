package com.aiyl.bank.rest.dto.response;

import com.aiyl.bank.rest.enam.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponse(
        String sourceAccountNumber,
        String targetAccountNumber,
        BigDecimal amount,
        TransactionStatus status,
        LocalDateTime createAt
) {
}
