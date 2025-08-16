package com.ibcs.model;

public class User {
    private String id;
    private String username;
    private String password;
    private boolean admin;

    public User(String id, String username, String password, boolean admin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isAdmin() { return admin; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}
