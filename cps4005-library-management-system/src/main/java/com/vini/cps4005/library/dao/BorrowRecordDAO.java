/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vini.cps4005.library.dao;

/**
 *
 * @author Daniele
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.vini.cps4005.library.util.DatabaseConnection;
import com.vini.cps4005.library.model.BorrowRecord;
import com.vini.cps4005.library.model.ReturnStatus;

public class BorrowRecordDAO {
    /*
    Borrowing Management
    ▪ Create: Record new borrowing transactions including Book ID, Member ID, Borrow Date, and Due Date.
    ▪ Retrieve: Display borrowing history, Search records by member or book.
    ▪ Update: Update borrowing status (Returned, Overdue, Borrowed).
    ▪ Delete: Remove incorrect borrowing records.
    */
    
    public void createTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS borrow_records (
                        record_id INTEGER PRIMARY KEY,
                        book_id INTEGER NOT NULL,
                        member_id INTEGER NOT NULL,
                        borrow_date DATE NOT NULL,
                        due_date DATE NOT NULL,
                     return_status TEXT NOT NULL
                      )""";
        
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            System.out.println("Borrow records table created successfully");
            
        } catch (SQLException e) {
            System.out.println("Error creating borrow records table: "+ e.getMessage());
        }
    }
    
    public boolean addBorrowRecord(BorrowRecord b) {
        String sql = "INSERT INTO borrow_records (book_id, member_id, borrow_date, due_date, return_status) VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
            pstmt.setInt(1, b.getBookId());
            pstmt.setInt(2, b.getMemberId());
            pstmt.setDate(3, Date.valueOf(b.getBorrowDate()));
            pstmt.setDate(4, Date.valueOf(b.getDueDate()));
            pstmt.setString(5, b.getReturnStatus().name());
        
            return pstmt.executeUpdate() > 0;
        
        } catch (SQLException e) {
            System.out.println("Error adding borrow record: " + e.getMessage());
            return false;
        }
    }
    
    public List<BorrowRecord> getAllBorrowRecords() {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM borrow_records";
        
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                records.add(new BorrowRecord(
                        rs.getInt("record_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        ReturnStatus.valueOf(rs.getString("return_status"))
                        
                
                ));
            }
            
            
        } catch (SQLException e) {
            System.out.println("Error getting borrow records from database: " + e.getMessage());
        }
        
        return records;
    }
    
    public List<BorrowRecord> searchbyMember(int memberId) {
        String sql = "SELECT * FROM borrow_records WHERE member_id = ? ORDER BY borrow_date DESC";
        List<BorrowRecord> memberRecords = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                memberRecords.add(new BorrowRecord(
                        rs.getInt("record_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        ReturnStatus.valueOf(rs.getString("return_status"))
                        
                
                ));
            }
            
        } catch (SQLException e) {
            System.out.println("Error finding records (by member)" + e.getMessage());
        }
        
        return memberRecords;
    }


    public List<BorrowRecord> searchByBook(int bookId) {
        String sql = "SELECT * FROM borrow_records WHERE book_name LIKE ? ORDER BY borrow_date DESC";
        List<BorrowRecord> bookRecords = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                bookRecords.add(new BorrowRecord(
                        rs.getInt("record_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        ReturnStatus.valueOf(rs.getString("return_status"))
                        
                
                ));
            }
            
        } catch (SQLException e) {
            System.out.println("Error finding records (by book)" + e.getMessage());
        }
        
        return bookRecords;
    }
    
    public BorrowRecord searchById(int recordId) {
        String sql = "SELECT * FROM borrow_records WHERE record_id = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, recordId);
            ResultSet rs = pstmt.executeQuery();
            
            BorrowRecord foundBr = new BorrowRecord(
                        rs.getInt("record_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        ReturnStatus.valueOf(rs.getString("return_status"))
                );
            
            return foundBr;
            
        } catch (SQLException e) {
            System.out.println("Error finding records (by book)" + e.getMessage());
            return null;
        }
        
    }    
    
    public boolean updateBorrowingStatus(int recordId, ReturnStatus returnStatus) {
        String sql = "UPDATE borrow_records SET return_status = ? WHERE record_id = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, returnStatus.toString());
            pstmt.setInt(2, recordId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error updating borrowing status: " + e.getMessage());
            return false;
        }
    }
    
    
    public boolean deleteBorrowRecord(int recordId) {
        String sql = "DELETE FROM borrow_records WHERE record_id = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, recordId);
            int rowsAffected = pstmt.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error deleting record: " +e.getMessage());
            return false;        
        }
    }
    
    public boolean isTableEmpty() {
        String sql = "SELECT COUNT(*) FROM borrow_records";
        
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()){
                return rs.getInt(1) == 0;
            }
            
        } catch (SQLException e) {
            System.out.println("Error checking borrow records table: " + e.getMessage());
        }
        
        return true;
    }    
}