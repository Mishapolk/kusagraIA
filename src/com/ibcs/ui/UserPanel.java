package com.ibcs.ui;

import com.ibcs.db.BookDatabase;
import com.ibcs.db.ReadingHistoryDatabase;
import com.ibcs.db.UserDatabase;
import com.ibcs.model.User;
import com.ibcs.ui.BookCard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class UserPanel extends JPanel {
    public UserPanel(MainFrame frame, User user, UserDatabase userDb, ReadingHistoryDatabase historyDb, BookDatabase bookDb) {
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        top.setBackground(new Color(0x181818));
        top.setBorder(new EmptyBorder(0,20,0,20));
        JButton backBtn = new JButton("Back");
        JButton saveBtn = new JButton("Save");
        top.add(backBtn); top.add(saveBtn);
        add(top, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0,2,10,10));
        form.setBorder(new EmptyBorder(20,20,20,20));
        form.setOpaque(false);
        JTextField emailField = new JTextField(user.getEmail());
        JPasswordField passwordField = new JPasswordField(user.getPassword());
        form.add(new JLabel("Email")); form.add(emailField);
        form.add(new JLabel("Password")); form.add(passwordField);
        add(form, BorderLayout.CENTER);

        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(new EmptyBorder(20,20,20,20));
        JLabel historyLabel = new JLabel("History");
        historyLabel.setForeground(Color.WHITE);
        historyPanel.add(historyLabel, BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(0,3,10,10));
        grid.setOpaque(false);
        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        historyPanel.add(scroll, BorderLayout.CENTER);
        add(historyPanel, BorderLayout.SOUTH);
        refreshHistory(historyDb, bookDb, user.getId(), grid, frame);

        backBtn.addActionListener(e -> frame.showHome());
        saveBtn.addActionListener(e -> {
            String email = emailField.getText();
            String pass = new String(passwordField.getPassword());
            if (email.isBlank() || pass.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }
            user.setEmail(email);
            user.setPassword(pass);
            try {
                userDb.update(user);
                JOptionPane.showMessageDialog(this, "Profile updated");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void refreshHistory(ReadingHistoryDatabase historyDb, BookDatabase bookDb, String userId, JPanel grid, MainFrame frame) {
        grid.removeAll();
        try {
            for (String[] h : historyDb.getHistory(userId)) {
                bookDb.getById(h[1]).ifPresent(b -> grid.add(new BookCard(b, () -> frame.showBookDetail(b))));
            }
            grid.revalidate();
            grid.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
