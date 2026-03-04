package com.aiyl.bank.rest.exception;

import org.springframework.http.HttpStatus;

public class SameAccountException extends ApiException {
    public SameAccountException() {
        super("Одинаковые счета", HttpStatus.BAD_REQUEST);
    }
}
