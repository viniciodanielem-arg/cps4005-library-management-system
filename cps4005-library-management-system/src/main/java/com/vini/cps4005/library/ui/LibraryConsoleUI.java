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
import java.time.LocalDate;

public class LibraryConsoleUI {
    /*
    
    start()
    
    showMainMenu()
    
    manageBooks()
    
    manageMembers()
    
    manageBorrowRecords()
    
    */
    
    private final BookService bookService = new BookService();
    private final MemberService memberService = new MemberService();
    private final BorrowService borrowRecordService = new BorrowService();
    
    private final Validation validator = new Validation();
    private final Scanner sc = new Scanner(System.in);
    
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
        
        if (bookDAO.isTableEmpty()) {
            bookDAO.addBook(book1);
            bookDAO.addBook(book2);
            bookDAO.addBook(book3);
        }
        
        //inserting sample members:
        
        Member member1 = new Member("Alice Johnson", "alice.johnson@stmarys.ac.uk", MembershipType.STUDENT);
        Member member2 = new Member("Michael Lee", "michael.lee@stmarys.ac.uk", MembershipType.STAFF);
        Member member3 = new Member("Sara Ahmed", "sara.ahmed@stmarys.ac.uk", MembershipType.STUDENT);
        
        if (memberDAO.isTableEmpty()) {
            memberDAO.addMember(member1);
            memberDAO.addMember(member2);
            memberDAO.addMember(member3);
        }
        
        //inserting sample borrow records:
        
        
        BorrowRecord br1 = new BorrowRecord(2, 1, validator.parseDate("01-03-2025"), validator.parseDate("15-03-2025"), ReturnStatus.BORROWED);
        BorrowRecord br2 = new BorrowRecord(1, 2, validator.parseDate("02-03-2025"), validator.parseDate("16-03-2025"), ReturnStatus.RETURNED);
        BorrowRecord br3 = new BorrowRecord(3, 3, validator.parseDate("05-03-2025"), validator.parseDate("19-03-2025"), ReturnStatus.BORROWED);
        
        if (borrowRecordDAO.isTableEmpty()) {
            borrowRecordDAO.addBorrowRecord(br1);
            borrowRecordDAO.addBorrowRecord(br2);
            borrowRecordDAO.addBorrowRecord(br3);
        }
        
        
       
        int choice;
        
        do {
            showMainMenu();
            choice = validator.parseId(sc.nextLine());
            
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
                    break;
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
        
        int choice = -1;
        
        do {
            showBooksMenu();
            
            String input = sc.nextLine();
            if (!input.matches("\\d+")) {
                System.out.println("Invalid input. Enter a number.");
                continue;
            }
            
            choice = Integer.parseInt(input);
            
            switch (choice) {
                
                case 1: //Add a book
                    System.out.println("Enter title: ");
                    String title = sc.nextLine();
                    
                    System.out.println("Enter author: ");
                    String author = sc.nextLine();
                    
                    System.out.println("Enter category: ");
                    String category = sc.nextLine();
                   
                    if (bookService.addBook(title, author, category)) {
                        System.out.println("Book added successfully");
                    } else {
                        System.out.println("Failed to add book");
                    }
                    break;
                    
                case 2: // Display all books
                    System.out.println("\n=== All Books ===");
                    for (Book book : bookService.getAllBooks()) {
                        System.out.println(book);
                    }
                    break;
                    
                case 3: //search for books
                    handleBookSearch();
                    break;
                    
                case 4: // Update a book
                    System.out.println("Enter book ID to update: ");
                    Integer updateId = validator.parseId(sc.nextLine());
                    if (updateId == null) {
                        System.out.println("Invalid ID.");
                        break;
                    }
                    
                    System.out.println("Enter new title (leave blank to keep the same)");
                    String newTitle = sc.nextLine();
                    
                    System.out.println("Enter new author (leave blank to keep the same)");
                    String newAuthor = sc.nextLine();
                    
                    System.out.println("Enter new category (leave blank to keep the same)");
                    String newCategory = sc.nextLine();
                    
                    if (bookService.updateBook(updateId, newTitle, newAuthor, newCategory)) {
                        System.out.println("Book updated successfully");
                    } else {
                        System.out.println("Update failed");
                    }
                    break;
                    
                case 5: // Delete a book
                    System.out.println("Enter book ID to delete: ");
                    Integer deleteId = validator.parseId(sc.nextLine());
                    if (deleteId == null) {
                        System.out.println("Invalid ID.");
                        break;
                    }
                    
                    if (bookService.deleteBook(deleteId)) {
                        System.out.println("Book deleted successfully");
                    } else {
                        System.out.println("Delete failed.");
                    }
                    break;
                    
                case 6:
                    System.out.println("Returning to main menu...");
                    break;
                
                default:
                    System.out.println("Invalid choice.");
                           
                    
            }
        } while (choice != 6);
    }
    
    private void handleBookSearch() {
        int choice = -1;
        
        do {
            System.out.println("\n---Search Books ---");
            System.out.println("1. By Id");
            System.out.println("2. By Title");
            System.out.println("3. By Author");
            System.out.println("4. Back");
            
            String input = sc.nextLine();
            if (!input.matches("\\d+")) {
                System.out.println("Invalid input.");
                continue;
            }
            
            choice = Integer.parseInt(input);
           
            switch (choice) {
                
                case 1:
                    System.out.println("Enter ID: ");
                    Integer id = validator.parseId(sc.nextLine());
                    if (id == null) {
                        System.out.println("Invalid ID.");
                        break;
                    }
                    
                    Book book = bookService.getBookById(id);
                    if (book != null) {
                        System.out.println(book);
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;
                    
                case 2:
                    System.out.println("Enter title: ");
                    String title = sc.nextLine();
                    
                    for (Book b : bookService.searchByTitle(title)) {
                        System.out.println(b);
                    }
                    break;
                    
                case 3:
                    System.out.println("Enter author: ");
                    String author = sc.nextLine();
                    
                    for (Book b : bookService.searchByAuthor(author)) {
                        System.out.println(b);
                    }
                    break;
                    
                case 4:
                    break;
                    
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 4);
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
        int choice = -1;
        
        do {
            showMembersMenu();
            
            String input  = sc.nextLine();
            if (!input.matches("\\d+")) {
                System.out.println("Invalid input. Enter a number.");
                continue;
            }
            
            choice = Integer.parseInt(input);
            
            switch (choice) {
                
                case 1: //Add a member
                    System.out.println("Enter name: ");
                    String name = sc.nextLine();

                    String email;

                    while (true) {
                        System.out.print("Enter email: ");
                        String emailInput = sc.nextLine();

                        if (validator.isValidEmail(emailInput)) {
                            email = emailInput;
                            break;
                        } else {
                            System.out.println("Invalid email. Try again.");
                        }
                    }

                    
                    System.out.println("Enter membership type:(STUDENT, STAFF, ADMIN)");
                    MembershipType membershipType = validator.parseMembershipType(sc.nextLine());
                    if (membershipType == null) {
                        System.out.println("Invalid membership type, defaulting to Student type...");
                        membershipType = MembershipType.STUDENT; // (DEFAULT = STUDENT)
                    }
                    
                    if (memberService.addMember(name, email, membershipType)) {
                        System.out.println("Member added successfully");
                    } else {
                        System.out.println("Failed to add member");
                    }
                    break;
                
                case 2: // Display all members
                    System.out.println("\n=== All Members ===");
                    for (Member member : memberService.getAllMembers()) {
                        System.out.println(member);
                        }
                    break;
                    
                case 3: // search for members
                    handleMemberSearch();
                    break;
                    
                case 4: // Update a member
                    System.out.println("Enter member Id to update: ");
                    Integer updateId = validator.parseId(sc.nextLine());
                    if (updateId == null) {
                        System.out.println("Invalid ID.");
                        break;
                    }
                    
                    System.out.println("Enter new name (leave blank to keep the same)");
                    String newName = sc.nextLine();
                    
                    System.out.println("Enter new email (leave blank to keep the same)");
                    String emailInput2 = sc.nextLine().trim();
                    String newEmail = null;

                    if (!emailInput2.isEmpty()) {
                        if (validator.isValidEmail(emailInput2)) {
                            newEmail = emailInput2;
                        } else {
                            System.out.println("Invalid email.");
                            break;
                        }
                    }
                    
                    System.out.println("Enter new membership type (leave blank to keep the same)");
                    String typeInput = sc.nextLine().trim();
                    MembershipType newMembershipType = null;
                    
                    if (!typeInput.isEmpty()) {
                        newMembershipType = validator.parseMembershipType(typeInput);
                        if (newMembershipType == null) {
                            System.out.println("Invalid membership type");
                            break;
                        }
                    }
                    
                    if (memberService.updateMember(updateId, newName, newEmail, newMembershipType)) {
                        System.out.println("Member updated successfully");
                    } else {
                        System.out.println("Update failed");
                    }
                    break;
                    
                case 5: // Delete member
                    System.out.println("Enter member ID to delete: ");
                    Integer memId = validator.parseId(sc.nextLine());
                    if (memId == null) {
                        System.out.println("Invalid ID.");
                        break;
                    }
                    
                    if (memberService.deleteMember(memId)) {
                        System.out.println("member deleted successfully");
                    } else {
                        System.out.println("delete failed");
                    }
                    break;
                    
                case 6:
                    System.out.println("Returning to main menu...");
                    break;
                    
                default:
                    System.out.println("Invalid choice");
                    
            }
        } while (choice != 6);
            
    }
    
    public void handleMemberSearch() {
        int choice = -1;
        
        do {
            System.out.println("\n---Search Members---");
            System.out.println("1. By Id");
            System.out.println("2. By Name");
            System.out.println("3. Back");
            
            String input = sc.nextLine();
                if (!input.matches("\\d+")) {
                    System.out.println("Invalid input.");
                    continue;
                }
            
            choice = Integer.parseInt(input);
                
            switch (choice) {
                
                case 1:
                    System.out.println("Enter ID: ");
                    Integer id = validator.parseId(sc.nextLine());
                    if (id == null) {
                        System.out.println("Invalid ID.");
                        break;
                    }
                    
                    Member member = memberService.getMemberById(id);
                    if (member != null) {
                        System.out.println(member);
                    } else {
                        System.out.println("Member not found");
                    }
                    break;
                    
                case 2:
                    System.out.println("Enter Name: ");
                    String name = sc.nextLine();
                    
                    for (Member mem : memberService.getMemberByName(name)) {
                        System.out.println(mem);
                    }
                    break;
                    
                case 3:
                    break;
                    
                default:
                    System.out.println("Invalid choice");
            }    

        } while (choice != 3);
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
      
        int choice = -1;
        
        do {
            showBorrowingRecordsMenu();
            
            String input  = sc.nextLine();
            if (!input.matches("\\d+")) {
                System.out.println("Invalid input. Enter a number.");
                continue;
            }
            
            choice = Integer.parseInt(input);
            
            switch (choice) {
                
                case 1: //Borrow a book (1st person)
                    System.out.println("Enter your member Id: ");
                    String memberInput = sc.nextLine();
                    Integer memId = validator.parseId(memberInput);
                    if (memId == null) {
                        System.out.println("Invalid member ID.");
                        break;
                    }

                    System.out.println("Enter chosen book Id: ");
                    String bookIdInput = sc.nextLine();
                    Integer bookId = validator.parseId(bookIdInput);
                    if (bookId == null) {
                        System.out.println("Invalid book ID.");
                        break;
                    }
                    
                    if (borrowRecordService.borrowBook(memId, bookId)) {
                        System.out.println("Book borrowed successsfully");
                    } else {
                        System.out.println("Borrowing unsuccessful... Check Details + try again"); 
                    }
                    break;
                
                case 2: // Add a Borrowing Record
                    System.out.println("Please enter the member Id: ");
                    String memberInput2 = sc.nextLine();
                    Integer memId2 = validator.parseId(memberInput2);
                    if (memId2 == null) {
                        System.out.println("Invalid member ID.");
                        break;
                    }
                    
                    System.out.println("Please enter the book Id");
                    String bookIdInput2 = sc.nextLine();
                    Integer bookId2 = validator.parseId(bookIdInput2);
                    if (bookId2 == null) {
                        System.out.println("Invalid book ID.");
                        break;
                    }
                                       
                    System.out.println("Please enter the borrow date (dd-MM-yyyy)");
                    String dateInput = sc.nextLine();
                    LocalDate borrowDate = validator.parseDate(dateInput);
                    if (borrowDate == null) {
                        borrowDate = LocalDate.now();
                        System.out.println("Defaulting to today's date...");
                    }
                    
                    
                    System.out.println("Please enter the return status (BORROWED, RETURNED, OVERDUE)");
                    String statusInput = sc.nextLine();
                    ReturnStatus returnStatus = validator.parseReturnStatus(statusInput);
                    if (returnStatus == null) {
                        returnStatus = ReturnStatus.RETURNED;
                        System.out.println("Defaulting to RETURNED...");
                    }
                    
                    if (borrowRecordService.addBorrowRecord(bookId2, memId2, borrowDate, returnStatus)) {
                        System.out.println("Successfully added borrow record");
                    } else {
                        System.out.println("Adding borrow record - unsuccessful");
                    }
                    
                    break;
                    
                case 3: // Update a borrowing record return status
                    
                    System.out.println("Enter borrow record Id to update: ");
                    Integer updateId = validator.parseId(sc.nextLine());
                    if (updateId == null) {
                        System.out.println("Invalid ID.");
                        break;
                    }
                    
                    System.out.println("Enter new return Status (leave blank to keep the same)");
                    String returnStatusInput = sc.nextLine();
                    ReturnStatus updateStatus = validator.parseReturnStatus(returnStatusInput);
                    
                    if (borrowRecordService.updateBorrowingStatus(updateId, updateStatus)) {
                        System.out.println("Successfully updated borrowing Status");
                    } else {
                        System.out.println("Failed to update borrowing status");
                    }
                    break;
                    
                case 4: // Delete borrowing record
                    System.out.println("Enter Borrow record ID to delete: ");
                    Integer deleteId = validator.parseId(sc.nextLine());
                    if (deleteId == null) {
                        System.out.println("Invalid ID.");
                        break;
                    }
                    
                    if (borrowRecordService.deleteBorrowRecord(deleteId)) {
                        System.out.println("Borrow record deleted successfully");
                    } else {
                        System.out.println("delete failed");
                    }
                    break;
                    
                case 5:
                    System.out.println("Returning to main menu...");
                    break;
                    
                default:
                    System.out.println("Invalid choice");
                    
            }
        } while (choice != 5);
            
    }
    
    
    
    public void showBorrowingRecordsMenu() {
        System.out.println("---------- Borrowing Records Management Menu ----------");
        System.out.println("1. Borrow a book");
        System.out.println("2. Add a borrowing record");
        System.out.println("3. Update a borrowing record");
        System.out.println("4. Delete a borrowing record");
        System.out.println("5. Exit Borrowing Record Management");
        
        System.out.println("Please enter your Choice (1-5):");
    }
    
    
    
    
    
    //SEARCH RECORDS -----------------------
    
    public void searchRecords() {
        
        int choice = -1;
        
        do {
            showSearchRecordsMenu();
            
            String input  = sc.nextLine();
            if (!input.matches("\\d+")) {
                System.out.println("Invalid input. Enter a number.");
                continue;
            }
            
            choice = Integer.parseInt(input);
            
            switch (choice) {
                case 1: //Display all borrowing records
                    System.out.println("\n--- All Borrowing Records ---");
                    for (BorrowRecord b : borrowRecordService.getAllBorrowRecords()) {
                        System.out.println("\n" + b);
                    }
                    break;
                    
                case 2: //Display a member's borrowing history
                    System.out.println("Please enter a member Id: ");
                    Integer memId = validator.parseId(sc.nextLine());
                    if (memId == null) {
                        System.out.println("Invalid ID.");
                        break;
                    }
                    
                    System.out.println("Member " + memId + "'s borrowing history:");
                    for (BorrowRecord bR : borrowRecordService.getMemberBorrowRecords(memId)) {
                        System.out.println("\n" + bR);
                    }
                    break;

                case 3: //Display a book's borrowing history
                    System.out.println("Please enter a book Id: ");
                    Integer bookId = validator.parseId(sc.nextLine());
                    if (bookId == null) {
                        System.out.println("Invalid ID.");
                        break;
                    }
                    
                    System.out.println("Book " + bookId + "'s borrowing history:");
                    for (BorrowRecord bRec : borrowRecordService.getBookBorrowRecords(bookId)) {
                        System.out.println("\n" + bRec);
                    }
                    break;
                case 4:
                    break;
                    
                default:
                    System.out.println("Invalid Choice");
            }
        } while (choice != 4);
        
        
        
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