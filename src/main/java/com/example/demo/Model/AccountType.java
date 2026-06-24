package com.example.demo.Model;

import java.math.BigDecimal;

public class AccountType {
    private int accountTypeId;
    private String typeName;
    private BigDecimal interestRate;
    private BigDecimal minimumBalance;

    public AccountType() {}

    public int getAccountTypeId() { return accountTypeId; }
    public void setAccountTypeId(int accountTypeId) { this.accountTypeId = accountTypeId; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public BigDecimal getMinimumBalance() { return minimumBalance; }
    public void setMinimumBalance(BigDecimal minimumBalance) { this.minimumBalance = minimumBalance; }
}