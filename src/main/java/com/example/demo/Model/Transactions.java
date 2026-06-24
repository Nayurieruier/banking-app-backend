package com.example.demo.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transactions {
    private long transactionId;
    private long accountId;
    private Long relatedAccountId;   // nullable -> wrapper type
    private String transactionType;  // 'DEPOSIT', 'WITHDRAWAL', etc.
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String description;
    private String referenceNumber;
    private LocalDateTime createdAt;

    public Transactions() {}

    public long getTransactionId() { return transactionId; }
    public void setTransactionId(long transactionId) { this.transactionId = transactionId; }

    public long getAccountId() { return accountId; }
    public void setAccountId(long accountId) { this.accountId = accountId; }

    public Long getRelatedAccountId() { return relatedAccountId; }
    public void setRelatedAccountId(Long relatedAccountId) { this.relatedAccountId = relatedAccountId; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(BigDecimal balanceAfter) { this.balanceAfter = balanceAfter; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}