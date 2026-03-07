package com.aiyl.bank.rest.exception;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends ApiException {
    public AccountNotFoundException(String accountNumber) {
        super("Account not found: " + accountNumber, HttpStatus.NOT_FOUND);
    }
}
