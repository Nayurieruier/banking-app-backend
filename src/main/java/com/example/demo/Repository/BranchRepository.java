package com.example.demo.Repository;

import com.example.demo.Config.DBConnection;
import com.example.demo.Model.Branches;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BranchRepository {

    private final DBConnection dbConnection;

    public BranchRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public int save(Branches branch) throws SQLException {
        String sql = "INSERT INTO Branches (BranchName, BranchCode, Address, Phone) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, branch.getBranchName());
            stmt.setString(2, branch.getBranchCode());
            stmt.setString(3, branch.getAddress());
            stmt.setString(4, branch.getPhone());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public Branches findById(int branchId) throws SQLException {
        String sql = "SELECT * FROM Branches WHERE BranchId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, branchId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    /** Lookup tables are usually small enough to just fetch in full — useful for populating a dropdown in the frontend. */
    public List<Branches> findAll() throws SQLException {
        String sql = "SELECT * FROM Branches ORDER BY BranchName";
        List<Branches> results = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(mapRow(rs));
            }
        }
        return results;
    }

    private Branches mapRow(ResultSet rs) throws SQLException {
        Branches branch = new Branches();
        branch.setBranchId(rs.getInt("BranchId"));
        branch.setBranchName(rs.getString("BranchName"));
        branch.setBranchCode(rs.getString("BranchCode"));
        branch.setAddress(rs.getString("Address"));
        branch.setPhone(rs.getString("Phone"));
        return branch;
    }
}