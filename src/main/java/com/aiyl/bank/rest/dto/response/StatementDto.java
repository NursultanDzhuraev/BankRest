package com.aiyl.bank.rest.dto.response;

import com.aiyl.bank.rest.enam.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StatementDto(
        Long transactionId,
        LocalDateTime operationDate,
        BigDecimal amount,
        TransactionStatus status
) {
}
