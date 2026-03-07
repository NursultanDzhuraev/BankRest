package com.aiyl.bank.rest.service.impl;

import com.aiyl.bank.rest.dto.response.PaginationResponse;
import com.aiyl.bank.rest.dto.response.StatementDto;
import com.aiyl.bank.rest.entity.Transaction;
import com.aiyl.bank.rest.exception.AccountNotFoundException;
import com.aiyl.bank.rest.repository.AccountRepository;
import com.aiyl.bank.rest.repository.TransactionRepository;
import com.aiyl.bank.rest.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public PaginationResponse<StatementDto> getStatement(
            String accountNumber, LocalDate from, LocalDate to, int pageNumber, int pageSize) {
        if (!accountRepository.existsByAccountNumber(accountNumber)) {
            throw new AccountNotFoundException(accountNumber);
        }
        LocalDateTime fromDateTime = from != null ? from.atStartOfDay() : null;
        LocalDateTime toDateTime = to != null ? to.atTime(LocalTime.MAX) : null;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Transaction> transactionPage = transactionRepository.findStatementByAccountNumber(
                accountNumber, fromDateTime, toDateTime, pageable);
        Page<StatementDto> dtoPage = transactionPage.map(this::mapToDto);

        log.debug("Statement fetched :{} records (page{}/{}"
                , dtoPage.getNumberOfElements(), pageNumber + 1, dtoPage.getTotalPages());
        return PaginationResponse.of(dtoPage);

    }

    private StatementDto mapToDto(Transaction transaction) {
        return new StatementDto(
                transaction.getId(),
                transaction.getCreateTime(),
                transaction.getAmount(),
                transaction.getStatus());
    }

}
