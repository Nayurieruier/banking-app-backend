package com.example.demo.Model;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class AuditLog {
    private BigInteger logId;
    private int customerId;
    private String action;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;

    public AuditLog() {
    }

    public AuditLog(BigInteger logId, int customerId, String action, String ipAddress, String userAgent, LocalDateTime createdAt) {
        this.logId = logId;
        this.customerId = customerId;
        this.action = action;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.createdAt = createdAt;
    }

    public BigInteger getLogId() { return logId; }
    public void setLogId(BigInteger logId) { this.logId = logId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}