/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp.part1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author ST10476385 KN SHIKWAMBANA
 */

//This is my Message class, it handles everything to do with the messages the user
//sends. It makes the message ID, works out the hash, checks the recipient number,
//checks the message length and stores the messages in a JSON file. I kept it all
//in one class like the assignment asked.
public class Message {

    // These are the details for each message, I made them private so they can only be changed through my methods.
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    // These are static because they need to be shared across every Message object.
    //totalMessagesSent is how many messages were actually sent.
    private static int totalMessagesSent = 0;

    //I'm keeping all the SENT messages in this string so printMessages() can show them
    //at the end. Stored and disregarded messages are not added here, only sent ones.
    private static String allMessages = "";

    //These two are for the JSON file. storedEntries holds the JSON text of every stored
    //message and storedCount tells me if I need a comma before the next one.
    private static String storedEntries = "";
    private static int storedCount = 0;

    //This is the main constructor. The message number now comes from the for loop counter
    //in main (i), so the first message is 0 like the example in the assignment (00:0:HITONIGHT).
    public Message(int messageNumber, String recipient, String messageText)
    {
        this(generateMessageID(), messageNumber, recipient, messageText);
    }

    //This second constructor lets me pass in a fixed ID. I only use it in my unit tests,
    //because the real ID is random and I can't test the hash against a random number.
    public Message(String messageID, int messageNumber, String recipient, String messageText)
    {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        //The hash is made last because it needs everything else.
        this.messageHash = createMessageHash();
    }

    //I made this method to generate the 10-digit random number. I used Random
    //and a loop that adds one digit at a time until I have 10 digits.
    //It's static so the constructor can call it before the object exists.
    private static String generateMessageID()
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

    //This gives back the "Message ID generated" line the assignment wants shown.
    public String displayMessageID()
    {
        return "Message ID generated: " + messageID;
    }

    //This checks the message isn't longer than 250 characters. I moved it into its own
    //method (it used to be inside main) so that the unit tests test the real code
    //and not a copy of it. It's static because it doesn't need a Message object.
    public static String checkMessageLength(String text)
    {
        if (text.length() > 250)
        {
            //I work out how many characters over they are so the error is specific.
            int excess = text.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
        return "Message ready to send.";
    }

    //This checks the recipient number. Instead of writing the same regex again I reuse
    //checkCellPhoneNumber() from my Login class, since it's the exact same rule
    //(international code followed by the number). The regex is attributed in Login.java.
    public String checkRecipientCell()
    {
        Login login = new Login();
        if (login.checkCellPhoneNumber(recipient))
        {
            return "Cell phone number successfully captured.";
        }
        else
        {
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
        if (choice == 1)
        {
            //Only add to the total and the sent list when the user actually sends it.
            totalMessagesSent++;
            allMessages = allMessages
                    + "Message ID: " + messageID + "\n"
                    + "Message Hash: " + messageHash + "\n"
                    + "Recipient: " + recipient + "\n"
                    + "Message: " + messageText + "\n"
                    + "---\n";
            return "Message successfully sent.";
        }
        else if (choice == 2)
        {
            return "Press 0 to delete the message.";
        }
        else if (choice == 3)
        {
            storeMessage();
            return "Message successfully stored.";
        }
        else
        {
            return "Invalid option.";
        }
    }

    //This returns all the messages that were sent during the program run.
    public static String printMessages()
    {
        if (allMessages.equals(""))
        {
            return "No messages sent yet.";
        }
        return allMessages;
    }

    //This just gives back how many messages were actually sent.
    public static int returnTotalMessages()
    {
        return totalMessagesSent;
    }

    //This makes sure quotes and backslashes in a message don't break the JSON file.
    //For example a message with "hello" in it would end the JSON string too early otherwise.
    private static String escapeJson(String text)
    {
        String result = text.replace("\\", "\\\\");
        result = result.replace("\"", "\\\"");
        result = result.replace("\n", "\\n");
        return result;
    }

    //I researched how to store messages in a JSON file. I'm building the JSON
    //text myself and writing it to a file with FileWriter. Every stored message goes
    //into one JSON array, and I rewrite the whole file each time so it is always valid JSON.
    // @citation: Oracle. (2026) Java FileWriter Documentation. Available at: https://docs.oracle.com/javase/8/docs/api/java/io/FileWriter.html (Accessed: 10 October 2026).
    // @citation: W3Schools. (2026) JSON Syntax. Available at: https://www.w3schools.com/js/js_json_syntax.asp (Accessed: 10 October 2026).
    public void storeMessage()
    {
        //Builds the JSON text for this one message.
        String entry = "  {\n"
                + "    \"MessageID\": \"" + escapeJson(messageID) + "\",\n"
                + "    \"MessageNumber\": " + messageNumber + ",\n"
                + "    \"Recipient\": \"" + escapeJson(recipient) + "\",\n"
                + "    \"Message\": \"" + escapeJson(messageText) + "\",\n"
                + "    \"MessageHash\": \"" + escapeJson(messageHash) + "\"\n"
                + "  }";

        //If there's already a stored message I need a comma between the objects.
        if (storedCount > 0)
        {
            storedEntries = storedEntries + ",\n";
        }
        storedEntries = storedEntries + entry;
        storedCount++;

        //Writes the whole array to messages.json in the project folder.
        //false means it overwrites the file, so each program run starts fresh.
        try
        {
            FileWriter writer = new FileWriter("messages.json", false);
            writer.write("[\n" + storedEntries + "\n]\n");
            writer.close();
        }
        catch (IOException e)
        {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }

    //These are just getters so other classes can read the values.
    public String getMessageID()
    {
        return messageID;
    }

    public int getMessageNumber()
    {
        return messageNumber;
    }

    public String getRecipient()
    {
        return recipient;
    }

    public String getMessageText()
    {
        return messageText;
    }

    public String getMessageHash()
    {
        return messageHash;
    }

    //This shows the message details in the right order: ID, hash, recipient, message.
    public String displayMessageDetails()
    {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText;
    }
}
