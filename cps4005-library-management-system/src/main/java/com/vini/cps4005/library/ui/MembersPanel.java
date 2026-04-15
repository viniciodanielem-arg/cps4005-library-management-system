/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vini.cps4005.library.ui;

/**
 *
 * @author Daniele
 */

import com.vini.cps4005.library.model.Member;
import com.vini.cps4005.library.model.MembershipType;
import com.vini.cps4005.library.service.MemberService;
import com.vini.cps4005.library.util.Validation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MembersPanel extends JPanel {

    private final MemberService memberService = new MemberService();
    private final Validation validator = new Validation();

    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField membershipTypeField = new JTextField();

    private final DefaultTableModel tableModel;
    private final JTable table;

    public MembersPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.add(new JLabel("Member ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Membership Type:"));
        formPanel.add(membershipTypeField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton showAllButton = new JButton("Show All");
        JButton searchIdButton = new JButton("Search ID");
        JButton searchNameButton = new JButton("Search Name");
        JButton clearButton = new JButton("Clear");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(showAllButton);
        buttonPanel.add(searchIdButton);
        buttonPanel.add(searchNameButton);
        buttonPanel.add(clearButton);

        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Email", "Membership Type"}, 0
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

        addButton.addActionListener(e -> addMember());
        updateButton.addActionListener(e -> updateMember());
        deleteButton.addActionListener(e -> deleteMember());
        showAllButton.addActionListener(e -> loadTable(memberService.getAllMembers()));
        searchIdButton.addActionListener(e -> searchById());
        searchNameButton.addActionListener(e -> searchByName());
        clearButton.addActionListener(e -> clearFields());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFieldsFromSelectedRow();
            }
        });

        loadTable(memberService.getAllMembers());
    }

    private void loadTable(List<Member> members) {
        tableModel.setRowCount(0);

        for (Member member : members) {
            tableModel.addRow(new Object[]{
                    member.getMemberId(),
                    member.getName(),
                    member.getEmail(),
                    member.getMembershipType()
            });
        }
    }

    private void addMember() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        MembershipType membershipType = validator.parseMembershipType(membershipTypeField.getText().trim());

        if (name.isBlank()) {
            JOptionPane.showMessageDialog(this, "Name is required.");
            return;
        }

        if (!validator.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Enter a valid email address.");
            return;
        }

        if (membershipType == null) {
            JOptionPane.showMessageDialog(this, "Membership type must be STUDENT, STAFF, or ADMIN.");
            return;
        }

        boolean success = memberService.addMember(name, email, membershipType);

        if (success) {
            JOptionPane.showMessageDialog(this, "Member added successfully.");
            clearFields();
            loadTable(memberService.getAllMembers());
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add member.");
        }
    }

    private void updateMember() {
        Integer id = parseIdField();
        if (id == null) {
            return;
        }

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String typeInput = membershipTypeField.getText().trim();

        String finalName = name.isBlank() ? null : name;

        String finalEmail = null;
        if (!email.isBlank()) {
            if (!validator.isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Enter a valid email address.");
                return;
            }
            finalEmail = email;
        }

        MembershipType finalType = null;
        if (!typeInput.isBlank()) {
            finalType = validator.parseMembershipType(typeInput);
            if (finalType == null) {
                JOptionPane.showMessageDialog(this, "Membership type must be STUDENT, STAFF, or ADMIN.");
                return;
            }
        }

        boolean success = memberService.updateMember(id, finalName, finalEmail, finalType);

        if (success) {
            JOptionPane.showMessageDialog(this, "Member updated successfully.");
            clearFields();
            loadTable(memberService.getAllMembers());
        } else {
            JOptionPane.showMessageDialog(this, "Update failed.");
        }
    }

    private void deleteMember() {
        Integer id = parseIdField();
        if (id == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete member " + id + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean success = memberService.deleteMember(id);

        if (success) {
            JOptionPane.showMessageDialog(this, "Member deleted successfully.");
            clearFields();
            loadTable(memberService.getAllMembers());
        } else {
            JOptionPane.showMessageDialog(this, "Delete failed.");
        }
    }

    private void searchById() {
        Integer id = parseIdField();
        if (id == null) {
            return;
        }

        Member member = memberService.getMemberById(id);

        tableModel.setRowCount(0);

        if (member != null) {
            tableModel.addRow(new Object[]{
                    member.getMemberId(),
                    member.getName(),
                    member.getEmail(),
                    member.getMembershipType()
            });
        } else {
            JOptionPane.showMessageDialog(this, "Member not found.");
        }
    }

    private void searchByName() {
        String name = nameField.getText().trim();

        if (name.isBlank()) {
            JOptionPane.showMessageDialog(this, "Enter a name to search.");
            return;
        }

        List<Member> members = memberService.getMemberByName(name);
        loadTable(members);

        if (members.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No matching members found.");
        }
    }

    private Integer parseIdField() {
        try {
            String text = idField.getText().trim();
            if (text.isBlank()) {
                JOptionPane.showMessageDialog(this, "Enter a member ID.");
                return null;
            }

            Integer id = validator.parseId(text);
            if (id == null) {
                JOptionPane.showMessageDialog(this, "Enter a valid numeric member ID.");
                return null;
            }

            return id;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter a valid numeric member ID.");
            return null;
        }
    }

    private void fillFieldsFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }

        idField.setText(tableModel.getValueAt(row, 0).toString());
        nameField.setText(tableModel.getValueAt(row, 1).toString());
        emailField.setText(tableModel.getValueAt(row, 2).toString());
        membershipTypeField.setText(tableModel.getValueAt(row, 3).toString());
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        membershipTypeField.setText("");
        table.clearSelection();
    }
}