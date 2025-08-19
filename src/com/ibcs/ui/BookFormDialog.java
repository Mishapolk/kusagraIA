package com.ibcs.ui;

import com.ibcs.model.Book;

import javax.swing.*;
import java.awt.*;

public class BookFormDialog extends JDialog {
    private JTextField idField = new JTextField(5);
    private JTextField titleField = new JTextField(15);
    private JTextField authorField = new JTextField(15);
    private JTextField genreField = new JTextField(10);
    private JTextField pagesField = new JTextField(5);
    private JTextField ratingField = new JTextField(5);
    private JTextField langField = new JTextField(5);
    private JTextField descField = new JTextField(20);
    private JTextField imageField = new JTextField(20);
    private boolean ok = false;

    public BookFormDialog(JFrame owner, Book book) {
        super(owner, true);
        setLayout(new GridLayout(0,2));
        add(new JLabel("ID")); add(idField);
        add(new JLabel("Title")); add(titleField);
        add(new JLabel("Author")); add(authorField);
        add(new JLabel("Genre")); add(genreField);
        add(new JLabel("Pages")); add(pagesField);
        add(new JLabel("Rating")); add(ratingField);
        add(new JLabel("Language")); add(langField);
        add(new JLabel("Description")); add(descField);
        add(new JLabel("Image URL")); add(imageField);
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");
        add(okBtn); add(cancelBtn);
        pack();

        if (book != null) {
            idField.setText(book.getId()); idField.setEditable(false);
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            genreField.setText(book.getGenre());
            pagesField.setText(String.valueOf(book.getPageCount()));
            ratingField.setText(String.valueOf(book.getRating()));
            langField.setText(book.getLanguage());
            descField.setText(book.getDescription());
            imageField.setText(book.getImageUrl());
        }

        okBtn.addActionListener(e -> {
            if (idField.getText().isBlank() || titleField.getText().isBlank() ||
                    authorField.getText().isBlank() || genreField.getText().isBlank() ||
                    pagesField.getText().isBlank() || ratingField.getText().isBlank() ||
                    langField.getText().isBlank() || descField.getText().isBlank() || imageField.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }
            try {
                Integer.parseInt(pagesField.getText());
                Double.parseDouble(ratingField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format");
                return;
            }
            ok = true;
            setVisible(false);
        });
        cancelBtn.addActionListener(e -> setVisible(false));
    }

    public boolean isOk() { return ok; }

    public Book getBook() {
        return new Book(
                idField.getText(),
                titleField.getText(),
                authorField.getText(),
                genreField.getText(),
                Integer.parseInt(pagesField.getText()),
                Double.parseDouble(ratingField.getText()),
                langField.getText(),
                descField.getText(),
                imageField.getText());
    }
}
