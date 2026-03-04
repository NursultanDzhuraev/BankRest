package com.aiyl.bank.rest.service.impl;

import com.aiyl.bank.rest.dto.response.TransferResponse;
import com.aiyl.bank.rest.dto.request.TransferRequest;
import com.aiyl.bank.rest.exception.SameAccountException;
import com.aiyl.bank.rest.repository.AccountRepository;
import com.aiyl.bank.rest.repository.TransactionRepository;
import com.aiyl.bank.rest.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    public TransferResponse transferBetweenAccounts(TransferRequest transferRequest) {
        if(transferRequest.sourceAccountNumber().equals(transferRequest.targetAccountNumber())){
            throw new SameAccountException();
        }


      return null;
    }
    private void validateAccountNumber(String accountNumber){

    }
}
