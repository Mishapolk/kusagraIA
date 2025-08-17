package com.ibcs.ui;

import com.ibcs.db.BookmarkDatabase;
import com.ibcs.db.RatingDatabase;
import com.ibcs.model.Book;
import com.ibcs.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class BookDetailPanel extends JPanel {
    public BookDetailPanel(MainFrame frame, Book book, RatingDatabase ratingDb, User user, BookmarkDatabase bookmarkDb) {
        setLayout(new BorderLayout());

        // left column with title, genre, cover, author/page count
        JPanel left = new JPanel();
        left.setLayout(new BorderLayout());
        left.setBorder(new EmptyBorder(20,20,20,20));
        left.setOpaque(false);

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);
        JLabel title = new JLabel(book.getTitle(), SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);
        JLabel genre = new JLabel(book.getGenre(), SwingConstants.CENTER);
        genre.setAlignmentX(Component.CENTER_ALIGNMENT);
        genre.setForeground(Color.LIGHT_GRAY);
        top.add(title);
        top.add(genre);
        left.add(top, BorderLayout.NORTH);

        JLabel cover = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(new java.net.URL(book.getImageUrl()));
            Image scaled = icon.getImage().getScaledInstance(150, 220, Image.SCALE_SMOOTH);
            cover.setIcon(new ImageIcon(scaled));
        } catch (Exception ignored) {}
        left.add(cover, BorderLayout.CENTER);

        JPanel meta = new JPanel(new BorderLayout());
        meta.setOpaque(false);
        JLabel author = new JLabel(book.getAuthor());
        author.setForeground(Color.WHITE);
        JLabel pages = new JLabel(book.getPageCount() + " pages", SwingConstants.RIGHT);
        pages.setForeground(Color.WHITE);
        meta.add(author, BorderLayout.WEST);
        meta.add(pages, BorderLayout.EAST);
        left.add(meta, BorderLayout.SOUTH);
        add(left, BorderLayout.WEST);

        // right column with language, rating, description
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(20,20,20,20));
        right.setBackground(new Color(0x121212));
        JLabel language = new JLabel("Language: " + book.getLanguage());
        language.setForeground(Color.WHITE);
        JLabel ratingLabel = new JLabel("Rating: " + book.getRating());
        ratingLabel.setForeground(Color.WHITE);
        JTextArea desc = new JTextArea(book.getDescription());
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);
        desc.setBackground(new Color(0x121212));
        desc.setForeground(Color.WHITE);
        JScrollPane descScroll = new JScrollPane(desc);
        descScroll.setBorder(null);
        right.add(language);
        right.add(Box.createVerticalStrut(5));
        right.add(ratingLabel);
        right.add(Box.createVerticalStrut(10));
        right.add(descScroll);
        add(right, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        bottom.setBackground(new Color(0x181818));
        JButton backBtn = new JButton("Back");
        JButton bookmarkBtn = new JButton();
        bottom.add(backBtn); bottom.add(bookmarkBtn);

        JSpinner ratingSpinner = new JSpinner(new SpinnerNumberModel(5,1,5,1));
        JTextField commentField = new JTextField(15);
        commentField.putClientProperty("JTextField.placeholderText", "Comment");
        JButton rateBtn = new JButton("Rate");
        bottom.add(new JLabel("Rate:")); bottom.add(ratingSpinner);
        bottom.add(commentField); bottom.add(rateBtn);
        add(bottom, BorderLayout.SOUTH);

        JTextArea reviews = new JTextArea();
        reviews.setEditable(false);
        reviews.setBackground(new Color(0x121212));
        reviews.setForeground(Color.WHITE);
        JScrollPane reviewScroll = new JScrollPane(reviews);
        reviewScroll.setBorder(new EmptyBorder(20,20,20,20));
        add(reviewScroll, BorderLayout.EAST);
        refreshReviews(ratingDb, book, reviews);
        boolean[] bookmarked = {bookmarkDb.getBookmarks(user.getId()).contains(book.getId())};
        updateBookmarkText(bookmarkBtn, bookmarked[0]);

        backBtn.addActionListener(e -> frame.showHome());
        bookmarkBtn.addActionListener(e -> {
            try {
                if (bookmarked[0]) {
                    bookmarkDb.remove(user.getId(), book.getId());
                } else {
                    bookmarkDb.add(user.getId(), book.getId());
                }
                bookmarked[0] = !bookmarked[0];
                updateBookmarkText(bookmarkBtn, bookmarked[0]);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        rateBtn.addActionListener(e -> {
            String comment = commentField.getText();
            if (comment.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter a comment");
                return;
            }
            try {
                ratingDb.add(user.getId(), book.getId(), (int)ratingSpinner.getValue(), comment);
                refreshReviews(ratingDb, book, reviews);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void refreshReviews(RatingDatabase ratingDb, Book book, JTextArea reviews) {
        StringBuilder sb = new StringBuilder();
        for (String[] r : ratingDb.getRatingsForBook(book.getId())) {
            sb.append(r[0]).append(": ").append(r[2]).append(" stars - ").append(r[3]).append("\n");
        }
        reviews.setText(sb.toString());
    }

    private void updateBookmarkText(JButton btn, boolean bookmarked) {
        btn.setText(bookmarked ? "Remove Bookmark" : "Bookmark");
    }
}
