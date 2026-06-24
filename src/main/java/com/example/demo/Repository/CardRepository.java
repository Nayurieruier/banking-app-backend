package com.example.demo.Repository;

import com.example.demo.Config.DBConnection;
import com.example.demo.Model.Cards;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CardRepository {

    private final DBConnection dbConnection;

    public CardRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public long save(Cards card) throws SQLException {
        String sql = "INSERT INTO Cards (AccountId, CardNumberHash, CardLastFour, CardType, ExpiryDate, CvvHash, Status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, card.getAccountId());
            stmt.setString(2, card.getCardNumberHash());
            stmt.setString(3, card.getCardLastFour());
            stmt.setString(4, card.getCardType());
            stmt.setDate(5, Date.valueOf(card.getExpiryDate()));
            stmt.setString(6, card.getCvvHash());
            stmt.setString(7, card.getStatus());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        }
        return -1;
    }

    public Cards findById(long cardId) throws SQLException {
        String sql = "SELECT * FROM Cards WHERE CardId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, cardId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public List<Cards> findByAccountId(long accountId) throws SQLException {
        String sql = "SELECT * FROM Cards WHERE AccountId = ?";
        List<Cards> results = new ArrayList<>();

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

    public void updateStatus(long cardId, String status) throws SQLException {
        String sql = "UPDATE Cards SET Status = ? WHERE CardId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setLong(2, cardId);
            stmt.executeUpdate();
        }
    }

    private Cards mapRow(ResultSet rs) throws SQLException {
        Cards card = new Cards();
        card.setCardId(rs.getLong("CardId"));
        card.setAccountId(rs.getLong("AccountId"));
        card.setCardNumberHash(rs.getString("CardNumberHash"));
        card.setCardLastFour(rs.getString("CardLastFour"));
        card.setCardType(rs.getString("CardType"));

        Date expiryDate = rs.getDate("ExpiryDate");
        if (expiryDate != null) {
            card.setExpiryDate(expiryDate.toLocalDate());
        }

        card.setCvvHash(rs.getString("CvvHash"));
        card.setStatus(rs.getString("Status"));

        Timestamp issuedAt = rs.getTimestamp("IssuedAt");
        if (issuedAt != null) {
            card.setIssuedAt(issuedAt.toLocalDateTime());
        }

        return card;
    }
}