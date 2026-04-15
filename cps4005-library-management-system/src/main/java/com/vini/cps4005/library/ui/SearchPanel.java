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
import com.vini.cps4005.library.service.BorrowService;
import com.vini.cps4005.library.util.Validation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SearchPanel extends JPanel {

    private final BorrowService borrowService = new BorrowService();
    private final Validation validator = new Validation();

    private final JTextField memberIdField = new JTextField();
    private final JTextField bookIdField = new JTextField();

    private final DefaultTableModel tableModel;
    private final JTable table;

    public SearchPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.add(new JLabel("Member ID:"));
        inputPanel.add(memberIdField);
        inputPanel.add(new JLabel("Book ID:"));
        inputPanel.add(bookIdField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));

        JButton showAllButton = new JButton("Show All Records");
        JButton memberHistoryButton = new JButton("Member History");
        JButton bookHistoryButton = new JButton("Book History");
        JButton overdueButton = new JButton("Show Overdue");
        JButton clearButton = new JButton("Clear");

        buttonPanel.add(showAllButton);
        buttonPanel.add(memberHistoryButton);
        buttonPanel.add(bookHistoryButton);
        buttonPanel.add(overdueButton);
        buttonPanel.add(clearButton);

        topPanel.add(inputPanel, BorderLayout.CENTER);
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

        showAllButton.addActionListener(e -> showAllRecords());
        memberHistoryButton.addActionListener(e -> showMemberHistory());
        bookHistoryButton.addActionListener(e -> showBookHistory());
        overdueButton.addActionListener(e -> showOverdueRecords());
        clearButton.addActionListener(e -> clearFields());

        showAllRecords();
    }

    private void showAllRecords() {
        borrowService.updateOverdueRecords();
        loadTable(borrowService.getAllBorrowRecords());
    }

    private void showMemberHistory() {
        Integer memberId = parseIdField(memberIdField, "member ID");
        if (memberId == null) {
            return;
        }

        borrowService.updateOverdueRecords();
        List<BorrowRecord> records = borrowService.getMemberBorrowRecords(memberId);
        loadTable(records);

        if (records.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No borrowing records found for that member.");
        }
    }

    private void showBookHistory() {
        Integer bookId = parseIdField(bookIdField, "book ID");
        if (bookId == null) {
            return;
        }

        borrowService.updateOverdueRecords();
        List<BorrowRecord> records = borrowService.getBookBorrowRecords(bookId);
        loadTable(records);

        if (records.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No borrowing records found for that book.");
        }
    }

    private void showOverdueRecords() {
        borrowService.updateOverdueRecords();
        List<BorrowRecord> records = borrowService.getOverdueRecords();
        loadTable(records);

        if (records.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No overdue records found.");
        }
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

    private void clearFields() {
        memberIdField.setText("");
        bookIdField.setText("");
        table.clearSelection();
        showAllRecords();
    }
}