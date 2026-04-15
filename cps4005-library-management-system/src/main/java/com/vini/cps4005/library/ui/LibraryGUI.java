/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vini.cps4005.library.ui;

/**
 *
 * @author Daniele
 */

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class LibraryGUI extends JFrame {

    public LibraryGUI() {
        setTitle("St Mary's Digital Library System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Books", new BooksPanel());
        tabs.addTab("Members", new MembersPanel());
        tabs.addTab("Borrow Records", new BorrowRecordsPanel());
        tabs.addTab("Search", new SearchPanel());

        add(tabs);
    }

    public static void launch() {
        SwingUtilities.invokeLater(() -> {
            LibraryGUI gui = new LibraryGUI();
            gui.setVisible(true);
        });
    }
}
