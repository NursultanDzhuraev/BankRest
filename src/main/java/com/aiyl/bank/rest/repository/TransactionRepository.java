package com.aiyl.bank.rest.repository;

import com.aiyl.bank.rest.entity.Account;
import com.aiyl.bank.rest.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select a from Account a where a.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumber(@Param("accountNumber") String accountNumber);

    @Query("""
            select t from Transaction t
                    left join fetch t.sourceAccount
                    left join fetch t.targetAccount
                    where (t.sourceAccount.accountNumber = :accountNumber
                            or t.targetAccount.accountNumber = :accountNumber)
                       and (:from is null or t.createTime >= :from)
                       and (:to is null or t.createTime <= :to)
                    order by t.createTime desc
            """)
    Page<Transaction> findStatementByAccountNumber(
            @Param("accountNumber") String accountNumber,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable);

}
