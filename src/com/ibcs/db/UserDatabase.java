package com.ibcs.db;

import com.ibcs.model.User;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class UserDatabase {
    private final Path path;
    private final List<User> users = new ArrayList<>();

    public UserDatabase(String filePath) throws IOException {
        this.path = Paths.get(filePath);
        load();
    }

    private void load() throws IOException {
        users.clear();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length < 4) continue;
                users.add(new User(p[0], p[1], p[2], Boolean.parseBoolean(p[3])));
            }
        }
    }

    private void save() throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            bw.write("id,username,password,isAdmin\n");
            for (User u : users) {
                bw.write(String.join(",", u.getId(), u.getUsername(), u.getPassword(), String.valueOf(u.isAdmin())));
                bw.write("\n");
            }
        }
    }

    public Optional<User> authenticate(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();
    }

    public void add(User u) throws IOException {
        users.add(u);
        save();
    }

    public void update(User u) throws IOException {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(u.getId())) {
                users.set(i, u);
                break;
            }
        }
        save();
    }

    public boolean usernameExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }
}
