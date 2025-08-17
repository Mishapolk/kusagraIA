package com.ibcs.ui;

import com.ibcs.db.BookDatabase;
import com.ibcs.db.BookmarkDatabase;
import com.ibcs.db.SearchLogDatabase;
import com.ibcs.model.Book;
import com.ibcs.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.IOException;
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

        DefaultListModel<Book> model = new DefaultListModel<>();
        JList<Book> list = new JList<>(model);
        list.setBackground(new Color(0x121212));
        list.setForeground(Color.WHITE);
        add(top, BorderLayout.NORTH);
        add(filters, BorderLayout.CENTER);
        add(new JScrollPane(list), BorderLayout.SOUTH);

        backBtn.addActionListener(e -> frame.showHome());

        searchBtn.addActionListener(e -> {
            model.clear();
            List<Book> results;
            String q = searchField.getText();
            if (!q.isBlank()) {
                results = bookDb.search(q);
            } else {
                String genre = (String)genreBox.getSelectedItem();
                if (genre != null && genre.isEmpty()) genre = null;
                String author = authorField.getText();
                if (author.isBlank()) author = null;
                String lang = langField.getText();
                if (lang.isBlank()) lang = null;
                double rating = (double) ratingSpinner.getValue();
                results = bookDb.filter(genre, lang, author, rating);
            }

            String sort = (String) sortBox.getSelectedItem();
            if ("Author".equals(sort)) {
                results.sort(Comparator.comparing(Book::getAuthor));
            } else if ("Page Count".equals(sort)) {
                results.sort(Comparator.comparingInt(Book::getPageCount));
            }

            for (Book b: results) model.addElement(b);

            String logQuery;
            if (!q.isBlank()) {
                logQuery = q;
            } else {
                String genre = (String)genreBox.getSelectedItem();
                String author = authorField.getText();
                String lang = langField.getText();
                double rating = (double) ratingSpinner.getValue();
                logQuery = String.format("genre=%s,author=%s,lang=%s,rating>=%.1f", genre, author, lang, rating);
            }
            try {
                searchLogDb.log(user.getId(), logQuery);
            } catch (IOException ex) {
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
            model.clear();
        });

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Book b = list.getSelectedValue();
                if (b != null) frame.showBookDetail(b);
            }
        });
    }
}
