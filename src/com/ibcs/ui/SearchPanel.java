package com.ibcs.ui;

import com.ibcs.db.BookDatabase;
import com.ibcs.db.BookmarkDatabase;
import com.ibcs.db.SearchLogDatabase;
import com.ibcs.model.Book;
import com.ibcs.model.User;
import com.ibcs.ui.BookCard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class SearchPanel extends JPanel {
    private final SearchLogDatabase searchLogDb;

    public SearchPanel(MainFrame frame, BookDatabase bookDb, BookmarkDatabase bookmarkDb, SearchLogDatabase searchLogDb, User user) {
        this.searchLogDb = searchLogDb;
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        top.setBackground(new Color(0x181818));
        top.setBorder(new EmptyBorder(0,20,0,20));
        JTextField searchField = new JTextField(20);
        searchField.putClientProperty("JTextField.placeholderText", "Search by title or author");
        JButton searchBtn = new JButton("Search");
        JButton backBtn = new JButton("Back");
        top.add(searchField);
        top.add(searchBtn);
        top.add(backBtn);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        filters.setBackground(new Color(0x1E1E1E));
        filters.setBorder(new EmptyBorder(10,20,10,20));
        String[] genres = {"","Fiction","Mystery","Sci-Fi","Non-Fiction","Fantasy"};
        JComboBox<String> genreBox = new JComboBox<>(genres);
        JTextField authorField = new JTextField(10);
        authorField.putClientProperty("JTextField.placeholderText", "Author");
        JTextField langField = new JTextField(10);
        langField.putClientProperty("JTextField.placeholderText", "Language");
        JSpinner ratingSpinner = new JSpinner(new SpinnerNumberModel(0.0,0.0,5.0,0.1));
        JComboBox<String> sortBox = new JComboBox<>(new String[]{"None","Author","Page Count"});
        JButton resetBtn = new JButton("Reset");
        filters.add(new JLabel("Genre")); filters.add(genreBox);
        filters.add(authorField);
        filters.add(langField);
        filters.add(new JLabel("Min Rating")); filters.add(ratingSpinner);
        filters.add(new JLabel("Sort")); filters.add(sortBox);
        filters.add(resetBtn);

        JPanel grid = new JPanel(new GridLayout(0,3,10,10));
        grid.setBorder(new EmptyBorder(20,20,20,20));
        grid.setOpaque(false);
        JPanel header = new JPanel(new BorderLayout());
        header.add(top, BorderLayout.NORTH);
        header.add(filters, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        backBtn.addActionListener(e -> frame.showHome());

        searchBtn.addActionListener(e -> {
            grid.removeAll();
            String q = searchField.getText().toLowerCase();
            String genre = (String)genreBox.getSelectedItem();
            if (genre != null && genre.isEmpty()) genre = null;
            String author = authorField.getText();
            if (author.isBlank()) author = null;
            String lang = langField.getText();
            if (lang.isBlank()) lang = null;
            double rating = (double) ratingSpinner.getValue();
            try {
                List<Book> results = bookDb.filter(genre, lang, author, rating);
                if (!q.isBlank()) {
                    results = results.stream().filter(b ->
                            b.getTitle().toLowerCase().contains(q) ||
                            b.getAuthor().toLowerCase().contains(q))
                            .collect(java.util.stream.Collectors.toList());
                }

                String sort = (String) sortBox.getSelectedItem();
                if ("Author".equals(sort)) {
                    results.sort(Comparator.comparing(Book::getAuthor));
                } else if ("Page Count".equals(sort)) {
                    results.sort(Comparator.comparingInt(Book::getPageCount));
                }

                for (Book b: results) {
                    grid.add(new BookCard(b, () -> frame.showBookDetail(b)));
                }
                grid.revalidate();
                grid.repaint();

                String logQuery = String.format("q=%s,genre=%s,author=%s,lang=%s,rating>=%.1f",
                        q, genre, author, lang, rating);
                searchLogDb.log(user.getId(), logQuery);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        resetBtn.addActionListener(e -> {
            searchField.setText("");
            genreBox.setSelectedIndex(0);
            authorField.setText("");
            langField.setText("");
            ratingSpinner.setValue(0.0);
            sortBox.setSelectedIndex(0);
            grid.removeAll();
            grid.revalidate();
            grid.repaint();
        });
    }
}
