package com.ibcs.db;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;

public class ReadingHistoryDatabase {
    private final Path path;

    public ReadingHistoryDatabase(String filePath) throws IOException {
        this.path = Paths.get(filePath);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            try (BufferedWriter bw = Files.newBufferedWriter(path)) {
                bw.write("user_id,book_id,timestamp\n");
            }
        }
    }

    public synchronized void add(String userId, String bookId) throws IOException {
        String line = String.join(",", userId, bookId, String.valueOf(Instant.now().toEpochMilli())) + "\n";
        Files.write(path, line.getBytes(), StandardOpenOption.APPEND);
    }

    public List<String[]> getHistory(String userId) throws IOException {
        List<String[]> res = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 3 && p[0].equals(userId)) {
                    res.add(p);
                }
            }
        }
        return res;
    }
}
