package com.ibcs.db;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class BookmarkDatabase {
    private final Connection conn;

    public BookmarkDatabase() throws SQLException {
        this.conn = DatabaseManager.getConnection();
    }

    public Set<String> getBookmarks(String userId) throws SQLException {
        Set<String> set = new HashSet<>();
        String sql = "SELECT book_id FROM bookmarks WHERE user_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) set.add(rs.getString(1));
            }
        }
        return set;
    }

    public void add(String userId, String bookId) throws SQLException {
        String sql = "INSERT OR IGNORE INTO bookmarks(user_id, book_id) VALUES (?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, bookId);
            ps.executeUpdate();
        }
    }

    public void remove(String userId, String bookId) throws SQLException {
        String sql = "DELETE FROM bookmarks WHERE user_id=? AND book_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, bookId);
            ps.executeUpdate();
        }
    }
}
