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
import com.vini.cps4005.library.service.*;
import com.vini.cps4005.library.util.Validation;
import java.util.Scanner;

public class LibraryConsoleUI {
    /*
    
    start()
    
    showMainMenu()
    
    manageBooks()
    
    manageMembers()
    
    manageBorrowRecords()
    
    */
    
    Scanner sc = new Scanner(System.in);
    
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
        
        
       
        int choice;
        
        do {
            showMainMenu();
            choice = Integer.parseInt(sc.nextLine());
            
            switch (choice) {
                case 1:
                    manageBooks();
                    break;
                case 2:
                    manageMembers();
                    break;
                case 3:
                    manageBorrowingRecords();
                    break;
                case 4:
                    searchRecords();
                case 5:
                    System.out.println("Exiting system...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 5);
        
    }
    
    public void showMainMenu() {
        System.out.println("---------- Main Library System Menu ----------");
        System.out.println("1. Manage books");
        System.out.println("2. Manage members");
        System.out.println("3. Manage borrowing records");
        System.out.println("4. Search records");
        System.out.println("5. Exit system");
        
        System.out.println("Please enter your Choice (1-5):");
    }

    
 
    //BOOKS -----------------------

    public void manageBooks() {
        BookService bookService = new BookService();
        
        int choice;
        
        do {
            showBooksMenu();
            choice = Integer.parseInt(sc.nextLine());
            
            switch (choice) {
                case 1:
                    bookService.addBook();
                    break;
                case 2:
                    bookService.displayAllBooks();
                    break;
                case 3:
                    bookService.searchForBook();
                    break;
                case 4:
                    bookService.updateBook();
                    break;
                case 5:
                    bookService.deleteBook();
                    break;
                case 6:
                    System.out.println("Exiting system...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 5);
        
    }
    
    
    public void showBooksMenu() {
        System.out.println("---------- Book Management Menu ----------");
        System.out.println("1. Add a book");
        System.out.println("2. Display all books");
        System.out.println("3. Search for a book");
        System.out.println("4. Update a book");
        System.out.println("5. Delete a book");
        System.out.println("6. Exit Book Management");
        
        System.out.println("Please enter your Choice (1-6):");
    }
   
    
    
    
    
    //MEMBERS -----------------------
    
    public void manageMembers() {
        showMembersMenu();
        
        
        
    }
    
    
    public void showMembersMenu() {
        System.out.println("---------- Member Management Menu ----------");
        System.out.println("1. Add a member");
        System.out.println("2. Display all members");
        System.out.println("3. Search for a member");
        System.out.println("4. Update a member");
        System.out.println("5. Delete a member");
        System.out.println("6. Exit Member Management");
        
        System.out.println("Please enter your Choice (1-6):");
    }
    
    
    
    
    
    
    //BORROW RECORDS -----------------------
    
    public void manageBorrowingRecords() {
        showBorrowingRecordsMenu();
        
        
    }
    
    
    public void showBorrowingRecordsMenu() {
        System.out.println("---------- Borrowing Records Management Menu ----------");
        System.out.println("1. Borrow a book");
        System.out.println("2. Add a borrowing record");
        System.out.println("3. Update a borrowing record");
        System.out.println("4. Delete a borrowing record");
        System.out.println("5. Exit Borrowing Record Management");
        
        System.out.println("Please enter your Choice (1-6):");
    }
    
    
    
    
    
    //SEARCH RECORDS -----------------------
    
    public void searchRecords() {
        showSearchRecordsMenu();
        
    }
    
    public void showSearchRecordsMenu() {
        System.out.println("---------- Search Record Menu ----------");
        System.out.println("1. Display all borrowing records");
        System.out.println("2. Display a member's borrowing history");
        System.out.println("3. Display a book's records");
        System.out.println("4. Exit search records");
        
        System.out.println("Please enter your Choice (1-4):");
    }
}
