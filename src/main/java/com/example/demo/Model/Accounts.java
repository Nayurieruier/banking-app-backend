package com.example.demo.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Accounts {
    private long accountId;
    private int customerId;
    private int branchId;
    private int accountTypeId;
    private String accountNumber;
    private BigDecimal balance;
    private String currency;
    private String status;          // 'ACTIVE', 'FROZEN', 'CLOSED'
    private LocalDateTime openedAt;
    private LocalDateTime closedAt; // nullable

    public Accounts() {}

    public long getAccountId() { return accountId; }
    public void setAccountId(long accountId) { this.accountId = accountId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }

    public int getAccountTypeId() { return accountTypeId; }
    public void setAccountTypeId(int accountTypeId) { this.accountTypeId = accountTypeId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getOpenedAt() { return openedAt; }
    public void setOpenedAt(LocalDateTime openedAt) { this.openedAt = openedAt; }

    public LocalDateTime getClosedAt() { return closedAt; }
    public void setClosedAt(LocalDateTime closedAt) { this.closedAt = closedAt; }
}