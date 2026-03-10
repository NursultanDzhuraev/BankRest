package com.aiyl.bank.rest.service.impl;

import com.aiyl.bank.rest.dto.request.TransferRequest;
import com.aiyl.bank.rest.dto.response.TransferResponse;
import com.aiyl.bank.rest.entity.Account;
import com.aiyl.bank.rest.entity.Transaction;
import com.aiyl.bank.rest.enums.AccountStatus;
import com.aiyl.bank.rest.enums.TransactionStatus;
import com.aiyl.bank.rest.exception.AccountNotActiveException;
import com.aiyl.bank.rest.exception.AccountNotFoundException;
import com.aiyl.bank.rest.exception.InsufficientSumException;
import com.aiyl.bank.rest.exception.SameAccountException;
import com.aiyl.bank.rest.repository.AccountRepository;
import com.aiyl.bank.rest.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Transfer service unit tests")
public class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account source;
    private Account target;

    @BeforeEach
    void setUp() {
        source = Account.builder()
                .id(1L)
                .accountNumber("1000000000000001")
                .ownerName("Asan")
                .balance(new BigDecimal("10000.00"))
                .accountStatus(AccountStatus.ACTIVE)
                .build();

        target = Account.builder()
                .id(2L)
                .accountNumber("1000000000000002")
                .ownerName("Yson")
                .balance(new BigDecimal("5000.00"))
                .accountStatus(AccountStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Successful transfer")
    void transfer_success() {
       when(accountRepository.findByAccountNumber("1000000000000001")).thenReturn(Optional.of(source));
       when(accountRepository.findByAccountNumber("1000000000000002")).thenReturn(Optional.of(target));

        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction tx = invocation.getArgument(0);
            return Transaction.builder()
                    .id(1L)
                    .sourceAccount(tx.getSourceAccount())
                    .targetAccount(tx.getTargetAccount())
                    .amount(tx.getAmount())
                    .type(tx.getType())
                    .status(tx.getStatus())
                    .balanceAfter(tx.getBalanceAfter())
                    .failureReason(tx.getFailureReason())
                    .build();
        });

        TransferRequest request = new TransferRequest(
                "1000000000000001", "1000000000000002", BigDecimal.TEN);

        TransferResponse response = transactionService.transferBetweenAccounts(request);

        assertThat(response.status()).isEqualTo(TransactionStatus.SUCCESS);
        assertThat(response.sourceAccountNumber()).isEqualTo("1000000000000001");
        assertThat(response.targetAccountNumber()).isEqualTo("1000000000000002");
        assertThat(response.amount()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(response.sourceBalanceAfter()).isEqualByComparingTo(new BigDecimal("9990.00"));

        assertThat(source.getBalance()).isEqualByComparingTo(new BigDecimal("9990.00"));
        assertThat(target.getBalance()).isEqualByComparingTo(new BigDecimal("5010.00"));

        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Throws SameAccountTransferException for identical accounts")
    void transfer_sameAccount_throws() {
        TransferRequest request = new TransferRequest(
                "1000000000000001", "1000000000000001", BigDecimal.TEN);

        assertThatThrownBy(() -> transactionService.transferBetweenAccounts(request))
                .isInstanceOf(SameAccountException.class);

        verifyNoInteractions(accountRepository);
    }

    @Test
    @DisplayName("Throws AccountNotFoundException when source does not exist")
    void transfer_sourceNotFound_throws() {
        when(accountRepository.findByAccountNumber("1000000000000009")).thenReturn(Optional.empty());

        TransferRequest request = new TransferRequest(
                "1000000000000009", "1000000000000002", BigDecimal.TEN);

        assertThatThrownBy(() -> transactionService.transferBetweenAccounts(request))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("1000000000000009");
    }

    @Test
    @DisplayName("Throws InsufficientFundsException when balance is too low")
    void transfer_insufficientFunds_throws() {
        when(accountRepository.findByAccountNumber("1000000000000001")).thenReturn(Optional.of(source));
        when(accountRepository.findByAccountNumber("1000000000000002")).thenReturn(Optional.of(target));
        when(transactionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TransferRequest request = new TransferRequest(
                "1000000000000001", "1000000000000002", new BigDecimal("99999.00"));

        assertThatThrownBy(() -> transactionService.transferBetweenAccounts(request))
                .isInstanceOf(InsufficientSumException.class);
    }

    @Test
    @DisplayName("Throws AccountNotActiveException for blocked account")
    void transfer_blockedAccount_throws() {
        source.setAccountStatus(AccountStatus.BLOCKED);
        when(accountRepository.findByAccountNumber("1000000000000001")).thenReturn(Optional.of(source));
        when(accountRepository.findByAccountNumber("1000000000000002")).thenReturn(Optional.of(target));

        TransferRequest request = new TransferRequest(
                "1000000000000001", "1000000000000002", BigDecimal.TEN);

        assertThatThrownBy(() -> transactionService.transferBetweenAccounts(request))
                .isInstanceOf(AccountNotActiveException.class)
                .hasMessageContaining("1000000000000001");

        verify(accountRepository, never()).save(any(Account.class));
    }

}