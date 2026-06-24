package com.example.demo.Repository;

import com.example.demo.Config.DBConnection;
import com.example.demo.Model.Transactions;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepository {

    private final DBConnection dbConnection;

    public TransactionRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Transactions are typically created by TransferFunds (stored procedure)
     * or by deposit/withdrawal logic — this method covers the latter case,
     * e.g. a teller deposit that doesn't go through the transfer procedure.
     */
    public long save(Transactions txn) throws SQLException {
        String sql = "INSERT INTO Transactions (AccountId, RelatedAccountId, TransactionType, Amount, BalanceAfter, Description, ReferenceNumber) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, txn.getAccountId());

            if (txn.getRelatedAccountId() != null) {
                stmt.setLong(2, txn.getRelatedAccountId());
            } else {
                stmt.setNull(2, Types.BIGINT);
            }

            stmt.setString(3, txn.getTransactionType());
            stmt.setBigDecimal(4, txn.getAmount());
            stmt.setBigDecimal(5, txn.getBalanceAfter());
            stmt.setString(6, txn.getDescription());
            stmt.setString(7, txn.getReferenceNumber());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        }
        return -1;
    }

    public Transactions findById(long transactionId) throws SQLException {
        String sql = "SELECT * FROM Transactions WHERE TransactionId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    /** Statement history for one account — most recent first. */
    public List<Transactions> findByAccountId(long accountId) throws SQLException {
        String sql = "SELECT * FROM Transactions WHERE AccountId = ? ORDER BY CreatedAt DESC";
        List<Transactions> results = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    /** Same as above, but capped — useful for a dashboard "recent activity" widget. */
    public List<Transactions> findRecentByAccountId(long accountId, int limit) throws SQLException {
        String sql = "SELECT TOP (?) * FROM Transactions WHERE AccountId = ? ORDER BY CreatedAt DESC";
        List<Transactions> results = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setLong(2, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    private Transactions mapRow(ResultSet rs) throws SQLException {
        Transactions txn = new Transactions();
        txn.setTransactionId(rs.getLong("TransactionId"));
        txn.setAccountId(rs.getLong("AccountId"));

        long relatedId = rs.getLong("RelatedAccountId");
        if (!rs.wasNull()) {
            txn.setRelatedAccountId(relatedId);
        }

        txn.setTransactionType(rs.getString("TransactionType"));
        txn.setAmount(rs.getBigDecimal("Amount"));
        txn.setBalanceAfter(rs.getBigDecimal("BalanceAfter"));
        txn.setDescription(rs.getString("Description"));
        txn.setReferenceNumber(rs.getString("ReferenceNumber"));

        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            txn.setCreatedAt(createdAt.toLocalDateTime());
        }

        return txn;
    }
}