/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vini.cps4005.library.ui;

/**
 *
 * @author Daniele
 */

import com.vini.cps4005.library.model.Book;
import com.vini.cps4005.library.service.BookService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BooksPanel extends JPanel {

    private final BookService bookService = new BookService();

    private final JTextField idField = new JTextField();
    private final JTextField titleField = new JTextField();
    private final JTextField authorField = new JTextField();
    private final JTextField categoryField = new JTextField();

    private final DefaultTableModel tableModel;
    private final JTable table;

    public BooksPanel() {
        setLayout(new BorderLayout());

        // ===== FORM =====
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.add(new JLabel("Book ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);

        // ===== BUTTONS =====
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton showAllButton = new JButton("Show All");
        JButton searchTitleButton = new JButton("Search Title");
        JButton searchAuthorButton = new JButton("Search Author");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(showAllButton);
        buttonPanel.add(searchTitleButton);
        buttonPanel.add(searchAuthorButton);

        // ===== TABLE =====
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{
                "ID", "Title", "Author", "Category", "Status"
        });

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // ===== LAYOUT =====
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // ===== BUTTON ACTIONS =====
        addButton.addActionListener(e -> addBook());
        updateButton.addActionListener(e -> updateBook());
        deleteButton.addActionListener(e -> deleteBook());
        showAllButton.addActionListener(e -> loadTable(bookService.getAllBooks()));
        searchTitleButton.addActionListener(e -> searchByTitle());
        searchAuthorButton.addActionListener(e -> searchByAuthor());

        // Load initial data
        loadTable(bookService.getAllBooks());
    }

    // ===== CORE METHODS =====

    private void loadTable(List<Book> books) {
        tableModel.setRowCount(0); // clear table

        for (Book book : books) {
            tableModel.addRow(new Object[]{
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getCategory(),
                    book.getAvailabilityStatus()
            });
        }
    }

    private void addBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String category = categoryField.getText().trim();

        boolean success = bookService.addBook(title, author, category);

        if (success) {
            JOptionPane.showMessageDialog(this, "Book added successfully.");
            clearFields();
            loadTable(bookService.getAllBooks());
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add book.");
        }
    }

    private void updateBook() {
        try {
            int id = Integer.parseInt(idField.getText().trim());

            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String category = categoryField.getText().trim();

            boolean success = bookService.updateBook(id, title, author, category);

            if (success) {
                JOptionPane.showMessageDialog(this, "Book updated successfully.");
                clearFields();
                loadTable(bookService.getAllBooks());
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid numeric ID.");
        }
    }

    private void deleteBook() {
        try {
            int id = Integer.parseInt(idField.getText().trim());

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete book " + id + "?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = bookService.deleteBook(id);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Book deleted.");
                    clearFields();
                    loadTable(bookService.getAllBooks());
                } else {
                    JOptionPane.showMessageDialog(this, "Delete failed.");
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid numeric ID.");
        }
    }

    private void searchByTitle() {
        String title = titleField.getText().trim();
        loadTable(bookService.searchByTitle(title));
    }

    private void searchByAuthor() {
        String author = authorField.getText().trim();
        loadTable(bookService.searchByAuthor(author));
    }

    private void clearFields() {
        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        categoryField.setText("");
    }
}