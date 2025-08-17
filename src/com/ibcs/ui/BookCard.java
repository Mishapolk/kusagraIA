package com.ibcs.ui;

import com.ibcs.model.Book;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

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
        try {
            BufferedImage original = ImageIO.read(new URL(book.getImageUrl()));
            Image scaled = original.getScaledInstance(120, 180, Image.SCALE_SMOOTH);
            img.setIcon(new ImageIcon(scaled));
        } catch (Exception ignored) { }
        add(img, BorderLayout.CENTER);

        JLabel title = new JLabel("<html><div style='text-align:center;color:white;'>" + book.getTitle() + "</div></html>", SwingConstants.CENTER);
        add(title, BorderLayout.SOUTH);

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                onClick.run();
            }
        });
    }

    public Book getBook() { return book; }
}
