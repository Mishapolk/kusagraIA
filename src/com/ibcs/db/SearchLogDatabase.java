package com.ibcs.db;

import java.sql.*;
import java.time.Instant;

public class SearchLogDatabase {
    private final Connection conn;

    public SearchLogDatabase() throws SQLException {
        this.conn = DatabaseManager.getConnection();
    }

    public void log(String userId, String query) throws SQLException {
        String sql = "INSERT INTO search_log(user_id,query,search_time) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, query);
            ps.setLong(3, Instant.now().toEpochMilli());
            ps.executeUpdate();
        }
    }
}
