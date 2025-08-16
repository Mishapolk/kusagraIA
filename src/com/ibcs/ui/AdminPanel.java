package com.ibcs.ui;

import com.ibcs.db.BookDatabase;
import com.ibcs.db.AdminActionDatabase;
import com.ibcs.model.Book;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;

public class AdminPanel extends JPanel {
    public AdminPanel(MainFrame frame, BookDatabase bookDb, AdminActionDatabase actionDb) {
        setLayout(new BorderLayout());
        JButton backBtn = new JButton("Back");
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JPanel top = new JPanel();
        top.setBackground(new Color(240,248,255));
        top.setBorder(new EmptyBorder(10,10,10,10));
        top.add(backBtn); top.add(addBtn); top.add(editBtn); top.add(deleteBtn);
        add(top, BorderLayout.NORTH);

        String[] cols = {"ID","Title","Author","Genre","Pages","Rating","Language"};
        DefaultTableModel model = new DefaultTableModel(cols,0);
        JTable table = new JTable(model);
        refresh(bookDb, model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(10,10,10,10));
        add(scroll, BorderLayout.CENTER);

        backBtn.addActionListener(e -> frame.showHome());
        addBtn.addActionListener(e -> {
            BookFormDialog dialog = new BookFormDialog(frame, null);
            dialog.setVisible(true);
            if (dialog.isOk()) {
                try {
                    Book b = dialog.getBook();
                    bookDb.add(b);
                    actionDb.log(frame.getCurrentUser().getId(), b.getId(), "Add");
                    refresh(bookDb, model);
                } catch (IOException ex) { ex.printStackTrace(); }
            }
        });
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row,0);
                bookDb.getById(id).ifPresent(book -> {
                    BookFormDialog dialog = new BookFormDialog(frame, book);
                    dialog.setVisible(true);
                    if (dialog.isOk()) {
                        try {
                            Book updated = dialog.getBook();
                            bookDb.update(updated);
                            actionDb.log(frame.getCurrentUser().getId(), updated.getId(), "Edit");
                            refresh(bookDb, model);
                        } catch (IOException ex) { ex.printStackTrace(); }
                    }
                });
            }
        });
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row,0);
                int confirm = JOptionPane.showConfirmDialog(this, "Delete book?","Confirm",JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        bookDb.delete(id);
                        actionDb.log(frame.getCurrentUser().getId(), id, "Delete");
                        refresh(bookDb, model);
                    } catch (IOException ex) { ex.printStackTrace(); }
                }
            }
        });
    }

    private void refresh(BookDatabase bookDb, DefaultTableModel model) {
        model.setRowCount(0);
        for (Book b : bookDb.getAll()) {
            model.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getGenre(), b.getPageCount(), b.getRating(), b.getLanguage()});
        }
    }
}
