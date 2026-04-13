/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vini.cps4005.library.service;

/**
 *
 * @author Daniele
 */
import com.vini.cps4005.library.dao.BookDAO;
import com.vini.cps4005.library.model.Book;
import java.util.ArrayList;
import java.util.Scanner;

public class BookService {
    
    Scanner sc = new Scanner(System.in);
    BookDAO bookDAO = new BookDAO();
    
    public void addBook() {
        // ASK USER FOR BOOK DETAILS: title, author category
        // book_id is authomatic and set the availability_status to RETURNED
        // then add the book to the database (use validation beforehand)
        
        System.out.println("Please enter a book title: ");
        String titleInput  = sc.nextLine();

        System.out.println("Please enter an author: ");
        String authorInput  = sc.nextLine();

        System.out.println("Please enter a category: ");
        String categoryInput  = sc.nextLine();
        
        Book addingBook = new Book(titleInput, authorInput, categoryInput, "Available");
        
        if (bookDAO.addBook(addingBook)) {
            System.out.println("Successfully added book");
        }
        
        
    }
    
    public void displayAllBooks() {
        ArrayList<Book> allBooks = (ArrayList<Book>) bookDAO.getAllBooks();
        
        for (Book book : allBooks) {
            book.toString();
            System.out.print("\n");
        }
    }
    
    public void searchForBook() {
        int choice;
        
        do {
            System.out.println("Choose search method: ");
            System.out.println("1. Search by Id");
            System.out.println("2. Search by title");
            System.out.println("3. Search by author");
            System.out.println("4. Go Back");
            
            choice = Integer.parseInt(sc.nextLine());
            
            switch (choice) {
                case 1:
                    System.out.println("Enter an Id: ");
                    int id = sc.nextInt();
                    Book book1 = bookDAO.getBookById(id);
                    System.out.println(book1.toString());
                    break;
                    
                case 2:
                    System.out.println("Enter a title: ");
                    String title = sc.nextLine();
                    ArrayList<Book> booksFound1 = (ArrayList<Book>) bookDAO.searchBooksByTitle(title);
                    for (Book book : booksFound1) {
                        System.out.println(book.toString());
                        System.out.print("\n");
                    }
                    break;
                    
                case 3:
                    System.out.println("Enter an Author: ");
                    String author = sc.nextLine();
                    ArrayList<Book> booksFound2 = (ArrayList<Book>) bookDAO.searchBooksByAuthor(author);
                    for (Book book : booksFound2) {
                        System.out.println(book.toString());
                        System.out.print("\n");
                    }
                    break;
                    
                case 4:
                    System.out.println("Returning...");
                    break;
                    
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 4);
    }
    
    public void updateBook() {
        System.out.println("Please enter a book ID number to proceed: ");
        int bookId = sc.nextInt();
        Book book = bookDAO.getBookById(bookId);
        
        System.out.println("\n Current book details: \n");
        System.out.println(book.toString() + "\n");
        
        System.out.println("Please enter title update");
        String titleInput  = sc.nextLine();

        System.out.println("Please enter author update");
        String authorInput  = sc.nextLine();

        System.out.println("Please enter category update");
        String categoryInput  = sc.nextLine();
        
        Book updatedBook = new Book(bookId, titleInput, authorInput, categoryInput, "Available");
        
        if (bookDAO.updateBook(updatedBook)) {
            System.out.println("Successfully updated book " + bookId + " info");
        }
        
        
    }
    
    public void deleteBook() {
        System.out.println("Please enter a book ID number to proceed: ");
        int bookId = sc.nextInt();
        
        if (bookDAO.deleteBook(bookId)) {
            System.out.println("Successfully deleted book " + bookId + " info");
        }
        
              
    }
}
