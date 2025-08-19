package com.ibcs.db;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:data/library.db";
    private static Connection conn;

    public static synchronized Connection getConnection() throws SQLException {
        if (conn == null) {
            try {
                java.nio.file.Files.createDirectories(java.nio.file.Paths.get("data"));
            } catch (java.io.IOException ignored) {}
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException ignored) {}
            conn = DriverManager.getConnection(URL);
            initialise();
        }
        return conn;
    }

    private static void initialise() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS users (id TEXT PRIMARY KEY, email TEXT UNIQUE, password TEXT, is_admin INTEGER)");
            st.execute("CREATE TABLE IF NOT EXISTS books (id TEXT PRIMARY KEY, title TEXT, author TEXT, genre TEXT, page_count INTEGER, rating REAL, language TEXT, description TEXT, image_url TEXT)");
            st.execute("CREATE TABLE IF NOT EXISTS bookmarks (user_id TEXT, book_id TEXT, PRIMARY KEY(user_id, book_id))");
            st.execute("CREATE TABLE IF NOT EXISTS ratings (user_id TEXT, book_id TEXT, rating REAL, comment TEXT, PRIMARY KEY(user_id, book_id))");
            st.execute("CREATE TABLE IF NOT EXISTS history (user_id TEXT, book_id TEXT, viewed_at INTEGER)");
            st.execute("CREATE TABLE IF NOT EXISTS admin_actions (admin_id TEXT, book_id TEXT, action TEXT, timestamp INTEGER)");
            st.execute("CREATE TABLE IF NOT EXISTS search_log (user_id TEXT, query TEXT, search_time INTEGER)");
        }

        seedUsers();
        seedBooks();
    }

    private static void seedUsers() throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users")) {
            if (rs.next() && rs.getInt(1) == 0) {
                st.executeUpdate("INSERT INTO users VALUES ('1','alice@example.com','pass123',0)");
                st.executeUpdate("INSERT INTO users VALUES ('2','admin@example.com','adminpass',1)");
            }
        }
    }

    private static void seedBooks() throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM books")) {
            if (rs.next() && rs.getInt(1) == 0) {
                String sql = "INSERT INTO books (id,title,author,genre,page_count,rating,language,description,image_url) VALUES (?,?,?,?,?,?,?,?,?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    for (Object[] b : bookSeedData()) {
                        ps.setString(1, (String) b[0]);
                        ps.setString(2, (String) b[1]);
                        ps.setString(3, (String) b[2]);
                        ps.setString(4, (String) b[3]);
                        ps.setInt(5, (Integer) b[4]);
                        ps.setDouble(6, (Double) b[5]);
                        ps.setString(7, (String) b[6]);
                        ps.setString(8, (String) b[7]);
                        ps.setString(9, (String) b[8]);
                        ps.executeUpdate();
                    }
                }
            }
        }
    }

    private static List<Object[]> bookSeedData() {
        return Arrays.asList(new Object[][]{
            {"1","1984","George Orwell","Sci-Fi",328,4.8,"English","A chilling novel set in a totalitarian world where surveillance is absolute and dissent is crushed.","https://covers.openlibrary.org/b/id/722246-L.jpg"},
            {"2","The Hobbit","J.R.R. Tolkien","Fantasy",310,4.7,"English","Bilbo Baggins enjoys a quiet life until wizards and dwarves whisk him away on a perilous quest.","https://covers.openlibrary.org/b/id/6979861-L.jpg"},
            {"3","To Kill a Mockingbird","Harper Lee","Fiction",281,4.9,"English","Scout Finch witnesses injustice and compassion in the racially divided South.","https://covers.openlibrary.org/b/id/8228691-L.jpg"},
            {"4","The Girl with the Dragon Tattoo","Stieg Larsson","Mystery",465,4.5,"English","A hacker and a journalist investigate the disappearance of a wealthy heiress.","https://covers.openlibrary.org/b/id/8091016-L.jpg"},
            {"5","Brief Answers to the Big Questions","Stephen Hawking","Non-Fiction",256,4.6,"English","Hawking tackles the grandest mysteries of the cosmos with clarity and wit.","https://covers.openlibrary.org/b/id/9255027-L.jpg"},
            {"6","Dune","Frank Herbert","Sci-Fi",412,4.8,"English","On desert planet Arrakis young Paul Atreides discovers his destiny among the Fremen.","https://covers.openlibrary.org/b/id/9251080-L.jpg"},
            {"7","The Name of the Wind","Patrick Rothfuss","Fantasy",662,4.7,"English","Kvothe recounts his youth filled with music, magic and tragedy.","https://covers.openlibrary.org/b/id/8231990-L.jpg"},
            {"8","Sapiens","Yuval Noah Harari","Non-Fiction",498,4.6,"English","The journey of humankind from primitive gatherers to masters of the planet.","https://covers.openlibrary.org/b/id/8167892-L.jpg"},
            {"9","The Da Vinci Code","Dan Brown","Mystery",454,4.4,"English","A symbologist unravels hidden clues in famous artworks after a murder in the Louvre.","https://covers.openlibrary.org/b/id/240726-L.jpg"},
            {"10","The Martian","Andy Weir","Sci-Fi",369,4.8,"English","Stranded on Mars, Mark Watney fights to survive while awaiting rescue.","https://covers.openlibrary.org/b/id/8372586-L.jpg"},
            {"11","Pride and Prejudice","Jane Austen","Fiction",279,4.6,"English","Elizabeth Bennet navigates manners, morality and marriage in Regency England.","https://covers.openlibrary.org/b/id/8091010-L.jpg"},
            {"12","The Alchemist","Paulo Coelho","Fiction",208,4.5,"English","Shepherd boy Santiago pursues a treasure seen in his dreams.","https://covers.openlibrary.org/b/id/8128692-L.jpg"},
            {"13","The Catcher in the Rye","J.D. Salinger","Fiction",214,4.0,"English","Disillusioned teen Holden Caulfield wanders New York City.","https://covers.openlibrary.org/b/id/8231993-L.jpg"},
            {"14","The Great Gatsby","F. Scott Fitzgerald","Fiction",180,4.3,"English","The illusion of the American Dream unravels amid lavish parties.","https://covers.openlibrary.org/b/id/7222276-L.jpg"},
            {"15","Harry Potter and the Sorcerer's Stone","J.K. Rowling","Fantasy",309,4.8,"English","Young Harry Potter discovers he is a wizard and attends Hogwarts.","https://covers.openlibrary.org/b/id/7888783-L.jpg"}
        });
    }
}
