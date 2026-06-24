package com.example.demo.Repository;

import com.example.demo.Config.DBConnection;
import com.example.demo.Model.Customers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerRepository{

    private final DBConnection dbConnection;
    private final PasswordEncoder passwordEncoder;

    public CustomerRepository(DBConnection dbConnection, PasswordEncoder passwordEncoder) {
        this.dbConnection = dbConnection;
        this.passwordEncoder = passwordEncoder;
    }

    public int save(Customers customer) throws SQLException {
        String sql = "INSERT INTO Customers (FirstName, LastName, Email, Phone, NationalId, DateOfBirth, Address, PasswordHash) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhone());
            stmt.setString(5, customer.getNationalId());
            stmt.setDate(6, Date.valueOf(customer.getDateOfBirth()));
            stmt.setString(7, customer.getAddress());
            stmt.setString(8, passwordEncoder.encode(customer.getPasswordHash()));

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {
                throw new com.example.demo.Exception.BankingException(
                        "Email, phone, or national ID already in use.", 409);
            }
            throw e;
        }
        return -1;
    }

    // ... rest of the methods unchanged (findById, findByEmail, findAll, update, deactivate, mapRow)

public Customers findById(int customerId) throws SQLException {
    String sql = "SELECT * FROM Customers WHERE CustomerId = ?";

    try (Connection conn = dbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, customerId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return mapRow(rs);
            }
        }
    }
    return null;
}

/** Used for login lookups — email is unique per the schema. */
public Customers findByEmail(String email) throws SQLException {
    String sql = "SELECT * FROM Customers WHERE Email = ?";

    try (Connection conn = dbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, email);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return mapRow(rs);
            }
        }
    }
    return null;
}

public List<Customers> findAll() throws SQLException {
    String sql = "SELECT * FROM Customers ORDER BY LastName, FirstName";
    List<Customers> results = new ArrayList<>();

    try (Connection conn = dbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            results.add(mapRow(rs));
        }
    }
    return results;
}

/** Updates editable profile fields. Does not touch PasswordHash. */
public void update(Customers customer) throws SQLException {
    String sql = "UPDATE Customers SET FirstName = ?, LastName = ?, Email = ?, Phone = ?, Address = ?, UpdatedAt = SYSDATETIME() "
            + "WHERE CustomerId = ?";

    try (Connection conn = dbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, customer.getFirstName());
        stmt.setString(2, customer.getLastName());
        stmt.setString(3, customer.getEmail());
        stmt.setString(4, customer.getPhone());
        stmt.setString(5, customer.getAddress());
        stmt.setInt(6, customer.getCustomerId());
        stmt.executeUpdate();
    }
}

/** Soft-deactivate rather than hard delete — preserves history/audit trail. */
public void deactivate(int customerId) throws SQLException {
    String sql = "UPDATE Customers SET IsActive = 0 WHERE CustomerId = ?";

    try (Connection conn = dbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, customerId);
        stmt.executeUpdate();
    }
}

private Customers mapRow(ResultSet rs) throws SQLException {
    Customers customer = new Customers();
    customer.setCustomerId(rs.getInt("CustomerId"));
    customer.setFirstName(rs.getString("FirstName"));
    customer.setLastName(rs.getString("LastName"));
    customer.setEmail(rs.getString("Email"));
    customer.setPhone(rs.getString("Phone"));
    customer.setNationalId(rs.getString("NationalId"));

    Date dob = rs.getDate("DateOfBirth");
    if (dob != null) {
        customer.setDateOfBirth(dob.toLocalDate());
    }

    customer.setAddress(rs.getString("Address"));
    customer.setPasswordHash(rs.getString("PasswordHash"));

    Timestamp createdAt = rs.getTimestamp("CreatedAt");
    if (createdAt != null) {
        customer.setCreatedAt(createdAt.toLocalDateTime());
    }

    Timestamp updatedAt = rs.getTimestamp("UpdatedAt");
    if (updatedAt != null) {
        customer.setUpdatedAt(updatedAt.toLocalDateTime());
    }

    customer.setActive(rs.getBoolean("IsActive"));
    return customer;
}
}