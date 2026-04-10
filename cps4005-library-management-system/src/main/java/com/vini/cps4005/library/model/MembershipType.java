/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.vini.cps4005.library.model;

/**
 *
 * @author Daniele
 */
public enum MembershipType {
    STUDENT(10),
    STAFF(20),
    ADMIN(30);
    
    private final int borrowingLimit;
    
    MembershipType(int borrowingLimit) {
        this.borrowingLimit = borrowingLimit;
    }
    
    public int getBorrowingLimit() {
        return borrowingLimit;
    }
}
