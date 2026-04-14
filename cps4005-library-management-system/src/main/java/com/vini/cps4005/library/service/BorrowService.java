/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vini.cps4005.library.service;

import com.vini.cps4005.library.dao.BookDAO;
import com.vini.cps4005.library.dao.BorrowRecordDAO;
import com.vini.cps4005.library.dao.MemberDAO;
import com.vini.cps4005.library.model.Book;
import com.vini.cps4005.library.model.BorrowRecord;
import com.vini.cps4005.library.model.Member;
import com.vini.cps4005.library.model.ReturnStatus;
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
            System.out.println("Member does not exist.");
            return false;
        }

        if (book == null) {
            System.out.println("Book does not exist.");
            return false;
        }

        if (!"Available".equalsIgnoreCase(book.getAvailabilityStatus())) {
            System.out.println("Book is unavailable.");
            return false;
        }

        MemberService memberService = new MemberService();
        if (!memberService.canBorrow(member.getMemberId(), member.getMembershipType())) {
            System.out.println("Borrowing limit reached.");
            return false;
        }

        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusWeeks(4);

        if (!dueDate.isAfter(borrowDate)) {
            System.out.println("Due date must be later than borrow date.");
            return false;
        }

        BorrowRecord record = new BorrowRecord(
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

    public boolean addBorrowRecord(int bookId, int memberId, LocalDate borrowDate, ReturnStatus returnStatus) {
        if (borrowDate == null) {
            System.out.println("Borrow date is required.");
            return false;
        }

        if (returnStatus == null) {
            System.out.println("Return status is required.");
            return false;
        }

        Member member = memberDAO.getMemberById(memberId);
        if (member == null) {
            System.out.println("Member does not exist.");
            return false;
        }

        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            System.out.println("Book does not exist.");
            return false;
        }

        if ((returnStatus == ReturnStatus.BORROWED || returnStatus == ReturnStatus.OVERDUE)
                && !"Available".equalsIgnoreCase(book.getAvailabilityStatus())) {
            System.out.println("Book is not currently available.");
            return false;
        }

        LocalDate dueDate = borrowDate.plusWeeks(4);

        if (!dueDate.isAfter(borrowDate)) {
            System.out.println("Due date must be later than borrow date.");
            return false;
        }

        BorrowRecord record = new BorrowRecord(
                bookId,
                memberId,
                borrowDate,
                dueDate,
                returnStatus
        );

        boolean added = borrowDAO.addBorrowRecord(record);
        if (!added) {
            return false;
        }

        if (returnStatus == ReturnStatus.RETURNED) {
            book.setAvailabilityStatus("Available");
        } else {
            book.setAvailabilityStatus("Borrowed");
        }

        bookDAO.updateBook(book);
        return true;
    }

    public boolean updateBorrowingStatus(int recordId, ReturnStatus returnStatus) {
        BorrowRecord existing = borrowDAO.searchById(recordId);

        if (existing == null) {
            System.out.println("Borrow record not found.");
            return false;
        }

        if (returnStatus == null) {
            returnStatus = existing.getReturnStatus();
        }

        boolean updated = borrowDAO.updateBorrowingStatus(recordId, returnStatus);
        if (!updated) {
            return false;
        }

        Book book = bookDAO.getBookById(existing.getBookId());
        if (book != null) {
            if (returnStatus == ReturnStatus.RETURNED) {
                book.setAvailabilityStatus("Available");
            } else {
                book.setAvailabilityStatus("Borrowed");
            }
            bookDAO.updateBook(book);
        }

        return true;
    }

    public boolean deleteBorrowRecord(int recordId) {
        BorrowRecord existing = borrowDAO.searchById(recordId);

        if (existing == null) {
            System.out.println("Borrow record not found.");
            return false;
        }

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
    
    public int updateOverdueRecords() {
        return borrowDAO.markOverdueRecords();
    }

    public List<BorrowRecord> getOverdueRecords() {
        return borrowDAO.getOverdueRecords();
    }
}
