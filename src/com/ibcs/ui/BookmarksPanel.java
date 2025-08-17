package com.ibcs.ui;

import com.ibcs.db.BookDatabase;
import com.ibcs.db.BookmarkDatabase;
import com.ibcs.model.Book;
import com.ibcs.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.Set;

public class BookmarksPanel extends JPanel {
    public BookmarksPanel(MainFrame frame, BookDatabase bookDb, BookmarkDatabase bookmarkDb, User user) {
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        top.setBackground(new Color(0x181818));
        top.setBorder(new EmptyBorder(0,20,0,20));
        JButton backBtn = new JButton("Back");
        JButton removeBtn = new JButton("Remove");
        top.add(backBtn); top.add(removeBtn);
        add(top, BorderLayout.NORTH);

        DefaultListModel<Book> model = new DefaultListModel<>();
        JList<Book> list = new JList<>(model);
        list.setBackground(new Color(0x121212));
        list.setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(new EmptyBorder(20,20,20,20));
        add(scroll, BorderLayout.CENTER);

        refresh(bookDb, bookmarkDb, user, model);

        backBtn.addActionListener(e -> frame.showHome());
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Book b = list.getSelectedValue();
                if (b != null) frame.showBookDetail(b);
            }
        });
        removeBtn.addActionListener(e -> {
            Book b = list.getSelectedValue();
            if (b != null) {
                try {
                    bookmarkDb.remove(user.getId(), b.getId());
                    refresh(bookDb, bookmarkDb, user, model);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void refresh(BookDatabase bookDb, BookmarkDatabase bookmarkDb, User user, DefaultListModel<Book> model) {
        model.clear();
        Set<String> ids = bookmarkDb.getBookmarks(user.getId());
        for (String id : ids) {
            bookDb.getById(id).ifPresent(model::addElement);
        }
    }
}
