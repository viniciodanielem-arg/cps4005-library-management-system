/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vini.cps4005.library;

import com.vini.cps4005.library.util.DatabaseConnection;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection conn = DatabaseConnection.connect();

        if (conn != null) {
            System.out.println("Connected to SQLite successfully.");
        } else {
            System.out.println("Connection failed.");
        }
    }
}
