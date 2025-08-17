package com.ibcs.ui;

import com.ibcs.db.BookDatabase;
import com.ibcs.db.ReadingHistoryDatabase;
import com.ibcs.db.UserDatabase;
import com.ibcs.model.User;

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
        JTextField usernameField = new JTextField(user.getUsername());
        JPasswordField passwordField = new JPasswordField(user.getPassword());
        form.add(new JLabel("Username")); form.add(usernameField);
        form.add(new JLabel("Password")); form.add(passwordField);
        add(form, BorderLayout.CENTER);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> historyList = new JList<>(model);
        historyList.setBackground(new Color(0x121212));
        historyList.setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(historyList);
        scroll.setBorder(new EmptyBorder(20,20,20,20));
        add(scroll, BorderLayout.SOUTH);
        refreshHistory(historyDb, bookDb, user.getId(), model);

        backBtn.addActionListener(e -> frame.showHome());
        saveBtn.addActionListener(e -> {
            String uname = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            if (uname.isBlank() || pass.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }
            user.setUsername(uname);
            user.setPassword(pass);
            try {
                userDb.update(user);
                JOptionPane.showMessageDialog(this, "Profile updated");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void refreshHistory(ReadingHistoryDatabase historyDb, BookDatabase bookDb, String userId, DefaultListModel<String> model) {
        model.clear();
        try {
            for (String[] h : historyDb.getHistory(userId)) {
                bookDb.getById(h[1]).ifPresent(b -> model.addElement(b.getTitle()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
