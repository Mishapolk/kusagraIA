package com.ibcs.db;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class RatingDatabase {
    private final Path path;
    private final List<String[]> ratings = new ArrayList<>(); // user_id, book_id, rating, comment

    public RatingDatabase(String filePath) throws IOException {
        this.path = Paths.get(filePath);
        load();
    }

    private void load() throws IOException {
        ratings.clear();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", 4);
                if (p.length < 4) continue;
                ratings.add(p);
            }
        }
    }

    private void save() throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            bw.write("user_id,book_id,rating,comment\n");
            for (String[] r : ratings) {
                bw.write(String.join(",", r));
                bw.write("\n");
            }
        }
    }

    public void add(String userId, String bookId, int rating, String comment) throws IOException {
        ratings.add(new String[]{userId, bookId, String.valueOf(rating), comment.replace(",", " ")});
        save();
    }

    public List<String[]> getRatingsForBook(String bookId) {
        List<String[]> res = new ArrayList<>();
        for (String[] r : ratings) {
            if (r[1].equals(bookId)) res.add(r);
        }
        return res;
    }
}
