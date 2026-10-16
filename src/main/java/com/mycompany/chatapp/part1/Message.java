/*
Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package com.mycompany.chatapp.part1;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Pattern;
/**
 *
 * @author ST10476385 KN SHIKWAMBANA
 */

//This is my Message class, it handles everything to do with the messages the user
//sends. It makes the message ID, works out the hash, checks the recipient number,
//and stores the messages in a JSON file. I kept it all in one class like the assignment asked.
public class Message {

    // These are the details for each message, I made them private so they can only be changed through my methods.
    
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    // These are static because they need to be shared across every Message object.
    //totalMessagesSent is what actually got sent, messageCounter keeps track of how
    //many messages have been created. I started the counter at -1 so the first
    //message gets number 0, which matches the example in the assignment.
    private static int totalMessagesSent = 0;
    private static int messageCounter = -1;

    //I'm keeping all the messages in this string so printMessages() can show them all at the end of the program run.
    
    private static String allMessages = "";

    public Message(String recipient, String messageText)
    
    {
        //I make the ID first because the hash needs it.
        this.messageID = generateMessageID();
        //messageNumber only goes up each time a new Message is made.
        
        
        this.messageNumber = ++messageCounter;
        this.recipient = recipient;
        this.messageText = messageText;
        
        
        //The hash is made last because it needs everything else.
        this.messageHash = createMessageHash();
    }

    //I made this method to generate the 10-digit random number. I used Random
    //and a loop that adds one digit at a time until I have 10 digits.
    private String generateMessageID() 
    {
        Random random = new Random();
        String id = "";
        
        for (int i = 0; i < 10; i++)
        
        {
            id = id + random.nextInt(10);
        }
        return id;
    }

    //This just checks the ID isn't longer than 10 characters. It should never fail because generateMessageID() always makes exactly 10.
   
    public boolean checkMessageID()
    {
        if (messageID == null) 
        {
            return false;
        }
        return messageID.length() <= 10;
    }

    //This checks the recipient number. It needs to start with the SA international
    //code (+27) and the digits after that can't be more than 10.
    // @citation: Stack Overflow. (2026) Regular expression to match South African cell phone numbers. Available at: https://stackoverflow.com/questions/16699007/ (Accessed: 20 August 2026).
    public String checkRecipientCell() {
        if (recipient == null) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        //The pattern is +27 followed by 1 to 10 digits.
        String cellPattern = "^\\+27[0-9]{1,10}$";
        if (Pattern.matches(cellPattern, recipient))
        
        {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    //This makes the message hash. It's made up of the first 2 digits of the ID,
    //a colon, the message number, another colon, and the first and last words
    //of the message. Everything is in caps.
    public String createMessageHash()
    
    {

        if (messageText == null || messageText.trim().equals(""))
        
        {
            return "";
        }

        //I take just the first 2 characters of the ID using substring.
        String firstTwo = messageID.substring(0, 2);

        //The message number as a string.
        String numberPart = String.valueOf(messageNumber);

        //I split the message into words so I can grab the first and last ones.
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        //I strip out punctuation so the hash only has letters. Otherwise
        //things like "tonight?" would end up in the hash with the question mark.
        firstWord = firstWord.replaceAll("[^a-zA-Z]", "");
        lastWord = lastWord.replaceAll("[^a-zA-Z]", "");

        //Put it all together and make it uppercase.
        String hash = firstTwo + ":" + numberPart + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    //This lets the user pick if they want to send, store, or disregard the message.
    //1 = send, 2 = disregard, 3 = store for later.
    public String sentMessage(int choice) 
    
    {

        if (choice == 1) {
            //Only add to the total when the user actually sends it.
            totalMessagesSent++;
            storeMessage();
            return "Message successfully sent.";
        } else if (choice == 2)
        
        {
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            storeMessage();
            return "Message successfully stored.";
        } else {
            return "Invalid option.";
        }
    }

    //This returns all the messages that were sent during the program run.
    public static String printMessages() 
    
    {
        if (allMessages.equals("")) {
            return "No messages sent yet.";
        }
        return allMessages;
    }

    //This just gives back how many messages were actually sent.
    public static int returnTotalMessages()
    
    {
        return totalMessagesSent;
    }

    //I researched how to store messages in a JSON file. I'm building the JSON
    //text myself and writing it to a file with FileWriter.
    // @citation: Oracle. (2026) Java FileWriter Documentation. Available at: https://docs.oracle.com/javase/8/docs/api/java/io/FileWriter.html (Accessed: 10 October 2026).
    // @citation: W3Schools. (2026) JSON Syntax. Available at: https://www.w3schools.com/js/js_json_syntax.asp (Accessed: 10 October 2026).
    public void storeMessage() {

        //I add the message to the running list every time storeMessage is called,
        //      so printMessages() can show all of them at the end.
        allMessages = allMessages
                + "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText + "\n"
                + "---\n";

        //   Builds the JSON text for this one message.
        String json = "{\n"
                + "  \"MessageID\": \"" + messageID + "\",\n"
                + "  \"MessageNumber\": " + messageNumber + ",\n"
                + "  \"Recipient\": \"" + recipient + "\",\n"
                + "  \"Message\": \"" + messageText + "\",\n"
                + "  \"MessageHash\": \"" + messageHash + "\"\n"
                + "}\n";

        //    Writes it to messages.json in the project folder using append mode.
        try {
            FileWriter writer = new FileWriter("messages.json", true);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }

    //These are just getters so other classes can read the values.
    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    //This shows the message details in the right order: ID, hash, recipient, message.
    public String displayMessageDetails() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText;
    }
}