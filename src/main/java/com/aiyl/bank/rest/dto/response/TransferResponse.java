package com.aiyl.bank.rest.dto.response;

import com.aiyl.bank.rest.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponse(
        Long transactionId,
        String sourceAccountNumber,
        String targetAccountNumber,
        BigDecimal amount,
        TransactionStatus status,
        BigDecimal sourceBalanceAfter,
        LocalDateTime createdAt
) {
}
