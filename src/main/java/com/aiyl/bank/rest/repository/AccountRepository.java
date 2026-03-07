package com.aiyl.bank.rest.repository;

import com.aiyl.bank.rest.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
    boolean existsByAccountNumber(String accountNumber);
}
