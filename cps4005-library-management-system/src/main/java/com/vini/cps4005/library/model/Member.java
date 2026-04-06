/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vini.cps4005.library.model;

/**
 *
 * @author Daniele
 */
public class Member {
    private int memberId;
    private String name;
    private String email;
    private MembershipType membershipType;
    
    public Member(int memberId, String name, String email, MembershipType membershipType){
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.membershipType = membershipType;
    }


    //Getters
    public int getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public MembershipType getMembershipType() { return membershipType; }
    
    //Setters
    public void setMemberId(int memberId) { this.memberId = memberId; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setMembershipType(MembershipType membershipType) { this.membershipType = membershipType; }
    
    @Override
    public String toString() {
        return "Member ID: " + memberId +
               ", Name: " + name +
               ", Email: " + email +
               ", Membership type: " + membershipType;
    }
    
}
