/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
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

    // -----------assertEquals TESTS--------------------------------------

    // It tests if Message should not be more than 250 characters : should show "Message ready to send."
    @Test
    public void testMessageLengthSuccess() {
        String message = "Hi Mike, can you join us for dinner tonight?";
        //This message is under 250 so it should be fine. Now this tests the real method.
        assertEquals("Message ready to send.", Message.checkMessageLength(message));
    }

    // It tests if Message more than 250 characters : should return the error with the number of characters over
    @Test
    public void testMessageLengthFailure() {
        //I'm building a message that's exactly 260 characters long.
        String longMessage = "";
        for (int i = 0; i < 260; i++) {
            longMessage = longMessage + "a";
        }
        //10 characters over 250 so the message should say "by 10".
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                Message.checkMessageLength(longMessage));
    }

    // It tests if Recipient number correctly formatted (+27718693002) : should return success message
    @Test
    public void testRecipientCorrect() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    // It tests if Recipient number incorrectly formatted (08575975889) : should return error message
    @Test
    public void testRecipientIncorrect() {
        Message msg = new Message(1, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                msg.checkRecipientCell());
    }

    // It tests if Message hash is correct for Test Case 1 : should be exactly 00:0:HITONIGHT
    // I pass in a fixed ID (0012345678) because the real one is random, so the first two digits are 00.
    @Test
    public void testMessageHashCorrect() {
        Message msg = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", msg.createMessageHash());
    }

    // It tests the rest of the message hashes in a loop (the assignment said to do it this way)
    @Test
    public void testMessageHashesInLoop() {
        String[] ids = {"0012345678", "5512345678", "9987654321"};
        String[] texts = {
            "Hi Mike, can you join us for dinner tonight?",
            "Hi Keegan, did you receive the payment?",
            "Okay"
        };
        String[] expected = {"00:0:HITONIGHT", "55:1:HIPAYMENT", "99:2:OKAYOKAY"};

        //The loop counter i is also the message number, just like in the real app.
        for (int i = 0; i < texts.length; i++) {
            Message msg = new Message(ids[i], i, "+27718693002", texts[i]);
            assertEquals(expected[i], msg.createMessageHash());
        }
    }

    // It tests if Message ID is created and shown correctly : "Message ID generated: <Message ID>"
    @Test
    public void testMessageIDCreated() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue(msg.checkMessageID());
        assertEquals("Message ID generated: " + msg.getMessageID(), msg.displayMessageID());
        System.out.println(msg.displayMessageID());
    }

    // It tests if MessageSent: User selected "Send Message" (option 1) : "Message successfully sent."
    @Test
    public void testSentMessageSend() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", msg.sentMessage(1));
    }

    // It tests if MessageSent: User selected "Disregard Message" (option 2) : "Press 0 to delete the message."
    @Test
    public void testSentMessageDisregard() {
        Message msg = new Message(1, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Press 0 to delete the message.", msg.sentMessage(2));
    }

    // It tests if MessageSent: User selected "Store Message" (option 3) : "Message successfully stored."
    @Test
    public void testSentMessageStore() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully stored.", msg.sentMessage(3));
    }

    // ============= assertTrue or assertFalse TESTS =================================================

    // It tests if Message ID is always exactly 10 digits and never more than 10 characters
    @Test
    public void testMessageIDLength() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue(msg.getMessageID().length() <= 10);
        assertEquals(10, msg.getMessageID().length());
    }

    // It tests if Total messages sent should increase when we send a message
    @Test
    public void testReturnTotalMessages() {
        //I make a message and send it. totalMessagesSent should go up by 1.
        int before = Message.returnTotalMessages();
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        msg.sentMessage(1);
        assertEquals(before + 1, Message.returnTotalMessages());
    }

    // It tests the assignment's test data: message 1 is sent and message 2 is disregarded,
    // so the total should only go up by 1 (a disregarded message must not be counted).
    @Test
    public void testTotalAfterSendAndDiscard() {
        int before = Message.returnTotalMessages();
        Message msg1 = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        Message msg2 = new Message(1, "08575975889", "Hi Keegan, did you receive the payment?");
        msg1.sentMessage(1);
        msg2.sentMessage(2);
        assertEquals(before + 1, Message.returnTotalMessages());
    }
}
