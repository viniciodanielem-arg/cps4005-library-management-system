/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vini.cps4005.library.ui;

/**
 *
 * @author Daniele
 */
import com.vini.cps4005.library.dao.*;
import com.vini.cps4005.library.model.*;
import com.vini.cps4005.library.util.Validation;

public class LibraryConsoleUI {
    /*
    
    start()
    
    showMainMenu()
    
    manageBooks()
    
    manageMembers()
    
    manageBorrowRecords()
    */
    
    public void start() {
        BookDAO bookDAO = new BookDAO();
        MemberDAO memberDAO = new MemberDAO();
        BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
        
        bookDAO.createTable();
        memberDAO.createTable();
        borrowRecordDAO.createTable();
        
        //inserting sample books:
        Book book1 = new Book("Introduction to Java", "John Smith", "Programming", "Available");
        Book book2 = new Book("Database Systems", "Maria Garcia", "Computer Science", "Borrowed");
        Book book3 = new Book("Software Engineering Principles", "Alan Brown", "Engineering", "Available");
        
        bookDAO.addBook(book1);
        bookDAO.addBook(book2);
        bookDAO.addBook(book3);
        
        //inserting sample members:
        
        Member member1 = new Member("Alice Johnson", "alice.johnson@stmarys.ac.uk", MembershipType.STUDENT);
        Member member2 = new Member("Michael Lee", "michael.lee@stmarys.ac.uk", MembershipType.STAFF);
        Member member3 = new Member("Sara Ahmed", "sara.ahmed@stmarys.ac.uk", MembershipType.STUDENT);
        
        memberDAO.addMember(member1);
        memberDAO.addMember(member2);
        memberDAO.addMember(member3);
        
        //inserting sample borrow records:
        Validation validator = new Validation();
        
        BorrowRecord br1 = new BorrowRecord(2, 1, validator.parseDate("2025-03-01"), validator.parseDate("2025-03-15"), ReturnStatus.BORROWED);
        BorrowRecord br2 = new BorrowRecord(1, 2, validator.parseDate("2025-03-02"), validator.parseDate("2025-03-16"), ReturnStatus.RETURNED);
        BorrowRecord br3 = new BorrowRecord(3, 3, validator.parseDate("2025-03-05"), validator.parseDate("2025-03-19"), ReturnStatus.BORROWED);
        
        borrowRecordDAO.addBorrowRecord(br1);
        borrowRecordDAO.addBorrowRecord(br2);
        borrowRecordDAO.addBorrowRecord(br3);
    }
}
