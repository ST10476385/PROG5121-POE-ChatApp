/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.chatapp.part1;

import java.util.Scanner;

/**
 *
 * @author ST10476385 KN SHIKWAMBANA
 */
public class ChatAppPart1 {

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        Login login = new Login();

        //Starting off with registration, asking for all the details one at a time so it's easier to follow what's happening.
        System.out.println("=== Registration ===");

        System.out.print("Enter a username ( must contain _ and be 5 characters or less): ");
        String username = input.nextLine();

        System.out.print("Enter a password (8+ characters, a capital letter, a number and a special character): ");
        String password = input.nextLine();

        System.out.print("Enter your cell number (with country code, example +27614880723): ");
        String cellNumber = input.nextLine();

        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        //RegisterUser() does all the checking for me and just gives back message
        //saying what's wrong or that it worked, so I just print whatever it returns.
        String registerResult = login.registerUser(username, password, cellNumber, firstName, lastName);
        System.out.println(registerResult);

        //It only moves on to login if registration actually succeeded, checking the start of the message
        //since that's the easiest way to tell if it went through without adding another return type.
        if (registerResult.startsWith("Username successfully captured"))
        {
            System.out.println();
            System.out.println("=== Login ===");

            System.out.print("Enter your username: ");
            String loginUsername = input.nextLine();

            System.out.print("Enter your password: ");
            String loginPassword = input.nextLine();

            login.loginUser(loginUsername, loginPassword);
            System.out.println(login.returnLoginStatus());

            //Only move on to the QuickChat menu if login was actually successful.
            //I added this check so random people can't just skip the login part.
            if (login.isLoggedIn())
            {
                System.out.println();
                System.out.println("Welcome to QuickChat.");

                //The user says how many messages they want to enter ONCE, right when the app starts.
                //I keep asking until they give me a number more than 0.
                int numMessages = 0;
                while (numMessages <= 0)
                {
                    System.out.print("How many messages do you wish to enter? ");
                    String numInput = input.nextLine();
                    try
                    {
                        numMessages = Integer.parseInt(numInput);
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("Please enter a valid number.");
                        continue;
                    }
                    if (numMessages <= 0)
                    {
                        System.out.println("Number must be more than 0.");
                    }
                }

                //This remembers if the user already entered all their messages,
                //so they can't pick option 1 again and send more than the number they set.
                boolean messagesEntered = false;

                //This boolean keeps the menu running until the user picks quit.
                boolean running = true;
                while (running)
                {
                    System.out.println();
                    System.out.println("--- Menu ---");
                    System.out.println("Option 1) Send Messages");
                    System.out.println("Option 2) Show recently sent messages");
                    System.out.println("Option 3) Quit");
                    System.out.print("Choose an option: ");

                    //I'm reading it as a String first and then converting it, because if
                    //someone types letters instead of a number, nextInt() would crash.
                    String menuInput = input.nextLine();
                    int menuChoice;

                    try
                    {
                        menuChoice = Integer.parseInt(menuInput);
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("Please enter a number (1, 2 or 3).");
                        continue;
                    }

                    if (menuChoice == 1)
                    {
                        if (messagesEntered)
                        {
                            System.out.println("You have already entered all " + numMessages + " messages you set at the start.");
                        }
                        else
                        {
                            //This is where the send messages feature lives.
                            sendMessages(input, numMessages);
                            messagesEntered = true;
                        }
                    }
                    else if (menuChoice == 2)
                    {
                        System.out.println("Coming Soon.");
                    }
                    else if (menuChoice == 3)
                    {
                        System.out.println("Goodbye!");
                        running = false;
                    }
                    else
                    {
                        System.out.println("Invalid option. Please choose 1, 2 or 3.");
                    }
                }
            }
        }

        input.close();
    }

    //This method runs the whole send messages flow. I made it separate so main
    //doesn't get too long and it's easier to read. numMessages is what the user
    //picked at the start, so this loop only runs that many times.
    private static void sendMessages(Scanner input, int numMessages)
    {
        //This is the for loop that runs once for each message they wanted to send.
        //i is also used as the message number, so the first message is 0.
        for (int i = 0; i < numMessages; i++)
        {
            System.out.println();
            System.out.println("--- Message " + (i + 1) + " of " + numMessages + " ---");

            // -------- Get the recipient --------
            //I keep asking until the number passes the check, so nobody gets past with a bad number.
            String recipient = "";
            boolean recipientOk = false;
            while (!recipientOk)
            {
                System.out.print("Enter recipient cell number (with international code, example +27718693002): ");
                recipient = input.nextLine();

                //I make a temp Message object just to reuse the checkRecipientCell() method.
                Message tempMessage = new Message(i, recipient, "test");
                String checkResult = tempMessage.checkRecipientCell();
                System.out.println(checkResult);

                if (checkResult.equals("Cell phone number successfully captured."))
                {
                    recipientOk = true;
                }
            }

            // -------- Get the message text ----------------------------
            //Same idea, keep asking until the length is fine. checkMessageLength()
            //gives back the error with how many characters over they are.
            String messageText = "";
            boolean messageOk = false;
            while (!messageOk)
            {
                System.out.print("Enter your message (max 250 characters): ");
                messageText = input.nextLine();

                String lengthResult = Message.checkMessageLength(messageText);
                System.out.println(lengthResult);

                if (lengthResult.equals("Message ready to send."))
                {
                    messageOk = true;
                }
            }

            // -------- Make the actual Message object --------
            Message msg = new Message(i, recipient, messageText);
            System.out.println(msg.displayMessageID());

            //Show all the details in the order the assignment asked for:
            //Message ID, Message Hash, Recipient, Message.
            System.out.println();
            System.out.println("--- Message Details ---");
            System.out.println(msg.displayMessageDetails());

            // -------- Ask what to do with it --------
            System.out.println();
            System.out.println("What would you like to do?");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print("Choose an option: ");

            String sendInput = input.nextLine();
            int sendChoice;

            try
            {
                sendChoice = Integer.parseInt(sendInput);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid input. Message disregarded.");
                continue;
            }

            String sendResult = msg.sentMessage(sendChoice);
            System.out.println(sendResult);
        }

        //After all the messages are done, show how many were actually sent.
        System.out.println();
        System.out.println("Total messages sent: " + Message.returnTotalMessages());

        //And show all the messages that were sent during this run.
        System.out.println();
        System.out.println("--- All Messages Sent ---");
        System.out.println(Message.printMessages());
    }
}
