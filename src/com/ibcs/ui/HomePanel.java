package com.ibcs.ui;

import com.ibcs.db.BookDatabase;
import com.ibcs.db.BookmarkDatabase;
import com.ibcs.model.Book;
import com.ibcs.model.User;
import com.ibcs.ui.BookCard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.util.Collections;
import java.util.List;

public class HomePanel extends JPanel {
    public HomePanel(MainFrame frame, BookDatabase bookDb, BookmarkDatabase bookmarkDb, User user) {
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        top.setBackground(new Color(0x181818));
        top.setBorder(new EmptyBorder(0,20,0,20));
        JButton searchBtn = new JButton("Search");
        JButton recommendBtn = new JButton("Recommend");
        JButton bookmarkBtn = new JButton("Bookmarks");
        JButton profileBtn = new JButton("Profile");
        JButton logoutBtn = new JButton("Logout");
        top.add(searchBtn);
        top.add(recommendBtn);
        top.add(bookmarkBtn);
        top.add(profileBtn);
        if (user.isAdmin()) {
            JButton adminBtn = new JButton("Admin");
            top.add(adminBtn);
            adminBtn.addActionListener(e -> frame.showAdmin());
        }
        top.add(logoutBtn);
        add(top, BorderLayout.NORTH);

        JPanel booksPanel = new JPanel(new GridLayout(0,3,10,10));
        booksPanel.setBorder(new EmptyBorder(20,20,20,20));
        booksPanel.setOpaque(false);
        JScrollPane scroll = new JScrollPane(booksPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        List<Book> all = bookDb.getAll();
        Collections.shuffle(all);
        for (int i=0; i<Math.min(3, all.size()); i++) {
            Book b = all.get(i);
            booksPanel.add(new BookCard(b, () -> frame.showBookDetail(b)));
        }

        searchBtn.addActionListener(e -> frame.showSearch());
        recommendBtn.addActionListener(e -> frame.showRecommendations());
        bookmarkBtn.addActionListener(e -> frame.showBookmarks());
        profileBtn.addActionListener(e -> frame.showProfile());
        logoutBtn.addActionListener(e -> frame.logout());
    }
}
