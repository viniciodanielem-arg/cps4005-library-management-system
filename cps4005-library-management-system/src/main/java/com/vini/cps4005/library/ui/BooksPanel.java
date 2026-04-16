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
import java.util.function.Supplier;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BooksPanel extends JPanel {

    private final BookService bookService = new BookService();

    private final JTextField idField = new JTextField();
    private final JTextField titleField = new JTextField();
    private final JTextField authorField = new JTextField();
    private final JTextField categoryField = new JTextField();

    private final JButton addButton = new JButton("Add");
    private final JButton updateButton = new JButton("Update");
    private final JButton deleteButton = new JButton("Delete");
    private final JButton showAllButton = new JButton("Show All");
    private final JButton searchTitleButton = new JButton("Search Title");
    private final JButton searchAuthorButton = new JButton("Search Author");
    private final JButton searchCategoryButton = new JButton("Search Category");
    private final JButton sortAscButton = new JButton("Sort A-Z");
    private final JButton sortDescButton = new JButton("Sort Z-A");
    private final JButton searchCombinedButton = new JButton("Advanced Search");
    private final JButton exportCsvButton = new JButton("Export CSV");

    private final JLabel statusLabel = new JLabel(" ");

    private final DefaultTableModel tableModel;
    private final JTable table;

    public BooksPanel() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.add(new JLabel("Book ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);

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
        buttonPanel.add(searchCombinedButton);
        buttonPanel.add(exportCsvButton);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(formPanel, BorderLayout.NORTH);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Author", "Category", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(topContainer, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addBook());
        updateButton.addActionListener(e -> updateBook());
        deleteButton.addActionListener(e -> deleteBook());

        showAllButton.addActionListener(e ->
                loadTableAsync(() -> bookService.getAllBooks(), "Loading all books...")
        );

        searchTitleButton.addActionListener(e ->
                loadTableAsync(() -> bookService.searchByTitle(titleField.getText().trim()), "Searching by title...")
        );

        searchAuthorButton.addActionListener(e ->
                loadTableAsync(() -> bookService.searchByAuthor(authorField.getText().trim()), "Searching by author...")
        );

        searchCategoryButton.addActionListener(e ->
                loadTableAsync(() -> bookService.searchByCategory(categoryField.getText().trim()), "Searching by category...")
        );

        sortAscButton.addActionListener(e ->
                loadTableAsync(() -> bookService.getAllBooksSortedByTitle(true), "Sorting A-Z...")
        );

        sortDescButton.addActionListener(e ->
                loadTableAsync(() -> bookService.getAllBooksSortedByTitle(false), "Sorting Z-A...")
        );
        
        exportCsvButton.addActionListener(e -> exportTableToCSV());
        
        searchCombinedButton.addActionListener(e ->
            loadTableAsync(
                () -> bookService.searchBooks(
                    titleField.getText().trim(),
                    authorField.getText().trim(),
                    categoryField.getText().trim()
                ),
                "Running advanced search..."
            )
        );

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                idField.setText(tableModel.getValueAt(row, 0).toString());
                titleField.setText(tableModel.getValueAt(row, 1).toString());
                authorField.setText(tableModel.getValueAt(row, 2).toString());
                categoryField.setText(tableModel.getValueAt(row, 3).toString());
            }
        });

        loadTableAsync(() -> bookService.getAllBooks(), "Loading all books...");
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

    private void loadTableAsync(Supplier<List<Book>> supplier, String loadingMessage) {
        statusLabel.setText(loadingMessage);
        setButtonsEnabled(false);

        SwingWorker<List<Book>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Book> doInBackground() {
                return supplier.get();
            }

            @Override
            protected void done() {
                try {
                    List<Book> books = get();
                    loadTable(books);
                    statusLabel.setText("Loaded " + books.size() + " book(s).");
                } catch (Exception e) {
                    statusLabel.setText("Error loading books.");
                    JOptionPane.showMessageDialog(BooksPanel.this, "Failed to load books.");
                } finally {
                    setButtonsEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void setButtonsEnabled(boolean enabled) {
        addButton.setEnabled(enabled);
        updateButton.setEnabled(enabled);
        deleteButton.setEnabled(enabled);
        showAllButton.setEnabled(enabled);
        searchTitleButton.setEnabled(enabled);
        searchAuthorButton.setEnabled(enabled);
        searchCategoryButton.setEnabled(enabled);
        sortAscButton.setEnabled(enabled);
        sortDescButton.setEnabled(enabled);
        searchCombinedButton.setEnabled(enabled);
        exportCsvButton.setEnabled(enabled);
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
            loadTableAsync(() -> bookService.getAllBooks(), "Reloading books...");
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
            loadTableAsync(() -> bookService.getAllBooks(), "Reloading books...");

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
                loadTableAsync(() -> bookService.getAllBooks(), "Reloading books...");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid ID.");
        }
    }
    
    private void exportTableToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV File");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = fileChooser.getSelectedFile();

        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new File(file.getAbsolutePath() + ".csv");
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                writer.write(tableModel.getColumnName(i));
                if (i < tableModel.getColumnCount() - 1) {
                    writer.write(",");
                }
            }
            writer.write("\n");

            for (int row = 0; row < tableModel.getRowCount(); row++) {
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    Object value = tableModel.getValueAt(row, col);
                    String text = value == null ? "" : value.toString();

                    text = text.replace("\"", "\"\"");
                    writer.write("\"" + text + "\"");

                    if (col < tableModel.getColumnCount() - 1) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }

            JOptionPane.showMessageDialog(this, "CSV exported successfully.");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to export CSV.");
        }
    }

    private void clearFields() {
        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        categoryField.setText("");
    }
}