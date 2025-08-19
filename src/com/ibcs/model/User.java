package com.ibcs.model;

public class User {
    private String id;
    private String email;
    private String password;
    private boolean admin;

    public User(String id, String email, String password, boolean admin) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.admin = admin;
    }

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean isAdmin() { return admin; }

    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}
