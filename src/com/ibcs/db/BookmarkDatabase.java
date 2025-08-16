package com.ibcs.db;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BookmarkDatabase {
    private final Path path;
    private final Map<String, Set<String>> bookmarks = new HashMap<>(); // user_id -> set of book_ids

    public BookmarkDatabase(String filePath) throws IOException {
        this.path = Paths.get(filePath);
        load();
    }

    private void load() throws IOException {
        bookmarks.clear();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length < 2) continue;
                bookmarks.computeIfAbsent(p[0], k -> new HashSet<>()).add(p[1]);
            }
        }
    }

    private void save() throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            bw.write("user_id,book_id\n");
            for (Map.Entry<String, Set<String>> e : bookmarks.entrySet()) {
                for (String b : e.getValue()) {
                    bw.write(e.getKey() + "," + b + "\n");
                }
            }
        }
    }

    public Set<String> getBookmarks(String userId) {
        return bookmarks.getOrDefault(userId, new HashSet<>());
    }

    public void add(String userId, String bookId) throws IOException {
        bookmarks.computeIfAbsent(userId, k -> new HashSet<>()).add(bookId);
        save();
    }

    public void remove(String userId, String bookId) throws IOException {
        if (bookmarks.containsKey(userId)) {
            bookmarks.get(userId).remove(bookId);
            save();
        }
    }
}
