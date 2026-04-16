/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vini.cps4005.library.ui;

/**
 *
 * @author Daniele
 */

import com.vini.cps4005.library.model.BorrowRecord;
import com.vini.cps4005.library.model.ReturnStatus;
import com.vini.cps4005.library.service.BorrowService;
import com.vini.cps4005.library.util.Validation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

public class BorrowRecordsPanel extends JPanel {

    private final BorrowService borrowService = new BorrowService();
    private final Validation validator = new Validation();

    private final JTextField recordIdField = new JTextField();
    private final JTextField bookIdField = new JTextField();
    private final JTextField memberIdField = new JTextField();
    private final JTextField borrowDateField = new JTextField();
    private final JTextField endDateField = new JTextField();
    private final JTextField returnStatusField = new JTextField();

    private final JButton borrowBookButton = new JButton("Borrow Book");
    private final JButton addRecordButton = new JButton("Add Record");
    private final JButton updateStatusButton = new JButton("Update Status");
    private final JButton deleteRecordButton = new JButton("Delete Record");
    private final JButton showAllButton = new JButton("Show All");
    private final JButton showOverdueButton = new JButton("Show Overdue");
    private final JButton filterStatusButton = new JButton("Filter Status");
    private final JButton filterDateRangeButton = new JButton("Filter Date");
    private final JButton clearButton = new JButton("Clear");

    private final JLabel statusLabel = new JLabel(" ");

    private final DefaultTableModel tableModel;
    private final JTable table;

    public BorrowRecordsPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.add(new JLabel("Record ID:"));
        formPanel.add(recordIdField);
        formPanel.add(new JLabel("Book ID:"));
        formPanel.add(bookIdField);
        formPanel.add(new JLabel("Member ID:"));
        formPanel.add(memberIdField);
        formPanel.add(new JLabel("Borrow Date (dd-MM-yyyy):"));
        formPanel.add(borrowDateField);
        formPanel.add(new JLabel("End Date (dd-MM-yyyy):"));
        formPanel.add(endDateField);
        formPanel.add(new JLabel("Return Status:"));
        formPanel.add(returnStatusField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        buttonPanel.add(borrowBookButton);
        buttonPanel.add(addRecordButton);
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(deleteRecordButton);
        buttonPanel.add(showAllButton);
        buttonPanel.add(showOverdueButton);
        buttonPanel.add(filterStatusButton);
        buttonPanel.add(filterDateRangeButton);
        buttonPanel.add(clearButton);

        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(
                new Object[]{"Record ID", "Book ID", "Member ID", "Borrow Date", "Due Date", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(22);

        JScrollPane scrollPane = new JScrollPane(table);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        borrowBookButton.addActionListener(e -> borrowBook());
        addRecordButton.addActionListener(e -> addRecord());
        updateStatusButton.addActionListener(e -> updateStatus());
        deleteRecordButton.addActionListener(e -> deleteRecord());

        showAllButton.addActionListener(e ->
                loadTableAsync(() -> {
                    borrowService.updateOverdueRecords();
                    return borrowService.getAllBorrowRecords();
                }, "Loading all borrow records...")
        );

        showOverdueButton.addActionListener(e ->
                loadTableAsync(() -> {
                    borrowService.updateOverdueRecords();
                    return borrowService.getOverdueRecords();
                }, "Loading overdue records...")
        );

        filterStatusButton.addActionListener(e -> filterByStatus());
        filterDateRangeButton.addActionListener(e -> filterByDateRange());
        clearButton.addActionListener(e -> clearFields());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFieldsFromSelectedRow();
            }
        });

        loadTableAsync(() -> {
            borrowService.updateOverdueRecords();
            return borrowService.getAllBorrowRecords();
        }, "Loading all borrow records...");
    }

    private void loadTable(List<BorrowRecord> records) {
        tableModel.setRowCount(0);

        for (BorrowRecord record : records) {
            tableModel.addRow(new Object[]{
                    record.getRecordId(),
                    record.getBookId(),
                    record.getMemberId(),
                    record.getBorrowDate(),
                    record.getDueDate(),
                    record.getReturnStatus()
            });
        }
    }

    private void loadTableAsync(Supplier<List<BorrowRecord>> supplier, String loadingMessage) {
        statusLabel.setText(loadingMessage);
        setButtonsEnabled(false);

        SwingWorker<List<BorrowRecord>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<BorrowRecord> doInBackground() {
                return supplier.get();
            }

            @Override
            protected void done() {
                try {
                    List<BorrowRecord> records = get();
                    loadTable(records);
                    statusLabel.setText("Loaded " + records.size() + " borrow record(s).");
                } catch (Exception e) {
                    statusLabel.setText("Error loading borrow records.");
                    JOptionPane.showMessageDialog(BorrowRecordsPanel.this, "Failed to load borrow records.");
                } finally {
                    setButtonsEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void setButtonsEnabled(boolean enabled) {
        borrowBookButton.setEnabled(enabled);
        addRecordButton.setEnabled(enabled);
        updateStatusButton.setEnabled(enabled);
        deleteRecordButton.setEnabled(enabled);
        showAllButton.setEnabled(enabled);
        showOverdueButton.setEnabled(enabled);
        filterStatusButton.setEnabled(enabled);
        filterDateRangeButton.setEnabled(enabled);
        clearButton.setEnabled(enabled);
    }

    private void borrowBook() {
        Integer memberId = parseIdField(memberIdField, "member ID");
        if (memberId == null) {
            return;
        }

        Integer bookId = parseIdField(bookIdField, "book ID");
        if (bookId == null) {
            return;
        }

        boolean success = borrowService.borrowBook(memberId, bookId);

        if (success) {
            JOptionPane.showMessageDialog(this, "Book borrowed successfully.");
            clearFields();
            loadTableAsync(() -> {
                borrowService.updateOverdueRecords();
                return borrowService.getAllBorrowRecords();
            }, "Reloading borrow records...");
        } else {
            JOptionPane.showMessageDialog(this, "Borrowing unsuccessful.");
        }
    }

    private void addRecord() {
        Integer memberId = parseIdField(memberIdField, "member ID");
        if (memberId == null) {
            return;
        }

        Integer bookId = parseIdField(bookIdField, "book ID");
        if (bookId == null) {
            return;
        }

        LocalDate borrowDate;
        String dateInput = borrowDateField.getText().trim();
        if (dateInput.isBlank()) {
            borrowDate = LocalDate.now();
        } else {
            borrowDate = validator.parseDate(dateInput);
            if (borrowDate == null) {
                JOptionPane.showMessageDialog(this, "Enter a valid borrow date in dd-MM-yyyy format.");
                return;
            }
        }

        ReturnStatus returnStatus;
        String statusInput = returnStatusField.getText().trim();
        if (statusInput.isBlank()) {
            returnStatus = ReturnStatus.RETURNED;
        } else {
            returnStatus = validator.parseReturnStatus(statusInput);
            if (returnStatus == null) {
                JOptionPane.showMessageDialog(this, "Return status must be BORROWED, RETURNED, or OVERDUE.");
                return;
            }
        }

        boolean success = borrowService.addBorrowRecord(bookId, memberId, borrowDate, returnStatus);

        if (success) {
            JOptionPane.showMessageDialog(this, "Borrow record added successfully.");
            clearFields();
            loadTableAsync(() -> {
                borrowService.updateOverdueRecords();
                return borrowService.getAllBorrowRecords();
            }, "Reloading borrow records...");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add borrow record.");
        }
    }

    private void updateStatus() {
        Integer recordId = parseIdField(recordIdField, "record ID");
        if (recordId == null) {
            return;
        }

        String statusInput = returnStatusField.getText().trim();
        if (statusInput.isBlank()) {
            JOptionPane.showMessageDialog(this, "Enter a return status to update.");
            return;
        }

        ReturnStatus returnStatus = validator.parseReturnStatus(statusInput);
        if (returnStatus == null) {
            JOptionPane.showMessageDialog(this, "Return status must be BORROWED, RETURNED, or OVERDUE.");
            return;
        }

        boolean success = borrowService.updateBorrowingStatus(recordId, returnStatus);

        if (success) {
            JOptionPane.showMessageDialog(this, "Borrowing status updated successfully.");
            clearFields();
            loadTableAsync(() -> {
                borrowService.updateOverdueRecords();
                return borrowService.getAllBorrowRecords();
            }, "Reloading borrow records...");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update borrowing status.");
        }
    }

    private void deleteRecord() {
        Integer recordId = parseIdField(recordIdField, "record ID");
        if (recordId == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete record " + recordId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean success = borrowService.deleteBorrowRecord(recordId);

        if (success) {
            JOptionPane.showMessageDialog(this, "Borrow record deleted successfully.");
            clearFields();
            loadTableAsync(() -> {
                borrowService.updateOverdueRecords();
                return borrowService.getAllBorrowRecords();
            }, "Reloading borrow records...");
        } else {
            JOptionPane.showMessageDialog(this, "Delete failed.");
        }
    }

    private void filterByStatus() {
        String input = returnStatusField.getText().trim();

        ReturnStatus status = validator.parseReturnStatus(input);
        if (status == null) {
            JOptionPane.showMessageDialog(this, "Status must be BORROWED, RETURNED, or OVERDUE.");
            return;
        }

        loadTableAsync(() -> {
            borrowService.updateOverdueRecords();
            return borrowService.getBorrowRecordsByStatus(status);
        }, "Filtering by status...");
    }

    private void filterByDateRange() {
        LocalDate start = validator.parseDate(borrowDateField.getText().trim());
        LocalDate end = validator.parseDate(endDateField.getText().trim());

        if (start == null || end == null) {
            JOptionPane.showMessageDialog(this, "Dates must be in dd-MM-yyyy format.");
            return;
        }

        if (end.isBefore(start)) {
            JOptionPane.showMessageDialog(this, "End date must be the same as or later than start date.");
            return;
        }

        loadTableAsync(() -> {
            borrowService.updateOverdueRecords();
            return borrowService.getBorrowRecordsByDateRange(start, end);
        }, "Filtering by date range...");
    }

    private Integer parseIdField(JTextField field, String label) {
        String text = field.getText().trim();

        if (text.isBlank()) {
            JOptionPane.showMessageDialog(this, "Enter a " + label + ".");
            return null;
        }

        Integer id = validator.parseId(text);
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Enter a valid numeric " + label + ".");
            return null;
        }

        return id;
    }

    private void fillFieldsFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }

        recordIdField.setText(tableModel.getValueAt(row, 0).toString());
        bookIdField.setText(tableModel.getValueAt(row, 1).toString());
        memberIdField.setText(tableModel.getValueAt(row, 2).toString());

        Object borrowDateValue = tableModel.getValueAt(row, 3);
        borrowDateField.setText(borrowDateValue == null ? "" : borrowDateValue.toString());

        Object statusValue = tableModel.getValueAt(row, 5);
        returnStatusField.setText(statusValue == null ? "" : statusValue.toString());
    }

    private void clearFields() {
        recordIdField.setText("");
        bookIdField.setText("");
        memberIdField.setText("");
        borrowDateField.setText("");
        endDateField.setText("");
        returnStatusField.setText("");
        table.clearSelection();
        statusLabel.setText(" ");
    }
}