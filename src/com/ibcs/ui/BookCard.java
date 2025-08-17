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

        final int w = 120, h = 180;
        JLabel img = new JLabel();
        img.setPreferredSize(new Dimension(w, h));
        img.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(img, BorderLayout.CENTER);

        ImageLoader.load(book.getImageUrl(), w, h, icon -> {
            img.setIcon(icon);
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
