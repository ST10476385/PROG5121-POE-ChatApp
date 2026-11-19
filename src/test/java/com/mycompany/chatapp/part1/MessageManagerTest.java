/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.chatapp.part1;

import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Part 3 - Message Manager (Arrays and Task Report).
 *
 * @author ST10476385 KN SHIKWAMBANA
 */
public class MessageManagerTest {

    // @BeforeEach means this method runs before every single test below.
    // I use it to wipe the arrays clean and set up the exact test data from the assignment brief,
    // so every test starts with the same fresh data and they don't interfere with each other.
    @BeforeEach
    public void setUp()
    {
        Message.clearArrays();

        // Tests Data Message 1 - Sent
        Message msg1 = new Message(0, "+27834557896", "Did you get the cake?");
        msg1.sentMessage(1);

        // Tests Data Message 2 - Stored (This is the longest message)
        Message msg2 = new Message(1, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        msg2.sentMessage(3);

        // Tests Data Message 3 - Disregard
        Message msg3 = new Message(2, "+27834484567", "Yohoooo, I am at your gate.");
        msg3.sentMessage(2);

        // Tests Data Message 4 - Sent
        // The brief gives Message 4 a specific ID ("0838884567"). Since my app generates random IDs,
        // I use my second constructor to pass in that fixed ID.
        Message msg4 = new Message("0838884567", 3, "Developer", "It is dinner time !");
        msg4.sentMessage(1);

        // Tests Data Message 5 - Stored
        Message msg5 = new Message(4, "+27838884567", "Ok, I am leaving without you.");
        msg5.sentMessage(3);
    }

    // Storing messages writes a real messages.json file in the project folder, so I delete it after every test.
    @AfterEach
    public void tearDown()
    {
        new File("messages.json").delete();
    }

    // Test 1: Sent Messages array correctly populated
    @Test
    public void testSentMessagesArrayPopulated()
    {
        // The brief says assertEquals, so I check the exact size and the exact messages in order
        // (before I only used contains).
        assertEquals(2, Message.sentMessagesArray.size());
        assertEquals("Did you get the cake?", Message.sentMessagesArray.get(0));
        assertEquals("It is dinner time !", Message.sentMessagesArray.get(1));
    }

    // Test 2: It display's the longest Message
    @Test
    public void testDisplayLongestMessage()
    {
        String expected = "Where are you? You are late! I have asked you to be on time.";
        assertEquals(expected, Message.displayLongestMessage());
    }

    // Test 3: It Searches for the messageID
    @Test
    public void testSearchForMessageID()
    {
        // It Checks success case
        String result = Message.searchByMessageID("0838884567");
        assertEquals("Message ID: 0838884567\nRecipient: Developer\nMessage: It is dinner time !", result);

        // It Checks failure case (not found)
        assertEquals("Message ID not found.", Message.searchByMessageID("0000000000"));
    }

    // Test 4: Search all the messages sent or stored regarding a particular recipient
    @Test
    public void testSearchAllMessagesForRecipient()
    {
        // It Checks success case (finds both stored messages for this recipient)
        String result = Message.searchByRecipient("+27838884567");
        assertEquals("Message: Where are you? You are late! I have asked you to be on time.\n"
                + "Message: Ok, I am leaving without you.\n", result);

        // It Checks failure case (disregarded message should not show up, and fake number returns not found)
        assertEquals("No messages found for this recipient.", Message.searchByRecipient("+27834484567"));
        assertEquals("No messages found for this recipient.", Message.searchByRecipient("+27000000000"));
    }

    // Test 5: It deletes a message using a message hash
    @Test
    public void testDeleteMessageByHash()
    {
        // It GrabS the hash of Message 2 and delete it
        String hashToDelete = Message.messageHashesArray.get(1);
        String result = Message.deleteByHash(hashToDelete);

        // It CheckS that the app confirms the deletion with the exact message from the brief and the message is gone
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.", result);
        assertFalse(Message.allMessagesText.contains("Where are you? You are late! I have asked you to be on time."));

        // It CheckS that deleting a fake hash returns the not found message
        assertEquals("Hash not found.", Message.deleteByHash("XX:9:NOPE"));

        // CheckS the JSON file was updated so it doesn't load again
        Message.clearArrays();
        Message.readMessagesFromJson();
        assertFalse(Message.messageHashesArray.contains(hashToDelete));
        assertEquals(1, Message.storedMessagesArray.size());
    }

    // Test 6: It Display's the Report
    @Test
    public void testDisplayReport()
    {
        String report = Message.displayReport();
        
        // It Check that the report has all the required details
        assertTrue(report.contains("Hash:") || report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient:"));
        assertTrue(report.contains("Message:"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time !"));

        // The disregarded message must never be in the report
        assertFalse(report.contains("Yohoooo"));
    }

    // Test 7: Checks that the real hash made for Message 4 is correct (the "!" does not break the last word)
    // I moved this out of the report test so every test checks one thing.
    @Test
    public void testMessage4Hash()
    {
        assertEquals("08:3:ITTIME", Message.messageHashesArray.get(3));
    }

    // Test 8: Checks the JSON round trip (both stored messages are saved to messages.json and read back into the arrays)
    // I moved this out of the report test too. It catches the bug where only the last message was loaded.
    @Test
    public void testJsonRoundTrip()
    {
        String hash = Message.messageHashesArray.get(1);
        Message.clearArrays();
        Message.readMessagesFromJson();

        assertEquals(2, Message.storedMessagesArray.size());
        assertTrue(Message.messageHashesArray.contains(hash));
        assertTrue(Message.storedMessagesArray.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(Message.storedMessagesArray.contains("Ok, I am leaving without you."));
    }
}