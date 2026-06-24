package com.example.demo.Repository;

import com.example.demo.Config.DBConnection;
import com.example.demo.Model.AccountType;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountTypeRepository {

    private final DBConnection dbConnection;

    public AccountTypeRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public int save(AccountType accountType) throws SQLException {
        String sql = "INSERT INTO AccountTypes (TypeName, InterestRate, MinimumBalance) VALUES (?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, accountType.getTypeName());
            stmt.setBigDecimal(2, accountType.getInterestRate());
            stmt.setBigDecimal(3, accountType.getMinimumBalance());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public AccountType findById(int accountTypeId) throws SQLException {
        String sql = "SELECT * FROM AccountTypes WHERE AccountTypeId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountTypeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public List<AccountType> findAll() throws SQLException {
        String sql = "SELECT * FROM AccountTypes ORDER BY TypeName";
        List<AccountType> results = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(mapRow(rs));
            }
        }
        return results;
    }

    private AccountType mapRow(ResultSet rs) throws SQLException {
        AccountType accountType = new AccountType();
        accountType.setAccountTypeId(rs.getInt("AccountTypeId"));
        accountType.setTypeName(rs.getString("TypeName"));
        accountType.setInterestRate(rs.getBigDecimal("InterestRate"));
        accountType.setMinimumBalance(rs.getBigDecimal("MinimumBalance"));
        return accountType;
    }
}