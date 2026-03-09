package com.aiyl.bank.rest.entity;

import com.aiyl.bank.rest.enam.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(generator = "account_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "account_gen", sequenceName = "account_seq", allocationSize = 1, initialValue = 100)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false, length = 16)
    private String accountNumber;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }

    public boolean hasSufficientSum(BigDecimal amount) {
        return this.balance.compareTo(amount) >= 0;
    }
}
