package com.aiyl.bank.rest.service;

import com.aiyl.bank.rest.dto.response.TransferResponse;
import com.aiyl.bank.rest.dto.request.TransferRequest;
import jakarta.validation.Valid;

public interface TransactionService {
    TransferResponse transferBetweenAccounts(@Valid TransferRequest transferRequest);
}
