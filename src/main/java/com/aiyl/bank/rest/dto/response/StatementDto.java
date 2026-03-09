package com.aiyl.bank.rest.dto.response;

import com.aiyl.bank.rest.enums.TransactionStatus;
import com.aiyl.bank.rest.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StatementDto(
        Long transactionId,
        LocalDateTime operationDate,
        TransactionType type,
        BigDecimal amount,
        BigDecimal balanceAfter,
        TransactionStatus status
) {
}
