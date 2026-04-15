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
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BooksPanel extends JPanel {

    private final BookService bookService = new BookService();

    private final JTextField titleField = new JTextField();
    private final JTextField authorField = new JTextField();
    private final JTextField categoryField = new JTextField();
    private final JTextField idField = new JTextField();

    private final JTextArea outputArea = new JTextArea();

    public BooksPanel() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.add(new JLabel("Book ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);

        JButton addButton = new JButton("Add");
        JButton showAllButton = new JButton("Show All");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton searchTitleButton = new JButton("Search by Title");
        JButton searchAuthorButton = new JButton("Search by Author");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(showAllButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchTitleButton);
        buttonPanel.add(searchAuthorButton);

        outputArea.setEditable(false);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        addButton.addActionListener(e -> addBook());
        showAllButton.addActionListener(e -> showAllBooks());
        updateButton.addActionListener(e -> updateBook());
        deleteButton.addActionListener(e -> deleteBook());
        searchTitleButton.addActionListener(e -> searchByTitle());
        searchAuthorButton.addActionListener(e -> searchByAuthor());
    }

    private void addBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String category = categoryField.getText().trim();

        boolean success = bookService.addBook(title, author, category);

        if (success) {
            JOptionPane.showMessageDialog(this, "Book added successfully.");
            clearFields();
            showAllBooks();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add book.");
        }
    }

    private void showAllBooks() {
        List<Book> books = bookService.getAllBooks();
        outputArea.setText("");

        for (Book book : books) {
            outputArea.append(book + "\n");
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
                showAllBooks();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid numeric book ID.");
        }
    }

    private void deleteBook() {
        try {
            int id = Integer.parseInt(idField.getText().trim());

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete book " + id + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = bookService.deleteBook(id);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Book deleted successfully.");
                    clearFields();
                    showAllBooks();
                } else {
                    JOptionPane.showMessageDialog(this, "Delete failed.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid numeric book ID.");
        }
    }

    private void searchByTitle() {
        String title = titleField.getText().trim();
        List<Book> books = bookService.searchByTitle(title);

        outputArea.setText("");
        for (Book book : books) {
            outputArea.append(book + "\n");
        }
    }

    private void searchByAuthor() {
        String author = authorField.getText().trim();
        List<Book> books = bookService.searchByAuthor(author);

        outputArea.setText("");
        for (Book book : books) {
            outputArea.append(book + "\n");
        }
    }

    private void clearFields() {
        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        categoryField.setText("");
    }
}
