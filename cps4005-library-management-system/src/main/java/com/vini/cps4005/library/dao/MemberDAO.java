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
import com.vini.cps4005.library.model.Member;
import com.vini.cps4005.library.model.MembershipType;

public class MemberDAO {
    /*
    Member Management:
    ▪ Create: Register new members with details such as Member ID, Name, Email, and Membership Type.
    ▪ Retrieve: Display member information, Search members by name or ID.
    ▪ Update: Modify member details.
    ▪ Delete: Remove members from the system when required.
    */
    
    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS members (
                    member_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    member_name TEXT NOT NULL,
                    email TEXT NOT NULL,
                    membership_type TEXT NOT NULL
                )
                """;
        
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            System.out.println("Members Table Ready!");
        
        } catch (SQLException e) {
            System.out.println("Error creating members table: " + e.getMessage());
        } 
    }
    
    public boolean addMember(Member member) {
        String sql = "INSERT INTO members (member_id, member_name, email, membership_type) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, member.getMemberId());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getMembershipType().name());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error inserting member into table: " + e.getMessage());
            return false;
        }
    }
    
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while(rs.next()) {
                Member member = new Member(
                            rs.getInt("member_id"),
                            rs.getString("member_name"),
                            rs.getString("email"),
                            MembershipType.valueOf(rs.getString("membership_type"))
                );
                members.add(member);
            }
                    
        } catch (SQLException e) {
            System.out.println("Error retrieving members: " + e.getMessage());
        }
        
        return members;
    }
    
    public Member getMemberById(int memberId) {
        String sql = "SELECT * FROM members WHERE member_id = ?";
        
        try (Connection conn = DatabaseConnection.connect();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Member(
                        rs.getInt("member_id"),
                        rs.getString("member_name"),
                        rs.getString("email"),
                        MembershipType.valueOf(rs.getString("membership_type"))
                );
            }
        } catch (SQLException e) {
            System.out.println("Error finding member: " + e.getMessage());
        }
        
        return null;
    }
    
    
    public List<Member> getMemberByName(String name) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE member_name LIKE ?";
        
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                members.add(new Member(
                        rs.getInt("member_id"),
                        rs.getString("member_name"),
                        rs.getString("email"),
                        MembershipType.valueOf(rs.getString("membership_type")
                        )));
            }
            
        } catch (SQLException e) {
            System.out.println("Error searching members: " + e.getMessage());
        } 
        
        return members;
    }
    
    public boolean updateMember(Member member) {
        String sql = "UPDATE members SET member_name = ?, email = ?, membership_type = ? WHERE member_id = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getMembershipType().toString());
            pstmt.setInt(4, member.getMemberId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error updating member: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteMember(int memberId) {
        String sql = "DELETE FROM members WHERE member_id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting member: " + e.getMessage());
            return false;
        }
    }

    public int getActiveBorrowCount(int memberId) {
        String sql = "SELECT COUNT(*) AS total FROM borrow_records WHERE member_id = ? AND return_status = 'Borrowed'";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.out.println("Error getting borrow count: " + e.getMessage());
        }

            return 0;
    } 
    
}
