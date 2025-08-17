package com.ibcs.ui;

import com.ibcs.db.UserDatabase;
import com.ibcs.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
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
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setBackground(new Color(0x1E1E1E));

        JLabel title = new JLabel("Login");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);
        card.add(title);
        card.add(Box.createVerticalStrut(20));

        JTextField emailField = new JTextField();
        emailField.putClientProperty("JTextField.placeholderText", "Email");
        card.add(emailField);
        card.add(Box.createVerticalStrut(10));

        JPasswordField passField = new JPasswordField();
        passField.putClientProperty("JTextField.placeholderText", "Password");
        card.add(passField);
        card.add(Box.createVerticalStrut(10));

        JCheckBox remember = new JCheckBox("Remember me");
        remember.setAlignmentX(Component.CENTER_ALIGNMENT);
        remember.setOpaque(false);
        remember.setForeground(Color.LIGHT_GRAY);
        card.add(remember);
        card.add(Box.createVerticalStrut(15));

        JButton loginButton = new JButton("Log in");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(loginButton);
        card.add(Box.createVerticalStrut(15));

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
            Optional<User> user = userDb.authenticate(email, password);
            if (user.isPresent()) {
                frame.loginSuccess(user.get());
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login");
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
