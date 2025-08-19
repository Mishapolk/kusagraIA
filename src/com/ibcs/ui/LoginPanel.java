package com.ibcs.ui;

import com.ibcs.db.UserDatabase;
import com.ibcs.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Minimalistic login screen centred on the frame. The card layout and
 * placeholder text mirror the provided concept art for a sleek look.
 */
public class LoginPanel extends JPanel {
    public LoginPanel(MainFrame frame, UserDatabase userDb) {
        setLayout(new GridBagLayout());
        setOpaque(false);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 60, 40, 60));
        card.setBackground(new Color(0x1E1E1E));
        card.setPreferredSize(new Dimension(400, 300));

        JLabel title = new JLabel("Login");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);
        card.add(title);
        card.add(Box.createVerticalStrut(20));

        JTextField emailField = new JTextField();
        emailField.putClientProperty("JTextField.placeholderText", "Email");
        emailField.setMargin(new Insets(0,8,0,8));
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        card.add(emailField);
        card.add(Box.createVerticalStrut(15));

        JPasswordField passField = new JPasswordField();
        passField.putClientProperty("JTextField.placeholderText", "Password");
        passField.setMargin(new Insets(0,8,0,8));
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        card.add(passField);
        card.add(Box.createVerticalStrut(15));

        JCheckBox remember = new JCheckBox("Remember me");
        remember.setAlignmentX(Component.CENTER_ALIGNMENT);
        remember.setOpaque(false);
        remember.setForeground(Color.LIGHT_GRAY);
        card.add(remember);
        card.add(Box.createVerticalStrut(20));

        JButton loginButton = new JButton("Log in");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        card.add(loginButton);
        card.add(Box.createVerticalStrut(20));

        JLabel register = new JLabel("<html><a href='#'>Register</a></html>");
        register.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        register.setAlignmentX(Component.CENTER_ALIGNMENT);
        register.setForeground(new Color(0x2979FF));
        card.add(register);

        add(card);

        loginButton.addActionListener((ActionEvent e) -> {
            String email = emailField.getText();
            String password = new String(passField.getPassword());
            if (email.isBlank() || password.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }
            try {
                Optional<User> user = userDb.authenticate(email, password);
                if (user.isPresent()) {
                    frame.loginSuccess(user.get());
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid login");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        register.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                RegisterDialog dialog = new RegisterDialog((JFrame) SwingUtilities.getWindowAncestor(LoginPanel.this), userDb);
                dialog.setLocationRelativeTo(LoginPanel.this);
                dialog.setVisible(true);
                if (dialog.isSucceeded()) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Registration successful. You can now log in.");
                }
            }
        });
    }
}
