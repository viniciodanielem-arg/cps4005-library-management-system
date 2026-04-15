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

        // NEW
        JButton searchCategoryButton = new JButton("Search Category");
        JButton sortAscButton = new JButton("Sort A-Z");
        JButton sortDescButton = new JButton("Sort Z-A");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(showAllButton);
        buttonPanel.add(searchTitleButton);
        buttonPanel.add(searchAuthorButton);
        buttonPanel.add(searchCategoryButton);
        buttonPanel.add(sortAscButton);
        buttonPanel.add(sortDescButton);

        // ===== TABLE =====
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Author", "Category", "Status"}, 0
        );

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // ===== ACTIONS =====
        addButton.addActionListener(e -> addBook());
        updateButton.addActionListener(e -> updateBook());
        deleteButton.addActionListener(e -> deleteBook());
        showAllButton.addActionListener(e -> loadTable(bookService.getAllBooks()));
        searchTitleButton.addActionListener(e -> loadTable(bookService.searchByTitle(titleField.getText().trim())));
        searchAuthorButton.addActionListener(e -> loadTable(bookService.searchByAuthor(authorField.getText().trim())));
        searchCategoryButton.addActionListener(e -> loadTable(bookService.searchByCategory(categoryField.getText().trim())));
        sortAscButton.addActionListener(e -> loadTable(bookService.getAllBooksSortedByTitle(true)));
        sortDescButton.addActionListener(e -> loadTable(bookService.getAllBooksSortedByTitle(false)));

        // Row click autofill
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                idField.setText(tableModel.getValueAt(row, 0).toString());
                titleField.setText(tableModel.getValueAt(row, 1).toString());
                authorField.setText(tableModel.getValueAt(row, 2).toString());
                categoryField.setText(tableModel.getValueAt(row, 3).toString());
            }
        });

        loadTable(bookService.getAllBooks());
    }

    private void loadTable(List<Book> books) {
        tableModel.setRowCount(0);

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
        boolean success = bookService.addBook(
                titleField.getText().trim(),
                authorField.getText().trim(),
                categoryField.getText().trim()
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "Book added.");
            clearFields();
            loadTable(bookService.getAllBooks());
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add book.");
        }
    }

    private void updateBook() {
        try {
            int id = Integer.parseInt(idField.getText().trim());

            boolean success = bookService.updateBook(
                    id,
                    titleField.getText().trim(),
                    authorField.getText().trim(),
                    categoryField.getText().trim()
            );

            JOptionPane.showMessageDialog(this, success ? "Updated." : "Update failed.");
            loadTable(bookService.getAllBooks());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid ID.");
        }
    }

    private void deleteBook() {
        try {
            int id = Integer.parseInt(idField.getText().trim());

            if (JOptionPane.showConfirmDialog(this, "Delete book?", "Confirm",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                boolean success = bookService.deleteBook(id);
                JOptionPane.showMessageDialog(this, success ? "Deleted." : "Delete failed.");
                loadTable(bookService.getAllBooks());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid ID.");
        }
    }

    private void clearFields() {
        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        categoryField.setText("");
    }
}