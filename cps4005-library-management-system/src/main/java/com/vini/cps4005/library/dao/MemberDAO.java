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
            System.out.println("Books Table Ready!");
        
        } catch (SQLException e) {
            System.out.println("Error creating books table: " + e.getMessage());
        } 
    }
    
}
