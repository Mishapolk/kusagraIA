package com.ibcs.model;

public class Book {
    private String id;
    private String title;
    private String author;
    private String genre;
    private int pageCount;
    private double rating;
    private String language;
    private String description;
    private String imageUrl;

    public Book(String id, String title, String author, String genre,
                int pageCount, double rating, String language,
                String description, String imageUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.pageCount = pageCount;
        this.rating = rating;
        this.language = language;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public int getPageCount() { return pageCount; }
    public double getRating() { return rating; }
    public String getLanguage() { return language; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setPageCount(int pageCount) { this.pageCount = pageCount; }
    public void setRating(double rating) { this.rating = rating; }
    public void setLanguage(String language) { this.language = language; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    @Override
    public String toString() {
        return title + " by " + author + " (" + genre + ")";
    }
}
