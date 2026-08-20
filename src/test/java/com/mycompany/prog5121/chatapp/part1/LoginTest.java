
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.prog5121.chatapp.part1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Part 1 – Registration and Login.
 * 
 *
 * @author ST10476385 KN SHIKWAMBANA
 */
public class LoginTest {

    // == assertEquals TESTS ==

    // Test: Username correctly formatted (kyl_1) -> should result in successful registration and login
    @Test
    public void testUsernameCorrectlyFormatted_Flow() {
        Login login = new Login();
        String regMsg = login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "John", "Doe");
        assertEquals("Username successfully captured. Password successfully captured. Cell phone number successfully added.", regMsg);
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
        assertEquals("Welcome John, Doe it is great to see you again.", login.returnLoginStatus());
    }

    // Test: Username incorrectly formatted (kyle!!!!!!!) -> should return the error message
    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login login = new Login();
        String msg = login.registerUser("kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976", "John", "Doe");
        assertEquals("Username is not correctly formatted; please ensure that your username "
                + "contains an underscore and is no more than five characters in length.", msg);
        
        
        
    }

    // Test: Password meets complexity requirements (Ch&&sec@ke99!) -> should return success message
    @Test
    public void testPasswordMeetsComplexity() {
        Login login = new Login();
        
        // @citation: Smith, J. (2023) Java Regex Tutorial. Available at: https://example.com/java-regex (Accessed: 20 August 2026).
        // Tests a valid password through registration and directly verifies password complexity.
// The Login class does not provide a separate password message method.
        Login login2 = new Login();
        String result = login2.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "John", "Doe");
        assertTrue(result.startsWith("Username successfully captured"));
        
        
        // Also test the boolean check directly.
        assertTrue(login2.checkPasswordComplexity("Ch&&sec@ke99!"));
        
    }

    // Test: Password does not meet complexity requirements (password) -> should return error message
    @Test
    public void testPasswordDoesNotMeetComplexity() {
        Login login = new Login();
        
        String msg = login.registerUser("kyl_1", "password", "+27838968976", "John", "Doe");
        assertEquals("Password is not correctly formatted; please ensure that the password "
                + "contains at least eight characters, a capital letter, a number, and a special character.", msg);
    }

    // Test: Cell phone correctly formatted (+27838968976) -> should return success message
    // Note: Your checkCellPhoneNumber expects +27 and exactly 1-10 digits after, which passes.
    @Test
    public void testCellPhoneCorrectlyFormatted() {
        Login login = new Login();
        String msg = login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "John", "Doe");
        
        
        // Check that registration succeeds (which implies phone was accepted)
        assertTrue(msg.startsWith("Username successfully captured"));
        
        
        // Also test the boolean directly
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    // Test: Cell phone incorrectly formatted (08966553) -> should return error message
    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        Login login = new Login();
        
        String msg = login.registerUser("kyl_1", "Ch&&sec@ke99!", "08966553", "John", "Doe");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an "
                + "international code; please correct the number and try again.", msg);
    }

    // == assertTrue or assertFalse TESTS ==

    // Login Successful (correct credentials)
    @Test
    public void testLoginSuccessful() {
        Login login = new Login();
        
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "John", "Doe");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    // Login Failed (wrong password)
    @Test
    public void testLoginFailed() {
        Login login = new Login();
        
        
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "John", "Doe");
        assertFalse(login.loginUser("kyl_1", "wrong"));
        
    }

    // Username correctly formatted (kyl_1) -> true
    @Test
    public void testUsernameCorrectlyFormatted_Boolean() {
        Login login = new Login();
        assertTrue(login.checkUserName("kyl_1"));
    }

    // Username incorrectly formatted (kyle!!!!!!!) -> false
    @Test
    public void testUsernameIncorrectlyFormatted_Boolean() {
        Login login = new Login();
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    // Password meets complexity requirements (Ch&&sec@ke99!) -> true
    @Test
    public void testPasswordMeetsComplexity_Boolean() {
        Login login = new Login();
        
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    // Password does not meet complexity requirements (password) -> false
    @Test
    public void testPasswordDoesNotMeetComplexity_Boolean() {
        Login login = new Login();
        
        assertFalse(login.checkPasswordComplexity("password"));
        
    }

    // Cell phone correctly formatted (+27838968976) -> true
    @Test
    public void testCellPhoneCorrectlyFormatted_Boolean() {
        Login login = new Login();
        
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
        
    }

    // Cell phone incorrectly formatted (08966553) -> false
    @Test
    public void testCellPhoneIncorrectlyFormatted_Boolean() {
        Login login = new Login();
        
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }
    
    
}
