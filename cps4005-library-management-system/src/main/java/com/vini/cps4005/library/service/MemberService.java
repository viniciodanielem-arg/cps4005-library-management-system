/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vini.cps4005.library.service;

import com.vini.cps4005.library.model.MembershipType;
import com.vini.cps4005.library.dao.MemberDAO;
import com.vini.cps4005.library.model.Member;

import java.util.List;

/**
 *
 * @author Daniele
 */
public class MemberService {
        
    private final MemberDAO memberDAO;
    
    public MemberService() {
        this.memberDAO = new MemberDAO(); 
    }
    
    // Check if member has reached their limit: 
    public boolean canBorrow(int memberId, MembershipType type) {
        int currentBorrowed = memberDAO.getActiveBorrowCount(memberId);
        
        return currentBorrowed < type.getBorrowingLimit();
    }
    
    // Create Member Object
    
    public boolean addMember(String name, String email, MembershipType membershipType) {
        
        if (name == null || name.isEmpty()) return false;
        if (email == null || email.isEmpty()) return false;
        if (membershipType == null || membershipType.toString().isEmpty()) return false;
        
        Member member = new Member(name, email, membershipType);
        
        return memberDAO.addMember(member);
    }
    
    // Read all members from db
    
    public List<Member> getAllMembers() {
        return memberDAO.getAllMembers();
    }
    
    // Read by Id
    
    public Member getMemberById(int id) {
        return memberDAO.getMemberById(id);
    }
    
    // Read by name
    
    public List<Member> getMemberByName(String name) {
        return memberDAO.getMemberByName(name);
    }
    
    // Update 
    
    public boolean updateMember(int id, String name, String email, MembershipType membershipType) {
        Member existing = memberDAO.getMemberById(id);
        if (existing == null) return false;
        
        if(name == null || name.isBlank()) name = existing.getName();
        if(email == null || email.isBlank()) email = existing.getEmail();
        if(membershipType == null || membershipType.toString().isBlank()) membershipType = existing.getMembershipType();
    
        Member updatedMember = new Member(
                id,
                name,
                email,
                membershipType
        );
        
        return memberDAO.updateMember(updatedMember);
        
    }
    
    // Delete
    
    public boolean deleteMember(int id) {
        return memberDAO.deleteMember(id);
    }
}
