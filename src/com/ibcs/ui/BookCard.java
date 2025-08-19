package com.ibcs.ui;

import com.ibcs.model.Book;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Small panel showing a book cover with the title underneath.
 * Clicking the card triggers a runnable, typically to open the
 * book detail view.
 */
public class BookCard extends JPanel {
    private final Book book;

    public BookCard(Book book, Runnable onClick) {
        this.book = book;
        setLayout(new BorderLayout(0,5));
        setBackground(new Color(0x1E1E1E));
        setBorder(new EmptyBorder(5,5,5,5));

        JLabel img = new JLabel();
        img.setHorizontalAlignment(SwingConstants.CENTER);
        img.setVerticalAlignment(SwingConstants.CENTER);
        img.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(img, BorderLayout.CENTER);

        // Load and scale the image to whatever size the label ends up with
        img.addComponentListener(new java.awt.event.ComponentAdapter() {
            private boolean loaded = false;
            @Override public void componentResized(java.awt.event.ComponentEvent e) {
                if (!loaded && img.getWidth() > 0 && img.getHeight() > 0) {
                    loaded = true;
                    ImageLoader.load(book.getImageUrl(), img.getWidth(), img.getHeight(), icon -> img.setIcon(icon));
                }
            }
        });

        JLabel title = new JLabel("<html><div style='text-align:center;color:white;'>" + book.getTitle() + "</div></html>", SwingConstants.CENTER);
        add(title, BorderLayout.SOUTH);

        Cursor hand = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        setCursor(hand);
        MouseAdapter click = new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                onClick.run();
            }
        };
        addMouseListener(click);
        img.addMouseListener(click);
        title.addMouseListener(click);
    }

    public Book getBook() { return book; }
}
