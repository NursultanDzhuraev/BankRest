package com.aiyl.bank.rest.service.impl;

import com.aiyl.bank.rest.dto.response.TransferResponse;
import com.aiyl.bank.rest.dto.request.TransferRequest;
import com.aiyl.bank.rest.enums.AccountStatus;
import com.aiyl.bank.rest.enums.TransactionStatus;
import com.aiyl.bank.rest.enums.TransactionType;
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

            Transaction transfer = saveTransaction(source, target, transferRequest.amount(),
                    TransactionType.TRANSFER_OUT, TransactionStatus.SUCCESS,
                    source.getBalance(), null);

            saveTransaction(target, source, transferRequest.amount(), TransactionType.TRANSFER_IN,
                    TransactionStatus.SUCCESS, target.getBalance(), null);

            log.info("Transfer created successfully: {}", transfer.getId());

            return mapToResponse(transfer, source, target);

        } catch (AccountNotFoundException | InsufficientSumException e) {
            log.warn("Transfer failed: {}", e.getMessage());
            saveFiledTransaction(source, target, transferRequest.amount(), e.getMessage());
            throw e;
        }
    }

    private Account findAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new AccountNotFoundException(accountNumber));
    }

    private void validateAccountActive(Account account) {
        if (!account.getAccountStatus().equals(AccountStatus.ACTIVE)) {
            throw new AccountNotActiveException(account.getAccountNumber(), account.getAccountStatus());
        }
    }

    private void validateSufficientSum(Account account, BigDecimal amount) {
        if (!account.hasSufficientSum(amount)) {
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

    private Transaction saveTransaction(Account primary, Account counterparty,
                                        BigDecimal amount, TransactionType type, TransactionStatus status,
                                        BigDecimal balanceAfter, String failureReason) {
        Transaction transfer = Transaction.builder()
                .sourceAccount(type == TransactionType.TRANSFER_OUT ? primary : counterparty)
                .targetAccount(type == TransactionType.TRANSFER_IN ? primary : counterparty)
                .amount(amount)
                .type(type)
                .status(status)
                .balanceAfter(balanceAfter)
                .failureReason(failureReason)
                .build();
        return transactionRepository.save(transfer);
    }

    private void saveFiledTransaction(Account source, Account target, BigDecimal amount, String message) {
        try {
            Transaction failed = Transaction.builder()
                    .sourceAccount(source)
                    .targetAccount(target)
                    .amount(amount)
                    .type(TransactionType.TRANSFER_IN)
                    .status(TransactionStatus.FAILED)
                    .balanceAfter(source.getBalance())
                    .failureReason(message)
                    .build();
            transactionRepository.save(failed);
            log.debug("Failed transaction saved");
        } catch (Exception e) {
            log.error("Failed transaction not saved: {}", e.getMessage());
        }
    }

}
