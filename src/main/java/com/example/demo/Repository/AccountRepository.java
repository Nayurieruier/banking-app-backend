package com.example.demo.Repository;

import com.example.demo.Config.DBConnection;
import com.example.demo.Exception.BankingException;
import com.example.demo.Model.Accounts;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountRepository {

    private final DBConnection dbConnection;

    public AccountRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /** Insert a new account. Returns the generated AccountId. */
    public long save(Accounts account) throws SQLException {
        String sql = "INSERT INTO Accounts (CustomerId, BranchId, AccountTypeId, AccountNumber, Balance, Currency, Status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, account.getCustomerId());
            stmt.setInt(2, account.getBranchId());
            stmt.setInt(3, account.getAccountTypeId());
            stmt.setString(4, account.getAccountNumber());
            stmt.setBigDecimal(5, account.getBalance());
            stmt.setString(6, account.getCurrency());
            stmt.setString(7, account.getStatus());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        }
        return -1;
    }

    /** Find a single account by its primary key. Returns null if not found. */
    public Accounts findById(long accountId) throws SQLException {
        String sql = "SELECT * FROM Accounts WHERE AccountId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    /** Find all accounts belonging to a given customer. */
    public List<Accounts> findByCustomerId(int customerId) throws SQLException {
        String sql = "SELECT * FROM Accounts WHERE CustomerId = ? ORDER BY OpenedAt DESC";
        List<Accounts> results = new ArrayList<>();

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

    /** Get just the balance for an account — cheaper than fetching the whole row. */
    public BigDecimal getBalance(long accountId) throws SQLException {
        String sql = "SELECT Balance FROM Accounts WHERE AccountId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("Balance");
                }
            }
        }
        return null;
    }

    /** Update mutable account fields (balance, status). */
    public void update(Accounts account) throws SQLException {
        String sql = "UPDATE Accounts SET Balance = ?, Status = ? WHERE AccountId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, account.getBalance());
            stmt.setString(2, account.getStatus());
            stmt.setLong(3, account.getAccountId());
            stmt.executeUpdate();
        }
    }

    /** Calls the TransferFunds stored procedure built earlier. */
    public void transferFunds(long fromAccountId, long toAccountId, BigDecimal amount, String description) throws SQLException {
        String sql = "{CALL TransferFunds (?, ?, ?, ?)}";

        try (Connection conn = dbConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setLong(1, fromAccountId);
            stmt.setLong(2, toAccountId);
            stmt.setBigDecimal(3, amount);
            stmt.setString(4, description);
            stmt.execute();

        } catch (SQLException e) {
            switch (e.getErrorCode()) {
                case 50001:
                    throw new BankingException("Source account not found.", 404);
                case 50002:
                    throw new BankingException("Insufficient funds.", 409);
                default:
                    throw e; // unrecognized SQL errors still propagate as-is
            }
        }
    }

    private Accounts mapRow(ResultSet rs) throws SQLException {
        Accounts account = new Accounts();
        account.setAccountId(rs.getLong("AccountId"));
        account.setCustomerId(rs.getInt("CustomerId"));
        account.setBranchId(rs.getInt("BranchId"));
        account.setAccountTypeId(rs.getInt("AccountTypeId"));
        account.setAccountNumber(rs.getString("AccountNumber"));
        account.setBalance(rs.getBigDecimal("Balance"));
        account.setCurrency(rs.getString("Currency"));
        account.setStatus(rs.getString("Status"));

        Timestamp openedAt = rs.getTimestamp("OpenedAt");
        if (openedAt != null) {
            account.setOpenedAt(openedAt.toLocalDateTime());
        }

        Timestamp closedAt = rs.getTimestamp("ClosedAt");
        if (closedAt != null) {
            account.setClosedAt(closedAt.toLocalDateTime());
        }

        return account;
    }
}