package com.ibcs.db;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;

public class AdminActionDatabase {
    private final Path path;

    public AdminActionDatabase(String filePath) throws IOException {
        this.path = Paths.get(filePath);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            try (BufferedWriter bw = Files.newBufferedWriter(path)) {
                bw.write("admin_id,book_id,action,timestamp\n");
            }
        }
    }

    public synchronized void log(String adminId, String bookId, String action) throws IOException {
        String line = String.join(",", adminId, bookId, action, String.valueOf(Instant.now().toEpochMilli())) + "\n";
        Files.write(path, line.getBytes(), StandardOpenOption.APPEND);
    }
}
