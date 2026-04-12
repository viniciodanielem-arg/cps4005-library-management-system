/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vini.cps4005.library.service;

import com.vini.cps4005.library.dao.*;
import com.vini.cps4005.library.model.*;
import java.time.LocalDate;

/**
 *
 * @author Daniele
 */
public class BorrowService {
    
    private BorrowRecordDAO borrowDAO;
    private MemberDAO memberDAO;
    private BookDAO bookDAO;
    
    
    
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
    
}
