package com.ibcs.db;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;

public class SearchLogDatabase {
    private final Path path;

    public SearchLogDatabase(String filePath) throws IOException {
        this.path = Paths.get(filePath);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            try (BufferedWriter bw = Files.newBufferedWriter(path)) {
                bw.write("user_id,query,timestamp\n");
            }
        }
    }

    public synchronized void log(String userId, String query) throws IOException {
        String sanitized = query.replace(",", " ");
        String line = String.join(",", userId, sanitized, String.valueOf(Instant.now().toEpochMilli())) + "\n";
        Files.write(path, line.getBytes(), StandardOpenOption.APPEND);
    }
}
