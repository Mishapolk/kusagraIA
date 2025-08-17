package com.ibcs.ui;

import com.ibcs.db.UserDatabase;
import com.ibcs.model.User;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.UUID;

public class RegisterDialog extends JDialog {
    private boolean succeeded = false;
    private JTextField userField = new JTextField(15);
    private JPasswordField passField = new JPasswordField(15);

    public RegisterDialog(JFrame owner, UserDatabase userDb) {
        super(owner, "Register", true);
        setLayout(new GridLayout(0,2,5,5));
        add(new JLabel("Username"));
        add(userField);
        add(new JLabel("Password"));
        add(passField);
        JButton ok = new JButton("Create");
        JButton cancel = new JButton("Cancel");
        add(ok); add(cancel);
        pack();

        ok.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            if (username.isBlank() || password.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }
            if (userDb.usernameExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists");
                return;
            }
            try {
                userDb.add(new User(UUID.randomUUID().toString(), username, password, false));
                succeeded = true;
                setVisible(false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        cancel.addActionListener(e -> setVisible(false));
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
