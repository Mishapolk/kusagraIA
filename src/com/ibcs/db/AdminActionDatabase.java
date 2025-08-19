package com.ibcs.db;

import java.sql.*;
import java.time.Instant;

public class AdminActionDatabase {
    private final Connection conn;

    public AdminActionDatabase() throws SQLException {
        this.conn = DatabaseManager.getConnection();
    }

    public void log(String adminId, String bookId, String action) throws SQLException {
        String sql = "INSERT INTO admin_actions(admin_id,book_id,action,timestamp) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, adminId);
            ps.setString(2, bookId);
            ps.setString(3, action);
            ps.setLong(4, Instant.now().toEpochMilli());
            ps.executeUpdate();
        }
    }
}
