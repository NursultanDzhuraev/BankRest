package com.aiyl.bank.rest.exception;

import com.aiyl.bank.rest.enam.AccountStatus;
import org.springframework.http.HttpStatus;

public class AccountNotActiveException extends ApiException {
    public AccountNotActiveException(String accountNumber, AccountStatus accountStatus) {
        super(String.format("Account %s is not active (current status: %s)", accountNumber, accountStatus),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
