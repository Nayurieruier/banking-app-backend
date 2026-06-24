package com.example.demo.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Cards {
    private long cardId;
    private long accountId;
    private String cardNumberHash;
    private String cardLastFour;
    private String cardType;        // 'DEBIT', 'CREDIT'
    private LocalDate expiryDate;
    private String cvvHash;
    private String status;          // 'ACTIVE', 'BLOCKED', 'EXPIRED'
    private LocalDateTime issuedAt;

    public Cards() {}

    public long getCardId() { return cardId; }
    public void setCardId(long cardId) { this.cardId = cardId; }

    public long getAccountId() { return accountId; }
    public void setAccountId(long accountId) { this.accountId = accountId; }

    public String getCardNumberHash() { return cardNumberHash; }
    public void setCardNumberHash(String cardNumberHash) { this.cardNumberHash = cardNumberHash; }

    public String getCardLastFour() { return cardLastFour; }
    public void setCardLastFour(String cardLastFour) { this.cardLastFour = cardLastFour; }

    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getCvvHash() { return cvvHash; }
    public void setCvvHash(String cvvHash) { this.cvvHash = cvvHash; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }
}