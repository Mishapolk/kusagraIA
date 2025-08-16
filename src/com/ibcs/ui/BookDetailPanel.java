package com.ibcs.ui;

import com.ibcs.db.BookmarkDatabase;
import com.ibcs.db.RatingDatabase;
import com.ibcs.model.Book;
import com.ibcs.model.User;

import javax.swing.*;
import java.awt.BorderLayout;
import java.io.IOException;

public class BookDetailPanel extends JPanel {
    public BookDetailPanel(MainFrame frame, Book book, RatingDatabase ratingDb, User user, BookmarkDatabase bookmarkDb) {
        setLayout(new BorderLayout());
        JTextArea info = new JTextArea(book.getTitle() + "\n" + book.getAuthor() + "\n" + book.getGenre() + "\n" + book.getPageCount() + " pages\n" + book.getRating() + " rating\n" + book.getLanguage());
        info.setEditable(false);
        add(info, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton backBtn = new JButton("Back");
        JButton bookmarkBtn = new JButton("Bookmark");
        bottom.add(backBtn); bottom.add(bookmarkBtn);

        JSpinner ratingSpinner = new JSpinner(new SpinnerNumberModel(5,1,5,1));
        JTextField commentField = new JTextField(15);
        JButton rateBtn = new JButton("Rate");
        bottom.add(new JLabel("Rate:")); bottom.add(ratingSpinner);
        bottom.add(commentField); bottom.add(rateBtn);
        add(bottom, BorderLayout.SOUTH);

        JTextArea reviews = new JTextArea();
        reviews.setEditable(false);
        add(new JScrollPane(reviews), BorderLayout.EAST);
        refreshReviews(ratingDb, book, reviews);

        backBtn.addActionListener(e -> frame.showHome());
        bookmarkBtn.addActionListener(e -> {
            try {
                bookmarkDb.add(user.getId(), book.getId());
                JOptionPane.showMessageDialog(this, "Book bookmarked");
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
}
