package com.ibcs.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingDatabase {
    private final Connection conn;

    public RatingDatabase() throws SQLException {
        this.conn = DatabaseManager.getConnection();
    }

    public void add(String userId, String bookId, int rating, String comment) throws SQLException {
        String sql = "INSERT OR REPLACE INTO ratings(user_id,book_id,rating,comment) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, bookId);
            ps.setInt(3, rating);
            ps.setString(4, comment);
            ps.executeUpdate();
        }
    }

    public List<String[]> getRatingsForBook(String bookId) throws SQLException {
        List<String[]> res = new ArrayList<>();
        String sql = "SELECT user_id, book_id, rating, comment FROM ratings WHERE book_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    res.add(new String[]{rs.getString(1), rs.getString(2), String.valueOf(rs.getInt(3)), rs.getString(4)});
                }
            }
        }
        return res;
    }
}
