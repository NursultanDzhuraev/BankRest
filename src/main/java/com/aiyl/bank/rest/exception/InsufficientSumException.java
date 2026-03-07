package com.aiyl.bank.rest.exception;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class InsufficientSumException extends ApiException {
    public InsufficientSumException(String accountNumber, BigDecimal balance, BigDecimal amount) {
        super(String.format("Insufficient funds on account %s: available %.2f, requested %.2f", accountNumber, balance, amount),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
