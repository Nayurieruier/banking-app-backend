package com.example.demo.DTO;

import java.math.BigDecimal;

public class TransferRequest {
    private long fromAccountId;
    private long toAccountId;
    private BigDecimal amount;
    private String description;

    public TransferRequest() {}

    public long getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(long fromAccountId) { this.fromAccountId = fromAccountId; }

    public long getToAccountId() { return toAccountId; }
    public void setToAccountId(long toAccountId) { this.toAccountId = toAccountId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}