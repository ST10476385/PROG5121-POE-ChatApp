/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp.part1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author ST10476385 KN SHIKWAMBANA
 */

//This is my Message class, it handles everything to do with the messages the user
//sends. It makes the message ID, works out the hash, checks the recipient number,
//checks the message length and stores the messages in a JSON file. For Part 3, I added
//arrays to hold all the messages so we can search, delete, and display reports.
public class Message {

    // These are the details for each message, I made them private so they can only be changed through my methods.
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    private String flag; // PART 3: This remembers if the message was Sent, Stored, or Disregarded.

    // These are static because they need to be shared across every Message object.
    //totalMessagesSent is how many messages were actually sent.
    private static int totalMessagesSent = 0;

    //I'm keeping all the SENT messages in this string so printMessages() can show them
    //at the end. Stored and disregarded messages are not added here, only sent ones.
    private static String allMessages = "";

    // PART 3: These are my parallel arrays to store the data.
    // I used ArrayList instead of standard arrays (like String[]) because
    // the user decides how many messages to send, so we don't know the exact size beforehand.
    public static ArrayList<String> sentMessagesArray = new ArrayList<>();
    public static ArrayList<String> disregardedMessagesArray = new ArrayList<>();
    public static ArrayList<String> storedMessagesArray = new ArrayList<>();
    public static ArrayList<String> messageHashesArray = new ArrayList<>();
    public static ArrayList<String> messageIDsArray = new ArrayList<>();

    // I also need to store the actual text and recipients so I can search for them later.
    public static ArrayList<String> allRecipients = new ArrayList<>();
    public static ArrayList<String> allMessagesText = new ArrayList<>();

    // PART 3: These two arrays keep track of the status and number of each message.
    // They use the same index as the other arrays so the correct details stay together.
    public static ArrayList<String> messageStatusesArray = new ArrayList<>();
    public static ArrayList<Integer> messageNumbersArray = new ArrayList<>();

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

    // LECTURER FEEDBACK FIX: I changed this so it generates 15 digits and then uses
    // the substring method to grab just the first 10, as requested by the lecturer.
    private static String generateMessageID()
    {
        Random random = new Random();
        String randomDigits = "";
        for (int i = 0; i < 15; i++)
        {
            randomDigits = randomDigits + random.nextInt(10);
        }
        return randomDigits.substring(0, 10);
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
        //  I added the trim check so a blank message (just pressing Enter) is rejected too,
        // before it would have passed and made an empty hash.
        if (text == null || text.trim().equals(""))
        {
            return "Message cannot be empty.";
        }

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

        //I strip out punctuation so the hash only has letters. Otherwise
        //things like "tonight?" would end up in the hash with the question mark.
        //  I also skip any "word" that is only punctuation, like the "!" at the end of
        // "It is dinner time !". Before, the last word came out empty and the hash lost "TIME".
        String firstWord = "";
        String lastWord = "";
        for (String w : words)
        {
            String clean = w.replaceAll("[^a-zA-Z]", "");
            if (!clean.equals(""))
            {
                if (firstWord.equals(""))
                {
                    firstWord = clean;
                }
                lastWord = clean;
            }
        }

        //Put it all together and make it uppercase.
        String hash = firstTwo + ":" + numberPart + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    // PART 3: This method puts the message details into the correct arrays depending on
    // what the user chose to do (Send, Store, or Disregard).
    private void addToArrays()
    {
        messageIDsArray.add(messageID);
        messageHashesArray.add(messageHash);
        allRecipients.add(recipient);
        allMessagesText.add(messageText);
        messageStatusesArray.add(flag);
        messageNumbersArray.add(messageNumber);

        // I check the flag to know which specific array needs this message.
        if (flag.equals("Sent"))
        {
            sentMessagesArray.add(messageText);
        }
        else if (flag.equals("Disregard"))
        {
            disregardedMessagesArray.add(messageText);
        }
        else if (flag.equals("Stored"))
        {
            storedMessagesArray.add(messageText);
        }
    }

    //This lets the user pick if they want to send, store, or disregard the message.
    //1 = send, 2 = disregard, 3 = store for later.
    public String sentMessage(int choice)
    {
        if (choice == 1)
        {
            //Only add to the total and the sent list when the user actually sends it.
            totalMessagesSent++;
            this.flag = "Sent";
            addToArrays();
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
            this.flag = "Disregard";
            addToArrays();
            return "Press 0 to delete the message.";
        }

        else if (choice == 3)
        {
            this.flag = "Stored";
            addToArrays();
            storeMessage();
            return "Message successfully stored.";
        }

        else
        {
            return "Invalid option.";
        }
    }

    // PART 3: Rebuilds the separate arrays after a message has been deleted.
    // This makes sure the arrays still contain the correct messages.
    private static void rebuildStatusArrays()
    {
        sentMessagesArray.clear();
        disregardedMessagesArray.clear();
        storedMessagesArray.clear();

        for (int i = 0; i < allMessagesText.size(); i++)
        {
            if (messageStatusesArray.get(i).equals("Sent"))
            {
                sentMessagesArray.add(allMessagesText.get(i));
            }
            else if (messageStatusesArray.get(i).equals("Disregard"))
            {
                disregardedMessagesArray.add(allMessagesText.get(i));
            }
            else if (messageStatusesArray.get(i).equals("Stored"))
            {
                storedMessagesArray.add(allMessagesText.get(i));
            }
        }
    }

    // PART 3: Rebuilds the output containing all sent messages.
    // This is used after deleting a message so the output stays updated.
    private static void rebuildSentMessagesOutput()
    {
        allMessages = "";

        for (int i = 0; i < allMessagesText.size(); i++)
        {
            if (messageStatusesArray.get(i).equals("Sent"))
            {
                allMessages = allMessages
                        + "Message ID: " + messageIDsArray.get(i) + "\n"
                        + "Message Hash: " + messageHashesArray.get(i) + "\n"
                        + "Recipient: " + allRecipients.get(i) + "\n"
                        + "Message: " + allMessagesText.get(i) + "\n"
                        + "---\n";
            }
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
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);

            switch (c)
            {
                case '\\':
                    result.append("\\\\");
                    break;

                case '"':
                    result.append("\\\"");
                    break;

                case '\n':
                    result.append("\\n");
                    break;

                case '\r':
                    result.append("\\r");
                    break;

                case '\t':
                    result.append("\\t");
                    break;

                case '\b':
                    result.append("\\b");
                    break;

                case '\f':
                    result.append("\\f");
                    break;

                default:
                    result.append(c);
                    break;
            }
        }

        return result.toString();
    }

    // PART 3: This writes all stored messages to the JSON file.
    // It rewrites the file so that multiple stored messages are kept together.
    // @citation: Oracle. (2026) Java FileWriter Documentation. Available at: https://docs.oracle.com/javase/8/docs/api/java/io/FileWriter.html (Accessed: 10 October 2026).
    // @citation: W3Schools. (2026) JSON Syntax. Available at: https://www.w3schools.com/js/js_json_syntax.asp (Accessed: 10 October 2026).
    public void storeMessage()
    {
        writeStoredMessagesToJson();
    }

    // PART 3: This writes every stored message into one JSON array.
    private static void writeStoredMessagesToJson()
    {
        StringBuilder json = new StringBuilder();

        json.append("[\n");

        boolean first = true;

        for (int i = 0; i < allMessagesText.size(); i++)
        {
            if (messageStatusesArray.get(i).equals("Stored"))
            {
                if (!first)
                {
                    json.append(",\n");
                }

                json.append("  {\n")
                        .append("    \"MessageID\": \"")
                        .append(escapeJson(messageIDsArray.get(i))).append("\",\n")
                        .append("    \"MessageNumber\": ")
                        .append(messageNumbersArray.get(i)).append(",\n")
                        .append("    \"Recipient\": \"")
                        .append(escapeJson(allRecipients.get(i))).append("\",\n")
                        .append("    \"Message\": \"")
                        .append(escapeJson(allMessagesText.get(i))).append("\",\n")
                        .append("    \"MessageHash\": \"")
                        .append(escapeJson(messageHashesArray.get(i))).append("\"\n")
                        .append("  }");

                first = false;
            }
        }

        json.append("\n]\n");

        try
        {
            FileWriter writer = new FileWriter("messages.json", false);
            writer.write(json.toString());
            writer.close();
        }
        catch (IOException e)
        {
            System.out.println("Error saving messages: " + e.getMessage());
        }
    }

    // PART 3: This extracts a string value from one line of the JSON file.
    // It also handles quotation marks and escaped characters.
    private static String extractJsonString(String line)
    {
        int colon = line.indexOf(":");

        if (colon == -1)
        {
            return "";
        }

        int start = colon + 1;

        while (start < line.length() && Character.isWhitespace(line.charAt(start)))
        {
            start++;
        }

        if (start >= line.length() || line.charAt(start) != '"')
        {
            return "";
        }

        start++;

        StringBuilder result = new StringBuilder();
        boolean escaped = false;

        for (int i = start; i < line.length(); i++)
        {
            char c = line.charAt(i);

            if (c == '"' && !escaped)
            {
                break;
            }

            result.append(c);

            if (c == '\\' && !escaped)
            {
                escaped = true;
            }
            else
            {
                escaped = false;
            }
        }

        return unescapeJson(result.toString());
    }

    // PART 3: Restores special characters when reading messages from JSON.
    private static String unescapeJson(String text)
    {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);

            if (c == '\\' && i + 1 < text.length())
            {
                char next = text.charAt(++i);

                switch (next)
                {
                    case 'n':
                        result.append('\n');
                        break;

                    case 'r':
                        result.append('\r');
                        break;

                    case 't':
                        result.append('\t');
                        break;

                    case 'b':
                        result.append('\b');
                        break;

                    case 'f':
                        result.append('\f');
                        break;

                    case '"':
                        result.append('"');
                        break;

                    case '\\':
                        result.append('\\');
                        break;

                    default:
                        result.append(next);
                        break;
                }
            }
            else
            {
                result.append(c);
            }
        }

        return result.toString();
    }

    // PART 3: This reads the JSON file back into the arrays when the program starts.
    // @citation: GeeksforGeeks. (2026) Java Read File. Available at: https://www.geeksforgeeks.org/different-ways-reading-text-file-java/ (Accessed: 20 October 2026).
    public static void readMessagesFromJson()
    {
        clearArrays();

        File file = new File("messages.json");

        if (!file.exists())
        {
            return;
        }

        try (Scanner scanner = new Scanner(file))
        {
            String currentID = "";
            String currentRecipient = "";
            String currentMessage = "";
            String currentHash = "";
            int currentNumber = 0;

            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine().trim();

                if (line.equals("{"))
                {
                    currentID = "";
                    currentRecipient = "";
                    currentMessage = "";
                    currentHash = "";
                    currentNumber = 0;
                }
                else if (line.startsWith("\"MessageID\":"))
                {
                    currentID = extractJsonString(line);
                }
                else if (line.startsWith("\"MessageNumber\":"))
                {
                    String number = line.substring(line.indexOf(":") + 1)
                            .replace(",", "").trim();

                    currentNumber = Integer.parseInt(number);
                }
                else if (line.startsWith("\"Recipient\":"))
                {
                    currentRecipient = extractJsonString(line);
                }
                else if (line.startsWith("\"Message\":"))
                {
                    currentMessage = extractJsonString(line);
                }
                else if (line.startsWith("\"MessageHash\":"))
                {
                    currentHash = extractJsonString(line);
                }
                // Every object in the file except the last one ends with "}," (with a comma),
                // so line.equals("}") missed them and only the LAST stored message was loaded.
                // startsWith("}") catches both "}" and "},".
                else if (line.startsWith("}"))
                {
                    if (!currentID.isEmpty())
                    {
                        messageIDsArray.add(currentID);
                        messageNumbersArray.add(currentNumber);
                        allRecipients.add(currentRecipient);
                        allMessagesText.add(currentMessage);
                        messageHashesArray.add(currentHash);
                        messageStatusesArray.add("Stored");
                    }
                }
            }

            // Rebuild the stored messages array after reading all the JSON data.
            rebuildStatusArrays();
        }
        catch (Exception e)
        {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
    }

    // PART 3: To find the longest message, I loop through the stored array and keep
    // replacing the 'longest' variable if I find a message with more characters.
    public static String displayLongestMessage()
    {
        if (storedMessagesArray.isEmpty())
        {
            return "No stored messages available.";
        }

        String longest = "";

        for (String msg : storedMessagesArray)
        {
            if (msg.length() > longest.length())
            {
                longest = msg;
            }
        }

        return longest;
    }

    // PART 3: To search by ID, I loop through the ID array. If I find a match,
    // I use that same index to get the recipient and message from the other arrays.
    public static String searchByMessageID(String id)
    {
        for (int i = 0; i < messageIDsArray.size(); i++)
        {
            if (messageIDsArray.get(i).equals(id))
            {
                return "Message ID: " + messageIDsArray.get(i)
                        + "\nRecipient: " + allRecipients.get(i)
                        + "\nMessage: " + allMessagesText.get(i);
            }
        }

        return "Message ID not found.";
    }

    // PART 3: Same idea here. I check every recipient, and if it matches,
    // I add their message to the result string.
    // The brief says "sent or stored", so I skip the disregarded ones using the status array.
    public static String searchByRecipient(String recipient)
    {
        String result = "";

        for (int i = 0; i < allRecipients.size(); i++)
        {
            if (allRecipients.get(i).equals(recipient)
                    && !messageStatusesArray.get(i).equals("Disregard"))
            {
                result = result + "Message: " + allMessagesText.get(i) + "\n";
            }
        }

        if (result.equals(""))
        {
            return "No messages found for this recipient.";
        }

        return result;
    }

    // PART 3: To delete by hash, I find the hash and save the message text.
    // I then remove that item from ALL the parallel arrays using the same index (i).
    public static String deleteByHash(String hash)
    {
        for (int i = 0; i < messageHashesArray.size(); i++)
        {
            if (messageHashesArray.get(i).equals(hash)
                    && messageStatusesArray.get(i).equals("Stored"))
            {
                String deletedMsg = allMessagesText.get(i);

                // Remove the message from all the parallel arrays using the same index.
                messageIDsArray.remove(i);
                messageHashesArray.remove(i);
                allRecipients.remove(i);
                allMessagesText.remove(i);
                messageStatusesArray.remove(i);
                messageNumbersArray.remove(i);

                // Rebuild the status arrays and sent messages output after deletion.
                rebuildStatusArrays();
                rebuildSentMessagesOutput();

                // Update the JSON file so the deleted message is not loaded again.
                writeStoredMessagesToJson();

                return "Message: \"" + deletedMsg + "\" successfully deleted.";
            }
        }

        return "Hash not found.";
    }

    // PART 3: The report shows all the sent messages with their ID, hash and recipient.
    //I added a section for the stored messages too. Delete only works on stored messages,
    // so the user needs to be able to see the hash of a stored message in the report to delete it.
    public static String displayReport()
    {
        String report = "--- MESSAGE REPORT ---\n";
        boolean found = false;

        report = report + "Sent messages:\n";
        for (int i = 0; i < allMessagesText.size(); i++)
        {
            if (messageStatusesArray.get(i).equals("Sent"))
            {
                report = report
                        + "Message ID: " + messageIDsArray.get(i) + "\n"
                        + "Message Hash: " + messageHashesArray.get(i) + "\n"
                        + "Recipient: " + allRecipients.get(i) + "\n"
                        + "Message: " + allMessagesText.get(i) + "\n"
                        + "------------------------------\n";

                found = true;
            }
        }

        report = report + "Stored messages:\n";
        for (int i = 0; i < allMessagesText.size(); i++)
        {
            if (messageStatusesArray.get(i).equals("Stored"))
            {
                report = report
                        + "Message ID: " + messageIDsArray.get(i) + "\n"
                        + "Message Hash: " + messageHashesArray.get(i) + "\n"
                        + "Recipient: " + allRecipients.get(i) + "\n"
                        + "Message: " + allMessagesText.get(i) + "\n"
                        + "------------------------------\n";

                found = true;
            }
        }

        if (!found)
        {
            return "No messages available.";
        }

        return report;
    }

    // Helper to clear arrays for testing so the data is fresh for every test.
    public static void clearArrays()
    {
        sentMessagesArray.clear();
        disregardedMessagesArray.clear();
        storedMessagesArray.clear();

        messageHashesArray.clear();
        messageIDsArray.clear();

        allRecipients.clear();
        allMessagesText.clear();

        messageStatusesArray.clear();
        messageNumbersArray.clear();

        totalMessagesSent = 0;
        allMessages = "";
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