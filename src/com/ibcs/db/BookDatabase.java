package com.ibcs.db;

import com.ibcs.model.Book;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class BookDatabase {
    private final Path path;
    private final List<Book> books = new ArrayList<>();

    public BookDatabase(String filePath) throws IOException {
        this.path = Paths.get(filePath);
        load();
    }

    private void load() throws IOException {
        books.clear();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] p = parse(line);
                if (p.length < 9) continue;
                books.add(new Book(p[0], p[1], p[2], p[3], Integer.parseInt(p[4]),
                        Double.parseDouble(p[5]), p[6], p[7], p[8]));
            }
        }
    }

    private void save() throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            bw.write("id,title,author,genre,page_count,rating,language,description,image_url\n");
            for (Book b : books) {
                bw.write(String.join(",",
                        escape(b.getId()),
                        escape(b.getTitle()),
                        escape(b.getAuthor()),
                        escape(b.getGenre()),
                        String.valueOf(b.getPageCount()),
                        String.valueOf(b.getRating()),
                        escape(b.getLanguage()),
                        escape(b.getDescription()),
                        escape(b.getImageUrl())));
                bw.write("\n");
            }
        }
    }

    private String[] parse(String line) {
        java.util.List<String> fields = new java.util.ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        fields.add(sb.toString());
        return fields.toArray(new String[0]);
    }

    private String escape(String field) {
        if (field.contains("\"") || field.contains(",") || field.contains("\n")) {
            field = field.replace("\"", "\"\"");
            return "\"" + field + "\"";
        }
        return field;
    }

    public List<Book> getAll() { return new ArrayList<>(books); }

    public Optional<Book> getById(String id) {
        return books.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    public void add(Book b) throws IOException {
        books.add(b);
        save();
    }

    public void update(Book b) throws IOException {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(b.getId())) {
                books.set(i, b);
                break;
            }
        }
        save();
    }

    public void delete(String id) throws IOException {
        books.removeIf(b -> b.getId().equals(id));
        save();
    }

    public List<Book> search(String query) {
        String q = query.toLowerCase();
        return books.stream().filter(b ->
                b.getTitle().toLowerCase().contains(q) ||
                b.getAuthor().toLowerCase().contains(q) ||
                b.getGenre().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Book> filter(String genre, String language, String author, double minRating) {
        return books.stream().filter(b ->
                (genre == null || b.getGenre().equalsIgnoreCase(genre)) &&
                (language == null || b.getLanguage().equalsIgnoreCase(language)) &&
                (author == null || b.getAuthor().equalsIgnoreCase(author)) &&
                b.getRating() >= minRating)
                .collect(Collectors.toList());
    }
}
