package com.ibcs.ui;

import com.ibcs.db.UserDatabase;
import com.ibcs.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Optional;

public class LoginPanel extends JPanel {
    public LoginPanel(MainFrame frame, UserDatabase userDb) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        gbc.gridx=0; gbc.gridy=0; add(userLabel, gbc);
        gbc.gridx=1; add(userField, gbc);
        gbc.gridx=0; gbc.gridy=1; add(passLabel, gbc);
        gbc.gridx=1; add(passField, gbc);
        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2; add(loginButton, gbc);

        loginButton.addActionListener((ActionEvent e) -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            if (username.isBlank() || password.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }
            Optional<User> user = userDb.authenticate(username, password);
            if (user.isPresent()) {
                frame.loginSuccess(user.get());
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login");
            }
        });
    }
}
