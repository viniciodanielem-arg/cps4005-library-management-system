/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vini.cps4005.library.service;

import com.vini.cps4005.library.dao.*;
import com.vini.cps4005.library.model.*;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Daniele
 */
public class BorrowService {
    
    private final BorrowRecordDAO borrowDAO;
    private final MemberDAO memberDAO;
    private final BookDAO bookDAO;

    public BorrowService() {
        this.borrowDAO = new BorrowRecordDAO();
        this.memberDAO = new MemberDAO();
        this.bookDAO = new BookDAO();
    }
    
    
    
    public boolean borrowBook(int memberId, int bookId) {
        Member member = memberDAO.getMemberById(memberId);
        Book book = bookDAO.getBookById(bookId);
        
        if (member == null) {
            System.out.println("Member does not exist");
            return false;
        }
        
        if (book == null) {
            System.out.println("Book does not exist");
            return false;
        }
        
        if (!"Available".equalsIgnoreCase(book.getAvailabilityStatus())) {
            System.out.println("Book is unavailable");
            return false;
        }
        
        MemberService memberService = new MemberService();
        if (!memberService.canBorrow(member.getMemberId(), member.getMembershipType())) {
            System.out.println("Borrowing limit reached");
            return false;
        }
        
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusWeeks(4);
        
        BorrowRecord record = new BorrowRecord(
                0,
                bookId,
                memberId,
                borrowDate, 
                dueDate,
                ReturnStatus.BORROWED
        );
        
        boolean recordAdded = borrowDAO.addBorrowRecord(record);
        
        if (!recordAdded) {
            return false;
        }
        
        book.setAvailabilityStatus("Borrowed");
        return bookDAO.updateBook(book);
    }
    
    public boolean addBorrowRecord(
            int bookId, 
            int memberId, 
            LocalDate borrowDate, 
            ReturnStatus returnStatus) {
        
        if (borrowDate == null) return false;
        if (returnStatus == null) return false;
        
        LocalDate dueDate = borrowDate.plusWeeks(4);
        BorrowRecord b = new BorrowRecord(bookId, memberId, borrowDate, dueDate, returnStatus);
        
        return borrowDAO.addBorrowRecord(b);
    }
    
    
    public boolean updateBorrowingStatus(int recordId, ReturnStatus returnStatus) {
        
        BorrowRecord existing = borrowDAO.searchById(recordId);
        
        if (existing == null) return false;
        if(returnStatus == null) returnStatus = existing.getReturnStatus();
        
        return borrowDAO.updateBorrowingStatus(recordId, returnStatus);
    
    }
    
    public boolean deleteBorrowRecord(int recordId) {
        BorrowRecord existing = borrowDAO.searchById(recordId);
        
        if (existing == null) return false;
        
        return borrowDAO.deleteBorrowRecord(recordId);
    }
    
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowDAO.getAllBorrowRecords();
    }
    
    public List<BorrowRecord> getMemberBorrowRecords(int memberId) {
        return borrowDAO.searchbyMember(memberId);
    }
    
    public List<BorrowRecord> getBookBorrowRecords(int bookId) {
        return borrowDAO.searchByBook(bookId);
    }
    
    
}
