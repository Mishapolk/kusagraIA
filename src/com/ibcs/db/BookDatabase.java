package com.ibcs.db;

import com.ibcs.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDatabase {
    private final Connection conn;

    public BookDatabase() throws SQLException {
        this.conn = DatabaseManager.getConnection();
    }

    private Book map(ResultSet rs) throws SQLException {
        return new Book(
                rs.getString("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("genre"),
                rs.getInt("page_count"),
                rs.getDouble("rating"),
                rs.getString("language"),
                rs.getString("description"),
                rs.getString("image_url"));
    }

    public List<Book> getAll() throws SQLException {
        List<Book> list = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM books")) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public Optional<Book> getById(String id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    public void add(Book b) throws SQLException {
        String sql = "INSERT INTO books (id,title,author,genre,page_count,rating,language,description,image_url) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getId());
            ps.setString(2, b.getTitle());
            ps.setString(3, b.getAuthor());
            ps.setString(4, b.getGenre());
            ps.setInt(5, b.getPageCount());
            ps.setDouble(6, b.getRating());
            ps.setString(7, b.getLanguage());
            ps.setString(8, b.getDescription());
            ps.setString(9, b.getImageUrl());
            ps.executeUpdate();
        }
    }

    public void update(Book b) throws SQLException {
        String sql = "UPDATE books SET title=?, author=?, genre=?, page_count=?, rating=?, language=?, description=?, image_url=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getTitle());
            ps.setString(2, b.getAuthor());
            ps.setString(3, b.getGenre());
            ps.setInt(4, b.getPageCount());
            ps.setDouble(5, b.getRating());
            ps.setString(6, b.getLanguage());
            ps.setString(7, b.getDescription());
            ps.setString(8, b.getImageUrl());
            ps.setString(9, b.getId());
            ps.executeUpdate();
        }
    }

    public void delete(String id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE id=?")) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public List<Book> search(String query) throws SQLException {
        String sql = "SELECT * FROM books WHERE lower(title) LIKE ? OR lower(author) LIKE ? OR lower(genre) LIKE ?";
        String q = "%" + query.toLowerCase() + "%";
        List<Book> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, q);
            ps.setString(2, q);
            ps.setString(3, q);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        }
        return list;
    }

    public List<Book> filter(String genre, String language, String author, double minRating) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT * FROM books WHERE rating >= ?");
        List<Object> params = new ArrayList<>();
        params.add(minRating);
        if (genre != null) { sb.append(" AND lower(genre)=?"); params.add(genre.toLowerCase()); }
        if (language != null) { sb.append(" AND lower(language)=?"); params.add(language.toLowerCase()); }
        if (author != null) { sb.append(" AND lower(author)=?"); params.add(author.toLowerCase()); }
        List<Book> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof String) ps.setString(i + 1, (String)p);
                else if (p instanceof Double) ps.setDouble(i + 1, (Double)p);
            }
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        }
        return list;
    }
}
