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
import com.vini.cps4005.library.dao.BorrowRecordDAO;
import com.vini.cps4005.library.model.Book;
import com.vini.cps4005.library.model.BookStatus;

import java.util.List;

public class BookService {
    
    private final BookDAO bookDAO;
    
    public BookService() {
        this.bookDAO = new BookDAO();
    }
    
    // Create book object
    public boolean addBook(String title, String author, String category) {
        
        if (title == null || title.isBlank()) return false;
        if (author == null || author.isBlank()) return false;
        if (category == null || category.isBlank()) return false;
        
        Book book = new Book(title, author, category, BookStatus.AVAILABLE);
        
        return bookDAO.addBook(book);
        
    }
    
    // Read all books
    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }
    
    
    // Read by Id
    public Book getBookById(int id) {
        return bookDAO.getBookById(id);
    }
    
    
    
    // Search for a book
    public List<Book> searchByTitle(String title) {
        return bookDAO.searchBooksByTitle(title);
    }
    
    public List<Book> searchByAuthor(String author) {
        return bookDAO.searchBooksByAuthor(author);
    }    
    
    
    
    public boolean updateBook(int id, String title, String author, String category) {
        
        Book existing = bookDAO.getBookById(id);
        
        if (existing == null) return false;
        
        if(title == null || title.isBlank()) title = existing.getTitle();
        if(author == null || author.isBlank()) author = existing.getAuthor();
        if(category == null || category.isBlank()) category = existing.getCategory();        
        
        Book updatedBook = new Book(
                id,
                title,
                author,
                category,
                existing.getAvailabilityStatus()
        );
        
        return bookDAO.updateBook(updatedBook);
        
    }
    
    
    
    public boolean deleteBook(int id) {
        Book existing = bookDAO.getBookById(id);
        if (existing == null) {
            System.out.println("Book not found.");
            return false;
        }

        BorrowRecordDAO borrowDAO = new BorrowRecordDAO();
        if (borrowDAO.bookHasActiveLoans(id)) {
            System.out.println("Cannot delete book with active loans.");
            return false;
        }

        return bookDAO.deleteBook(id);
    }
    
    public List<Book> searchByCategory(String category) {
        return bookDAO.searchBooksByCategory(category);
    }
    
    public List<Book> getAllBooksSortedByTitle(boolean ascending) {
       return bookDAO.getAllBooksSortedByTitle(ascending);
    }
    
    
}
