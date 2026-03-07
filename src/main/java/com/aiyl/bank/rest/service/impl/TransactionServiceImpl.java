package com.aiyl.bank.rest.service.impl;

import com.aiyl.bank.rest.dto.response.TransferResponse;
import com.aiyl.bank.rest.dto.request.TransferRequest;
import com.aiyl.bank.rest.enam.AccountStatus;
import com.aiyl.bank.rest.enam.TransactionStatus;
import com.aiyl.bank.rest.entity.Account;
import com.aiyl.bank.rest.entity.Transaction;
import com.aiyl.bank.rest.exception.AccountNotActiveException;
import com.aiyl.bank.rest.exception.InsufficientSumException;
import com.aiyl.bank.rest.exception.SameAccountException;
import com.aiyl.bank.rest.exception.AccountNotFoundException;
import com.aiyl.bank.rest.repository.AccountRepository;
import com.aiyl.bank.rest.repository.TransactionRepository;
import com.aiyl.bank.rest.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public TransferResponse transferBetweenAccounts(TransferRequest transferRequest) {
        if (transferRequest.sourceAccountNumber().equals(transferRequest.targetAccountNumber())) {
            throw new SameAccountException();
        }
        Account source = findAccountByAccountNumber(transferRequest.sourceAccountNumber());
        Account target = findAccountByAccountNumber(transferRequest.targetAccountNumber());

        try {
            validateAccountActive(source);
            validateAccountActive(target);
            validateSufficientSum(source, transferRequest.amount());

            source.setBalance(source.getBalance().subtract(transferRequest.amount()));
            target.setBalance(target.getBalance().add(transferRequest.amount()));
            accountRepository.save(source);
            accountRepository.save(target);
            log.debug("Balance updated: source={}, target={}", source.getBalance(), target.getBalance());

            Transaction transfer = new Transaction();
            transfer.setSourceAccount(source);
            transfer.setTargetAccount(target);
            transfer.setAmount(transferRequest.amount());
            transfer.setStatus(TransactionStatus.SUCCESS);
            transactionRepository.save(transfer);
            log.info("Transfer created successfully: {}", transfer.getId());

            return mapToResponse(transfer, source, target);

        } catch (AccountNotFoundException | InsufficientSumException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    private Account findAccountByAccountNumber(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new AccountNotFoundException(accountNumber));
    }

    private void validateAccountActive(Account account) {
        if (!account.getAccountStatus().equals(AccountStatus.ACTIVE)) {
            throw new AccountNotActiveException(account.getAccountNumber(), account.getAccountStatus());
        }
    }

    private void validateSufficientSum(Account account, BigDecimal amount) {
        if (account.hasSufficientSum(amount)) {
            throw new InsufficientSumException(account.getAccountNumber(), account.getBalance(), amount);
        }
    }

    private TransferResponse mapToResponse(Transaction transaction, Account source, Account target) {
        return new TransferResponse(
                transaction.getId(),
                source.getAccountNumber(),
                target.getAccountNumber(),
                transaction.getAmount(),
                transaction.getStatus(),
                source.getBalance(),
                transaction.getCreateTime());
    }

}
