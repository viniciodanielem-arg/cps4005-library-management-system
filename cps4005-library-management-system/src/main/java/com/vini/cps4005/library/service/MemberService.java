/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vini.cps4005.library.service;

import com.vini.cps4005.library.model.MembershipType;
import com.vini.cps4005.library.dao.MemberDAO;

/**
 *
 * @author Daniele
 */
public class MemberService {
        
    MemberDAO memberDAO = new MemberDAO();
    
    public boolean canBorrow(int memberId, MembershipType type) {
        int currentBorrowed = memberDAO.getActiveBorrowCount(memberId);
        
        return currentBorrowed < type.getBorrowingLimit();
    }
}
