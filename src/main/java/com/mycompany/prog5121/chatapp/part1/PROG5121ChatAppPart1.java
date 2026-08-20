
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.prog5121.chatapp.part1;
import java.util.Scanner;
/**
 *
 * @author ST10476385 KN SHIKWAMBANA
 */
public class PROG5121ChatAppPart1 {

    public static void main(String[] args) {
       
        Scanner scanner = new Scanner(System.in);
       
        Login login = new Login();
 
        //Starting off with registration, asking for all the details one at a time
        // so it's easier to follow what's happening.
       
        System.out.println("=== Registration ===");
        System.out.print("Enter a username (must contain _ and be 5 characters or less): ");
        String username = scanner.nextLine();
 
        System.out.print("Enter a password (8+ characters, a capital letter, a number and a special character): ");
        String password = scanner.nextLine();
 
        System.out.print("Enter your cell number (with country code, example +27614880723): ");
        String cellNumber = scanner.nextLine();
 
       
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();
 
        //RegisterUser() does all the checking for me and just gives back message
        // saying what's wrong or that it worked, so I just print whatever it returns.
        String registerResult = login.registerUser(username, password, cellNumber, firstName, lastName);
        System.out.println(registerResult);
 
        //It only move on to login if registration actually succeeded, checking the start of the message
        // since that's the easiest way to tell if it went through without adding another return type.
       
        if (registerResult.startsWith("Username successfully captured")) {
            System.out.println();
           
            System.out.println("=== Login ===");
 
            System.out.print("Enter your username: ");
            String loginUsername = scanner.nextLine();
 
            System.out.print("Enter your password: ");
            String loginPassword = scanner.nextLine();
 
            login.loginUser(loginUsername, loginPassword);
            System.out.println(login.returnLoginStatus());
        }
       
 
        scanner.close();
    }
}