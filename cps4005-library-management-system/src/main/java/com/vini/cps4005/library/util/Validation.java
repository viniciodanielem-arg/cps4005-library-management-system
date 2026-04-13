/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vini.cps4005.library.util;

/**
 *
 * @author Daniele
 */

import com.vini.cps4005.library.model.MembershipType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Validation {
    
    
    //DATE
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
                             .withResolverStyle(ResolverStyle.STRICT);

    public LocalDate parseDate(String input) {
        try {
            return LocalDate.parse(input, FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    
    
    //EMAIL
    public boolean isValidEmail(String email) {
        
        if (email == null) return false;
    
        email = email.trim();
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    
        return email.matches(emailRegex);
    }
    
    
    //ID
    public Integer isValidID(String input) {
        if (input == null) return null;
        
        input = input.trim();
        
        if (!input.matches("\\d+") || input.equals("0")) {
            return null;
        }
        
        return Integer.parseInt(input);
    }
    
    //MembershipType
    public MembershipType parseMembershipType(String input) {
    if (input == null) return null;

    input = input.trim().toUpperCase();

    try {
        return MembershipType.valueOf(input);
    } catch (IllegalArgumentException e) {
        return null;
    }
} 
}
