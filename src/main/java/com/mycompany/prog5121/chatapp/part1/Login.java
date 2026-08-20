/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121.chatapp.part1;

import java.util.regex.Pattern;
 
/**
 *
 * @author ST10476385 KN SHIKWAMBANA
 */
 
//This is my Login class, it basically does 2 things, registers a new user (checking their details are correct first)
  // and then lets them log back in with the same details. I kept everything in one class since the
//assignment said to make a Login class with all these methods in it.

public class Login {
 
    // These are the details for the person once they've registered, I made them private because I only
     //want them changed through my methods, not just from anywhere.
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;
 
   //This just remembers if the last login attempt worked or not
       //I need this so returnLoginStatus() knows which message to send back.
            private boolean loginSuccessful;
 
    public Login() {
       
        // I Don't need to set anything here, the fields only get filled in once registerUser() runs successfully.
       
    }
 
    //It checks if the username has an underscore in it somewhere and isn't longer than 5 characters.
       // I used .contains() to check for the underscore because it's the easiest way to check if a character is
            // anywhere in a string.
    public boolean checkUserName(String username) {
       
        if (username == null)
       
        {
            // I Added this check because if someone passes in null it would crash on .contains(),
            // rather just say it's not valid.
    return false;
    
        }
       
        return username.contains("_") && username.length() <= 5;
    }
 
    //This checks the password has 8+ characters, a capital letter, a number and a special character in it.
  //I used regex for this because trying to do it with if statements and loops would have been way longer.
   
    public boolean checkPasswordComplexity(String password) {
        if (password == null) {
            return false;
        }
        //Regex idea for this one is referenced in the README, changed it around a bit to fit what the assignmnent
       // actually asks for.
       
        String passwordPattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }
 
    // checks the cell number starts with +27 (SA country code) and that
    // whatever comes after that isn't more than 10 digits. I used the same
    // Pattern.matches() approach as the password check above since it
    // already worked there.
    
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return false;
        }
       // @citation: GeeksforGeeks. (2026) Regular Expressions in Java. Available at: https://www.geeksforgeeks.org/regular-expressions-in-java/ (Accessed: 20 August 2026).
        // Regex idea for this one is also referenced in the README, edited down to just SA numbers with the +27 code.
        String cellPattern = "^\\+27[0-9]{1,10}$";
        return Pattern.matches(cellPattern, cellPhoneNumber);
    }
 
    // this runs all 3 checks above in order and stops at the first one that fails, so the user knows
    // exactly what to fix first instead ofgetting every error at once.
    public String registerUser(String username, String password, String cellPhoneNumber,
    String firstName, String lastName) {
 
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username "
                    +"contains an underscore and is no more than five characters in length.";
        }
        
        
 
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, and a special character.";
        }
 
        
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell phone number is incorrectly formatted or does not contain an "
            +"international code; please correct the number and try again.";
        }
 
        //It only gets here if all 3 checks passed, so now it's safe to actually save the details to the fields.
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
 
        return "Username successfully captured. Password successfully captured. Cell phone number successfully added.";
   
    }
 
    //It checks whatever was typed in at login matches what got saved when the person registered.
    //Used .equals() instead of == because that's how we are supposed to compare Strings in Java,
    //== compares references not the actual text.
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (username == null || password == null) {
           
           
      //this means nobody has registered yet so there's nothing to log into.
            loginSuccessful = false;
            return false;
        }
 
        loginSuccessful = username.equals(enteredUsername) && password.equals(enteredPassword);
        return loginSuccessful;
    }
 
    //Tust sends back whichever message matches the last login attempt, used the boolean
                // I set in loginUser() so I didn't have to redo the whole check again in here.
    public String returnLoginStatus() {
        if (loginSuccessful) {
          return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        }
        
        
        else
        {
       
            return "Username or password incorrect, please try again.";
        }
    }
 
    
    
    //I added these getters so Main can grab the name if it needs to print it somewhere else.
    public String getFirstName() {
       
     return firstName;
    }
 
    public String getLastName()
   
    {
      return lastName;
      
      
    }
}