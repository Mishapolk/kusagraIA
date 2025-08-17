package com.ibcs.ui;

import com.ibcs.db.BookDatabase;
import com.ibcs.db.BookmarkDatabase;
import com.ibcs.model.Book;
import com.ibcs.model.User;
import com.ibcs.ui.BookCard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.Set;

public class BookmarksPanel extends JPanel {
    public BookmarksPanel(MainFrame frame, BookDatabase bookDb, BookmarkDatabase bookmarkDb, User user) {
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        top.setBackground(new Color(0x181818));
        top.setBorder(new EmptyBorder(0,20,0,20));
        JButton backBtn = new JButton("Back");
        top.add(backBtn);
        add(top, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0,3,10,10));
        grid.setBorder(new EmptyBorder(20,20,20,20));
        grid.setOpaque(false);
        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        refresh(bookDb, bookmarkDb, user, grid, frame);

        backBtn.addActionListener(e -> frame.showHome());
    }

    private void refresh(BookDatabase bookDb, BookmarkDatabase bookmarkDb, User user, JPanel grid, MainFrame frame) {
        grid.removeAll();
        Set<String> ids = bookmarkDb.getBookmarks(user.getId());
        for (String id : ids) {
            bookDb.getById(id).ifPresent(b -> grid.add(new BookCard(b, () -> frame.showBookDetail(b))));
        }
        grid.revalidate();
        grid.repaint();
    }
}
