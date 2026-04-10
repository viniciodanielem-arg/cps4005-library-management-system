/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vini.cps4005.library.model;

/**
 *
 * @author Daniele
 */
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BorrowRecord {
    private int recordId;
    private int bookId;
    private int memberId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private ReturnStatus returned;
    
    public BorrowRecord(
            
            int recordId, 
            int bookId, 
            int memberId, 
            LocalDate borrowDate, 
            LocalDate dueDate, 
            ReturnStatus returned) {
        
        this.recordId = recordId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returned = returned;   
              
    }
    
    public int getRecordId() { return recordId; }
    public int getBookId() { return bookId; }
    public int getMemberId() { return memberId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public ReturnStatus getReturnStatus() { return returned; }
    
    public void setRecordId(int recordId) { this.recordId = recordId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    public void setBorrowDate(LocalDate borrowDate) {this.borrowDate = borrowDate; }
    public void setDueDate(LocalDate dueDate) {this.dueDate = dueDate; }
    public void setReturnStatus(ReturnStatus returned) { this.returned = returned; }
    
    @Override
    public String toString() {
        return "Record ID: " + recordId +
               ", Book ID: " + bookId +
               ", Member ID: " + memberId +
               ", Borrow date: " + borrowDate +
               ", Due Date: " + dueDate +
               ", Return status: " + returned;
    }
                
}
