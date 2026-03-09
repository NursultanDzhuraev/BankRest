package com.aiyl.bank.rest.entity;

import com.aiyl.bank.rest.enam.TransactionStatus;
import com.aiyl.bank.rest.enam.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(generator = "transaction_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "transaction_gen", sequenceName = "transaction_seq", allocationSize = 1, initialValue = 100)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(name = "balance_after", precision = 19, scale = 2)
    private BigDecimal balanceAfter;

    @Column(name = "failure_reason" , length = 500)
    private String failureReason;

    @Column(name = "create_time", nullable = false,updatable = false)
    private LocalDateTime createTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_account_id")
    private Account targetAccount;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
    }
}
