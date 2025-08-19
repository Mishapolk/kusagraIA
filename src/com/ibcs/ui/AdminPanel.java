package com.ibcs.ui;

import com.ibcs.db.BookDatabase;
import com.ibcs.db.AdminActionDatabase;
import com.ibcs.model.Book;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.sql.SQLException;

public class AdminPanel extends JPanel {
    public AdminPanel(MainFrame frame, BookDatabase bookDb, AdminActionDatabase actionDb) {
        setLayout(new BorderLayout());
        JButton backBtn = new JButton("Back");
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        top.setBackground(new Color(0x181818));
        top.setBorder(new EmptyBorder(0,20,0,20));
        top.add(backBtn); top.add(addBtn); top.add(editBtn); top.add(deleteBtn);
        add(top, BorderLayout.NORTH);

        String[] cols = {"ID","Title","Author","Genre","Pages","Rating","Language"};
        DefaultTableModel model = new DefaultTableModel(cols,0);
        JTable table = new JTable(model);
        table.setBackground(new Color(0x121212));
        table.setForeground(Color.WHITE);
        refresh(bookDb, model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(20,20,20,20));
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
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
        });
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row,0);
                try {
                    bookDb.getById(id).ifPresent(book -> {
                        BookFormDialog dialog = new BookFormDialog(frame, book);
                        dialog.setVisible(true);
                        if (dialog.isOk()) {
                            try {
                                Book updated = dialog.getBook();
                                bookDb.update(updated);
                                actionDb.log(frame.getCurrentUser().getId(), updated.getId(), "Edit");
                                refresh(bookDb, model);
                            } catch (SQLException ex) { ex.printStackTrace(); }
                        }
                    });
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
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
                    } catch (SQLException ex) { ex.printStackTrace(); }
                }
            }
        });
    }

    private void refresh(BookDatabase bookDb, DefaultTableModel model) {
        model.setRowCount(0);
        try {
            for (Book b : bookDb.getAll()) {
                model.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getGenre(), b.getPageCount(), b.getRating(), b.getLanguage()});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
