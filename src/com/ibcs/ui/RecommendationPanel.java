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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RecommendationPanel extends JPanel {
    public RecommendationPanel(MainFrame frame, BookDatabase bookDb, BookmarkDatabase bookmarkDb, User user) {
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        top.setBackground(new Color(0x181818));
        top.setBorder(new EmptyBorder(0,20,0,20));
        String[] genres = {"Fiction","Mystery","Sci-Fi","Non-Fiction","Fantasy"};
        JComboBox<String> genreBox = new JComboBox<>(genres);
        JTextField pagesField = new JTextField(5);
        JButton getBtn = new JButton("Get Recommendations");
        JButton backBtn = new JButton("Back");
        top.add(new JLabel("Genre")); top.add(genreBox);
        top.add(new JLabel("Max Pages")); top.add(pagesField);
        top.add(getBtn); top.add(backBtn);
        add(top, BorderLayout.NORTH);

        DefaultListModel<Book> model = new DefaultListModel<>();
        JList<Book> list = new JList<>(model);
        list.setBackground(new Color(0x121212));
        list.setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(new EmptyBorder(20,20,20,20));
        add(scroll, BorderLayout.CENTER);

        getBtn.addActionListener(e -> {
            model.clear();
            String genre = (String)genreBox.getSelectedItem();
            int maxPages;
            try {
                maxPages = Integer.parseInt(pagesField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid page number");
                return;
            }
            int finalMax = maxPages;
            List<Book> filtered = bookDb.getAll().stream()
                    .filter(b -> b.getGenre().equalsIgnoreCase(genre) && b.getPageCount() <= finalMax)
                    .collect(Collectors.toList());
            Collections.shuffle(filtered);
            for (int i=0; i<Math.min(5, filtered.size()); i++) model.addElement(filtered.get(i));
        });

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Book b = list.getSelectedValue();
                if (b != null) frame.showBookDetail(b);
            }
        });
        backBtn.addActionListener(e -> frame.showHome());
    }
}
