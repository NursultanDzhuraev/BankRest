package com.aiyl.bank.rest.repository;

import com.aiyl.bank.rest.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
