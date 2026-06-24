package com.example.demo.Repository;

import com.example.demo.Config.DBConnection;
import com.example.demo.Model.AuditLog;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AuditLogRepository {

    private final DBConnection dbConnection;

    public AuditLogRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void save(AuditLog log) throws SQLException {
        String sql = "INSERT INTO AuditLog (CustomerId, Action, IpAddress, UserAgent) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, log.getCustomerId());
            stmt.setString(2, log.getAction());
            stmt.setString(3, log.getIpAddress());
            stmt.setString(4, log.getUserAgent());
            stmt.executeUpdate();
        }
    }

    public AuditLog findById(BigInteger logId) throws SQLException {
        String sql = "SELECT * FROM AuditLog WHERE LogId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, logId.longValue());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public List<AuditLog> findByCustomerId(int customerId) throws SQLException {
        String sql = "SELECT * FROM AuditLog WHERE CustomerId = ? ORDER BY CreatedAt DESC";
        List<AuditLog> results = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    private AuditLog mapRow(ResultSet rs) throws SQLException {
        AuditLog log = new AuditLog();
        log.setLogId(BigInteger.valueOf(rs.getLong("LogId")));
        log.setCustomerId(rs.getInt("CustomerId"));
        log.setAction(rs.getString("Action"));
        log.setIpAddress(rs.getString("IpAddress"));
        log.setUserAgent(rs.getString("UserAgent"));

        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            log.setCreatedAt(createdAt.toLocalDateTime());
        }

        return log;
    }
}