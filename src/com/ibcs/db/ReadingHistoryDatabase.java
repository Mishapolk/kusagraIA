package com.ibcs.db;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ReadingHistoryDatabase {
    private final Connection conn;

    public ReadingHistoryDatabase() throws SQLException {
        this.conn = DatabaseManager.getConnection();
    }

    public void add(String userId, String bookId) throws SQLException {
        String sql = "INSERT INTO history(user_id,book_id,viewed_at) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, bookId);
            ps.setLong(3, Instant.now().toEpochMilli());
            ps.executeUpdate();
        }
    }

    public List<String[]> getHistory(String userId) throws SQLException {
        List<String[]> res = new ArrayList<>();
        String sql = "SELECT user_id, book_id, viewed_at FROM history WHERE user_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    res.add(new String[]{rs.getString(1), rs.getString(2), String.valueOf(rs.getLong(3))});
                }
            }
        }
        return res;
    }
}
