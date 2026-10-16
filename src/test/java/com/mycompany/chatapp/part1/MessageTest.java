/*
Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
*/
package com.mycompany.chatapp.part1;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for Part 2 - Sending Messages.
 *
 * @author ST10476385 KN SHIKWAMBANA
 */
public class MessageTest {

    // == assertEquals TESTS ==

    // Test: Message should not be more than 250 characters -> should show "Message ready to send."
    @Test
    public void testMessageLengthSuccess() {
        String message = "Hi Mike, can you join us for dinner tonight?";
        //This message is under 250 so it should be fine.
        assertTrue(message.length() <= 250);
        assertEquals("Message ready to send.", "Message ready to send.");
    }

    // Test: Message more than 250 characters -> should return the error with the number of characters over
    @Test
    public void testMessageLengthFailure() {
        //I'm building a message that's exactly 260 characters long.
        String longMessage = "";
        for (int i = 0; i < 260; i++) {
            longMessage = longMessage + "a";
        }
        int excess = longMessage.length() - 250;
        //10 characters over 250 so the message should say "by 10".
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                "Message exceeds 250 characters by " + excess + "; please reduce the size.");
    }

    // Test: Recipient number correctly formatted (+27718693002) -> should return success message
    @Test
    public void testRecipientCorrect() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    // Test: Recipient number incorrectly formatted (08575975889) -> should return error message
    @Test
    public void testRecipientIncorrect() {
        Message msg = new Message("08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                msg.checkRecipientCell());
    }

    // Test: Message hash is correct for Test Case 1
    // The hash should end with HITONIGHT (first and last words of the message).
    @Test
    public void testMessageHashCorrect() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        //The hash should end with the first and last words in caps.
        assertTrue(msg.getMessageHash().endsWith("HITONIGHT"));
    }

    // Test: Message ID is created and shown correctly -> "Message ID generated: <Message ID>"
    @Test
    public void testMessageIDCreated() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue(msg.checkMessageID());
        assertTrue(msg.getMessageID().length() <= 10);
        System.out.println("Message ID generated: " + msg.getMessageID());
    }

    // Test: Message hash end part is correct for Test Case 2
    // The last word of "Hi Keegan, did you receive the payment?" is "payment",
    //so the hash should end with HIPAYMENT.
    @Test
    public void testMessageHashCorrectCase2() {
        Message msg = new Message("+27718693002", "Hi Keegan, did you receive the payment?");
        assertTrue(msg.getMessageHash().endsWith("HIPAYMENT"));
    }

    // == assertTrue or assertFalse TESTS ==

    // MessageSent: User selected "Send Message" (option 1) -> "Message successfully sent."
    @Test
    public void testSentMessageSend() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", msg.sentMessage(1));
    }

    // MessageSent: User selected "Disregard Message" (option 2) -> "Press 0 to delete the message."
    @Test
    public void testSentMessageDisregard() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Press 0 to delete the message.", msg.sentMessage(2));
    }

    // MessageSent: User selected "Store Message" (option 3) -> "Message successfully stored."
    @Test
    public void testSentMessageStore() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully stored.", msg.sentMessage(3));
    }

    // Message ID should never be more than 10 characters
    @Test
    public void testMessageIDLength() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue(msg.getMessageID().length() <= 10);
    }

    // Total messages sent should increase when we send a message
    @Test
    public void testReturnTotalMessages() {
        //I make a message and send it. totalMessagesSent should go up by 1.
        int before = Message.returnTotalMessages();
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        msg.sentMessage(1);
        assertEquals(before + 1, Message.returnTotalMessages());
    }
}