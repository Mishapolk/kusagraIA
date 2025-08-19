package com.ibcs.ui;

import com.ibcs.db.BookDatabase;
import com.ibcs.db.BookmarkDatabase;
import com.ibcs.model.Book;
import com.ibcs.model.User;
import com.ibcs.ui.BookCard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
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

        JPanel grid = new JPanel(new GridLayout(0,3,10,10));
        grid.setBorder(new EmptyBorder(20,20,20,20));
        grid.setOpaque(false);
        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        getBtn.addActionListener(e -> {
            grid.removeAll();
            String genre = (String)genreBox.getSelectedItem();
            int maxPages;
            try {
                maxPages = Integer.parseInt(pagesField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid page number");
                return;
            }
            int finalMax = maxPages;
            try {
                List<Book> filtered = bookDb.getAll().stream()
                        .filter(b -> b.getGenre().equalsIgnoreCase(genre) && b.getPageCount() <= finalMax)
                        .collect(Collectors.toList());
                Collections.shuffle(filtered);
                for (int i=0; i<Math.min(5, filtered.size()); i++) {
                    Book b = filtered.get(i);
                    grid.add(new BookCard(b, () -> frame.showBookDetail(b)));
                }
                grid.revalidate();
                grid.repaint();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        backBtn.addActionListener(e -> frame.showHome());
    }
}
