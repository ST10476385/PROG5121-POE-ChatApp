# PROG5121 Chat App (Part 1, Part 2 and Part 3) – QuickChat

[![TestJava](https://github.com/ST10476385/PROG5121-POE-ChatApp/actions/workflows/TestJava.yml/badge.svg)](https://github.com/ST10476385/PROG5121-POE-ChatApp/actions/workflows/TestJava.yml)

**Student Name:** Khanyisa Ntsko Shikwambana

---
**Student Number:** ST10476385  
**Module:** PROG5121  
**Part 1 Submission Date:** 18 September 2026  
**Part 2 Submission Date:** 19 October 2026  
**Part 3 Submission Date:** 19 November 2026

**YouTube Presentation (Part 1 – Registration and Login):** https://youtu.be/FY2LNRlOIKI  
**YouTube Presentation (Part 2 – Sending Messages):** https://youtu.be/2U3F6mGnfsQ  
**YouTube Presentation (Part 3 – Store Data and Display Task Report):** https://youtu.be/oSexzGYxrGU?si=4QN7ibutuSfhYOwE

---

##  Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Setup Instructions](#setup-instructions)
- [How to Run the Application](#how-to-run-the-application)
- [How to Run Unit Tests](#how-to-run-unit-tests)
- [Automated Testing with GitHub Actions](#automated-testing-with-github-actions)
- [Application Flow](#application-flow)
- [Validation Rules](#validation-rules)
- [Storing Messages as JSON](#storing-messages-as-json)
- [Part 3 – Stored Messages Menu](#part-3--stored-messages-menu)
- [Citations and References](#references)
  
---

##  Project Overview

This is the Chat App Proof of Engagement (PoE) for the module PROG5121. It is a **console-based chat application (QuickChat)** built in three parts.

**Part 1 – Registration and Login**

1. **Create an account** by providing a username, password, and South African cell phone number.
2. **Log in** using the same credentials.

**Part 2 – Sending Messages**

3. After a successful login, the user sees a **welcome message and a numeric menu**.
4. The user says **how many messages** they want to enter, then enters the recipient and message text for each one.
5. Each message gets a **unique 10-digit Message ID**, a **Message Hash**, and a message number.
6. For each message the user can **send it, disregard it, or store it** to send later (stored messages are saved to a **JSON file**).
7. At the end, the application shows the **total number of messages sent** and the messages that were sent.

**Part 3 – Store Data and Display Task Report**

8. Every message is also saved into **parallel arrays** (sent, disregarded, stored, message hashes, message IDs, plus the recipients, message text, status and message number). Nothing is hard-coded.
9. When the user logs in, the **JSON file is read back into the arrays**, so stored messages from a previous run are loaded.
10. A fourth main menu option, **Stored Messages**, lets the user display the sender and recipient of stored messages, find the longest stored message, search by message ID, search by recipient, delete a message using its hash, and display a full report.

The application validates all inputs using the rules specified in the assignment brief and provides clear feedback to the user. Every part is tested with JUnit 5 and the tests run automatically on GitHub.

---

##  Features

| Feature | Description |
|---------|-------------|
| **Username Validation** | Must contain an underscore (`_`) and be no more than 5 characters long. |
| **Password Complexity** | Must be at least 8 characters, contain a capital letter, a number, and a special character. |
| **Cell Phone Validation** | Must start with the South African country code (`+27`) and be followed by 1–10 digits. |
| **Registration** | Stores user details (first name, last name, username, password, cell number). |
| **Login** | Verifies credentials and returns a welcome message or error message. |
| **Welcome Message and Menu** | Shows "Welcome to QuickChat." and a numeric menu: 1) Send Messages, 2) Show recently sent messages ("Coming Soon."), 3) Quit, 4) Stored Messages (Part 3). The menu runs in a `while` loop until the user quits. |
| **Number of Messages** | The user sets how many messages they will enter once, at the start. A `for` loop then runs exactly that many times. |
| **Message Number** | Comes from the `for` loop counter, so the first message is number 0. |
| **Message ID** | Randomly generated 10-digit number (15 random digits, then `substring` keeps the first 10). |
| **Message Hash** | First two digits of the Message ID, a colon, the message number, a colon, then the first and last words of the message in capitals (for example `00:0:HITONIGHT`). Punctuation is removed, and a "word" that is only punctuation (like a lone `!`) is skipped. |
| **Message Length Check** | A message may not be longer than 250 characters. If it is too long, the app says how many characters over the limit it is. An empty message is rejected. |
| **Recipient Check** | Reuses the cell phone check from the `Login` class (international code followed by the number). |
| **Send, Disregard or Store** | The user chooses what to do with each message. Only sent messages count towards the total. An invalid choice is recorded as disregarded. |
| **JSON Storage** | Stored messages are written to `messages.json` as a valid JSON array. |
| **Parallel Arrays (Part 3)** | `ArrayList`s hold the sent, disregarded and stored messages, the hashes and the IDs. The recipients, message text, status and message number are kept at the same index so every message's details stay together. |
| **Read JSON into Arrays (Part 3)** | At login, `messages.json` is read back into the arrays and the app shows how many stored messages were loaded and lists them. |
| **Stored Messages Menu (Part 3)** | Main menu option 4 opens a menu: display sender and recipient, longest message, search by message ID, search by recipient, delete by hash, and a full report. |
| **Unit Testing** | JUnit 5 tests for the `Login` class (14 tests), the `Message` class (13 tests) and the Part 3 message manager features (8 tests). |
| **Automated Testing** | A GitHub Actions workflow (`TestJava.yml`) runs all the tests on every push and pull request. |

---

##  Technologies Used

| Technology | Purpose |
|------------|---------|
| **Java** (JDK 25) | Core programming language |
| **Maven** | Build automation and dependency management |
| **JUnit 5** | Unit testing framework |
| **Git** | Version control |
| **GitHub** | Remote repository hosting |
| **GitHub Actions** | Continuous integration (automated tests) |
| **NetBeans IDE** | Development environment |

---

##  Project Structure
The project now also contains the Part 3 files:

```
PROG5121-POE-ChatApp
├── .github/workflows/TestJava.yml      (GitHub Actions workflow)
├── src
│   ├── main/java/com/mycompany/chatapp/part1
│   │   ├── ChatAppPart1.java           (main program: registration, login, menus, sending messages, stored messages menu)
│   │   ├── Login.java                  (Part 1: registration and login checks)
│   │   └── Message.java                (Part 2 and 3: message ID, hash, checks, send/store, JSON, arrays, search, delete, report)
│   └── test/java/com/mycompany/chatapp/part1
│       ├── LoginTest.java              (14 unit tests)
│       ├── MessageTest.java            (13 unit tests)
│       └── MessageManagerTest.java     (8 unit tests for Part 3)
├── pom.xml
└── README.md
```

`messages.json` is created in the project folder while the app runs. It is not part of the source code.

---

##  Setup Instructions

### Prerequisites
- **Java JDK 25** or later
- **Maven** (optional – if using the command line)
- **NetBeans IDE** (recommended)
- **Git** (for version control)
- **GitHub account** (for submission)

### Clone the Repository

```bash
git clone https://github.com/ST10476385/PROG5121-POE-ChatApp.git
cd PROG5121-POE-ChatApp
```

### Open in NetBeans

1. Open NetBeans IDE.
2. Go to **File → Open Project**.
3. Navigate to the project folder and select it.
4. Click **Open Project**.

---

##  How to Run the Application

### Option 1: Run from NetBeans

1. In the **Projects** tab, expand `ChatApp-Part1`.
2. Expand **Source Packages** → `com.mycompany.chatapp.part1`.
3. Right-click `ChatAppPart1.java` → **Run File** (or press `Shift + F6`).

### Option 2: Run from Command Line (with Maven)

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.mycompany.chatapp.part1.ChatAppPart1"
```

### Expected Output – Part 1 (Registration and Login)

The application will guide you through registration and login:

```
=== Registration ===
Enter a username (must contain _ and be 5 characters or less): kyl_1
Enter a password (8+ characters, a capital letter, a number and a special character): Ch&&sec@ke99!
Enter your cell number (with country code, example +27614880723): +27838968976
Enter your first name: John
Enter your last name: Doe
Username successfully captured. Password successfully captured. Cell phone number successfully added.

=== Login ===
Enter your username: kyl_1
Enter your password: Ch&&sec@ke99!
Welcome John, Doe it is great to see you again.


```
<img width="1917" height="1017" alt="image" src="https://github.com/user-attachments/assets/86c8e824-e74c-40b2-a8cf-3e7d91e411b0" />

### Expected Output – Part 2 (Sending Messages)

After a successful login the QuickChat menu starts. The Message IDs are random, so yours will differ. The message details are only shown after the user chooses to send the message:

```
Loaded 0 stored message(s) from messages.json.

Welcome to QuickChat.
How many messages do you wish to enter? 3

--- Menu ---
Option 1) Send Messages
Option 2) Show recently sent messages
Option 3) Quit
Option 4) Stored Messages
Choose an option: 1

--- Message 1 of 3 ---
Enter recipient cell number (with international code, example +27718693002): +27718693002
Cell phone number successfully captured.
Enter your message (max 250 characters): Hi Mike, can you join us for dinner tonight?
Message ready to send.
Message ID generated: 7363025905

What would you like to do?
1) Send Message
2) Disregard Message
3) Store Message to send later
Choose an option: 1
Message successfully sent.

--- Message Details ---
Message ID: 7363025905
Message Hash: 73:0:HITONIGHT
Recipient: +27718693002
Message: Hi Mike, can you join us for dinner tonight?
```

**PART 2 SCREENSHOTS :**

<img width="1335" height="1078" alt="image" src="https://github.com/user-attachments/assets/9991fd03-edad-4d17-9ae8-2037a9f63d24" />

<img width="1332" height="1078" alt="image" src="https://github.com/user-attachments/assets/3d2f553b-f4ee-443b-952a-3c31911633d6" />

<img width="1357" height="1073" alt="image" src="https://github.com/user-attachments/assets/f45d0ebe-32e3-45b1-ab28-9ea3764a844a" />

<img width="1410" height="1078" alt="image" src="https://github.com/user-attachments/assets/77ce9910-0b31-47cc-a9df-5635970ff0cb" />

<img width="1346" height="1072" alt="image" src="https://github.com/user-attachments/assets/132060c4-4692-4a0a-acd1-6bafede906c9" />

### Expected Output – Part 3 (Stored Messages)

Main menu option 4 opens the Stored Messages menu. The IDs and hashes are random, so yours will differ:

```
--- Menu ---
Option 1) Send Messages
Option 2) Show recently sent messages
Option 3) Quit
Option 4) Stored Messages
Choose an option: 4

--- Stored Messages Menu ---
a) Display sender and recipient of all stored messages
b) Display the longest stored message
c) Search for a message ID
d) Search messages for a particular recipient
e) Delete a message using the message hash
f) Display full report of all stored messages
g) Go back to main menu
Choose an option: a
Sender: Khanyisa Shikwambana
Recipient: +27838884567

Choose an option: b
Longest Message: Where are you? You are late! I have asked you to be on time.

Choose an option: d
Enter Recipient Number: +27838884567
Message: Where are you? You are late! I have asked you to be on time.
Message: Ok, I am leaving without you.

Choose an option: e
Enter Message Hash: 38:3:WHERETIME
Message: "Where are you? You are late! I have asked you to be on time." successfully deleted.
```

**PART 3 SCREENSHOTS :**
<img width="1266" height="967" alt="image" src="https://github.com/user-attachments/assets/9b3163b4-4b00-4f13-8dbb-944af729cded" />
<img width="1290" height="950" alt="image" src="https://github.com/user-attachments/assets/8e535f07-30ba-479d-a1f8-20c71c24aab8" />
<img width="1282" height="956" alt="image" src="https://github.com/user-attachments/assets/e6233322-afeb-41ed-8ff7-df10ea14dc0c" />
<img width="1290" height="852" alt="image" src="https://github.com/user-attachments/assets/8beea815-b155-4c2e-bfa9-173088b59660" />
<img width="1237" height="906" alt="image" src="https://github.com/user-attachments/assets/b9672235-3b3a-41cc-a497-75a338d42364" />
<img width="1296" height="955" alt="image" src="https://github.com/user-attachments/assets/36c158cd-5cb6-4c7e-b589-d50a3ff15b9a" />
<img width="1300" height="978" alt="image" src="https://github.com/user-attachments/assets/190f8ab5-d9a4-488a-82be-87d01235af92" />

---

##  How to Run Unit Tests

### Option 1: Run from NetBeans

1. In the **Projects** tab, expand **Test Packages** → `com.mycompany.chatapp.part1`.
2. Right-click `LoginTest.java`, `MessageTest.java` or `MessageManagerTest.java` → **Test File** (or press `Ctrl + F6`), or right-click the project → **Test** to run everything.

### Option 2: Run from Command Line (with Maven)

```bash
mvn test
```

### Expected Test Output

All tests should pass with a green bar:

```
Running com.mycompany.chatapp.part1.LoginTest
Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
Running com.mycompany.chatapp.part1.MessageManagerTest
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
Running com.mycompany.chatapp.part1.MessageTest
Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
Tests run: 35, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS


```
<img width="1490" height="1012" alt="Screenshot 2026-11-19 192718" src="https://github.com/user-attachments/assets/aadfca4f-3dea-4978-bc3a-3bf4e9dcc761" />
<img width="1912" height="1016" alt="image" src="https://github.com/user-attachments/assets/9d7db0ea-91e7-480a-b51a-452249db3e8c" />
<img width="1912" height="1015" alt="image" src="https://github.com/user-attachments/assets/22eaa9d5-46cf-4807-aa55-3aa0d86f93e3" />


The tests that store messages create a `messages.json` file in the project folder. `MessageManagerTest` deletes it after each test, so delete any leftover `messages.json` before running the application to keep test data out of it.

### What the Message tests cover (Part 2)

| Test | What it checks |
|------|----------------|
| Message length (success and failure) | "Message ready to send." and "Message exceeds 250 characters by X; please reduce the size." |
| Recipient number (success and failure) | `+27718693002` is accepted and `08575975889` is rejected |
| Message hash | Exactly `00:0:HITONIGHT` for the first test message, plus a loop that tests the other hashes |
| Message ID | The ID is created, is 10 digits long, and is shown as "Message ID generated: ..." |
| Message sent (options 1, 2 and 3) | "Message successfully sent.", "Press 0 to delete the message." and "Message successfully stored." |
| Total messages | The total goes up by 1 when a message is sent, and a disregarded message is not counted |

**PART 2 TEST SCREENSHOT :**

<img width="1912" height="1015" alt="image" src="https://github.com/user-attachments/assets/eb042aee-be59-4461-92d8-85033a683069" />

### What the Message Manager tests cover (Part 3)

The tests use the five test messages from the brief: message 1 is sent, message 2 is stored (the longest), message 3 is disregarded, message 4 is sent and message 5 is stored.

| Test | What it checks |
|------|----------------|
| 1. Sent messages array | Holds exactly 2 messages, "Did you get the cake?" and "It is dinner time !", in order |
| 2. Display the longest message | Returns "Where are you? You are late! I have asked you to be on time." |
| 3. Search for a message ID | Message 4's ID (`0838884567`) returns its recipient and "It is dinner time !". An ID that does not exist gives "Message ID not found." |
| 4. Search for a recipient | `+27838884567` returns both of that recipient's stored messages. The disregarded message's recipient and a fake number give "No messages found for this recipient." |
| 5. Delete by hash | Deleting message 2 gives the exact "successfully deleted" message, removes it from the arrays and updates `messages.json`. A fake hash gives "Hash not found." |
| 6. Display report | The report contains the hash, recipient and message of the sent messages, and never the disregarded message |
| 7. Message 4 hash | `08:3:ITTIME`, created by the real hash method (the `!` does not break the last word) |
| 8. JSON round trip | Both stored messages are written to `messages.json` and read back into the arrays |

**PART 3 TEST SCREENSHOT :**

<img width="1487" height="1026" alt="Screenshot 2026-11-19 192756" src="https://github.com/user-attachments/assets/c132ac8e-1a23-4535-b812-0f27c73e39c6" />

---

##  Automated Testing with GitHub Actions

The workflow in `.github/workflows/TestJava.yml` runs automatically whenever code is pushed or a pull request is opened for `main` or `master`. It:

1. Checks out the code.
2. Sets up JDK 25.
3. Runs all the unit tests with Maven (`mvn -B test`).
4. Uploads the test results.

The badge at the top of this README shows whether the latest run passed.

**GITHUB ACTIONS SCREENSHOT :**
<img width="1763" height="2502" alt="image" src="https://github.com/user-attachments/assets/45af2871-30ca-4d59-8836-f656bc64d0df" />

<img width="1759" height="3120" alt="image" src="https://github.com/user-attachments/assets/6bb049e7-0367-4e20-ae3c-a6e9b4ee52d0" />


---

##  Application Flow

### Registration Flow

The registration process validates each input in order:
1. Username is checked first.
2. Password is checked second.
3. Cell phone number is checked third.
4. If all pass, user details are stored and a success message is returned.
5. If any fail, the appropriate error message is returned.

### Login Flow

1. User enters username and password.
2. System compares credentials with stored values.
3. If correct, a welcome message with the user's name is displayed.
4. If incorrect, an error message is displayed.

### QuickChat Flow (Part 2)

1. The menu only appears after a successful login, with the message "Welcome to QuickChat."
2. The user enters how many messages they want to send (once, at the start).
3. Option 1 runs a `for` loop for exactly that many messages. For each one the app asks for the recipient, checks it, asks for the message, checks the length, and creates the Message ID and Hash.
4. The user then chooses to send, disregard, or store the message. The full message details (ID, hash, recipient, message) are only shown after the message has been sent.
5. When all the messages are done, the app shows the total number of messages sent and the messages that were sent.
6. Option 2 shows "Coming Soon." and Option 3 quits the application.

### Stored Messages Flow (Part 3)

1. Before the menu starts, the app reads `messages.json` into the arrays, shows how many stored messages were loaded and lists them.
2. Every message is added to the parallel arrays when the user sends, disregards or stores it.
3. Option 4 opens the Stored Messages menu, which loops until the user chooses `g` to go back.
4. Searching, finding the longest message, deleting and the report all use the arrays, with the same index linking a message's ID, hash, recipient, text, status and number.
5. Deleting a stored message removes it from every array and rewrites `messages.json`.

---


##  Validation Rules

### Username Rules

| Condition | Valid Example | Invalid Example |
|-----------|---------------|-----------------|
| Contains underscore (`_`) | `kyl_1` | `kyle` |
| Maximum 5 characters | `kyl_1` | `kyle!!!!!!!` |

### Password Rules

| Condition | Valid Example | Invalid Example |
|-----------|---------------|-----------------|
| At least 8 characters | `Ch&&sec@ke99!` | `pass` |
| Contains a capital letter | `Ch&&sec@ke99!` | `password` |
| Contains a number | `Ch&&sec@ke99!` | `Password!` |
| Contains a special character | `Ch&&sec@ke99!` | `Password123` |

### Cell Phone Rules

| Condition | Valid Example | Invalid Example |
|-----------|---------------|-----------------|
| Starts with `+27` | `+27838968976` | `08966553` |
| Followed by 1–10 digits | `+27838968976` | `+271234567890123` |

### Message Rules (Part 2)

| Condition | Valid Example | Invalid Example |
|-----------|---------------|-----------------|
| Recipient has the international code and no more than 10 digits after it | `+27718693002` | `08575975889` |
| Message is not empty | `Hi Mike, can you join us for dinner tonight?` | (just pressing Enter) |
| Message is no longer than 250 characters | `Hi Mike, can you join us for dinner tonight?` | A message of 260 characters (rejected: exceeds by 10) |
| Message ID is 10 digits | `7363025905` | – (always generated, never typed) |
| Message Hash format | `73:0:HITONIGHT` (first two ID digits, message number, first and last word in capitals) | – (always generated) |

### Stored Messages Rules (Part 3)

| Condition | Behaviour |
|-----------|-----------|
| Search by recipient | Returns sent and stored messages for that number. Disregarded messages are not included. |
| Delete by hash | Works on stored messages. Use the report (option f) to see the hash of each stored message. |
| Hash, ID or recipient not found | The app shows a "not found" message instead of crashing. |
| Invalid choice when sending | The message is recorded as disregarded so it is not lost. |

---

##  Storing Messages as JSON

When the user chooses **Store Message to send later**, the message is added to `messages.json` in the project folder. The file is a valid JSON array and the whole file is rewritten from the arrays each time, so it always matches the stored messages in memory. Only stored messages go into the file. Messages that are sent are shown on screen at the end, and disregarded messages are not kept.

When the user logs in, the file is read back into the arrays (Part 3), so stored messages from a previous run are loaded. When a stored message is deleted, the file is rewritten so the message does not come back.

Example of what the file looks like:

```json
[
  {
    "MessageID": "1770852802",
    "MessageNumber": 1,
    "Recipient": "+27575975889",
    "Message": "Hi Keegan, did you receive the payment?",
    "MessageHash": "17:1:HIPAYMENT"
  }
]
```
<img width="1352" height="1078" alt="image" src="https://github.com/user-attachments/assets/fa26a6e6-92ac-4b27-8b0b-2037d4848565" />

Quotes and backslashes inside a message are escaped so they cannot break the JSON, and they are unescaped again when the file is read.

---

##  Part 3 – Stored Messages Menu

| Option | What it does |
|--------|--------------|
| **a** | Displays the sender and the recipient of every stored message |
| **b** | Displays the longest stored message |
| **c** | Searches for a message ID and displays the recipient and message |
| **d** | Searches for all the sent and stored messages for a particular recipient |
| **e** | Deletes a stored message using its message hash |
| **f** | Displays a report with the ID, hash, recipient and message of the sent and stored messages |
| **g** | Goes back to the main menu |

---
### References
---
- Burd, B. (2022) Java for Dummies. 8th edn. John Wiley and Sons. ISBN: 978-1119861645.

- Cadenehead, R. (2020) Sams Teach Yourself Java in 21 Days. 8th edn. Sams Publishing. ISBN: 978-0672337956.

- Farrell, J. (2019) Java Programming. 9th edn. Course Technology, Cengage Learning.

- Farrell, J. (2024) Java Programming. 10th edn. Course Technology, Cengage Learning.

- GeeksforGeeks. (2026) Regular Expressions in Java. Available at: https://www.geeksforgeeks.org/regular-expressions-in-java/ (Accessed: 20 August 2026).

- GeeksforGeeks. (2026) Java Read File. Available at: https://www.geeksforgeeks.org/different-ways-reading-text-file-java/ (Accessed: 20 October 2026).

<!-- TODO: add the source you used for the password regex in Login.java (author, year, title, URL, access date) -->

- GitHub. (2026) GitHub Documentation. Available at: https://docs.github.com/ (Accessed: 20 August 2026).

- GitHub Actions Tutorial. (2026) CI/CD with GitHub Actions. Available at: https://www.youtube.com/watch?v=oz0Qd5H4Onk (Accessed: 20 August 2026).

- GitHub Desktop. (2026) GitHub Desktop Tutorial. Available at: https://www.youtube.com/watch?v=bUgFv1Y5LJw (Accessed: 20 August 2026).

- JUnit 5. (2026) JUnit 5 User Guide. Available at: https://junit.org/junit5/docs/current/user-guide/ (Accessed: 20 August 2026).

- JUnit Tutorial. (2026) JUnit in NetBeans. Available at: https://www.youtube.com/watch?v=MOhiM2SXZI0 (Accessed: 20 August 2026).

- NetBeans IDE. (2026) NetBeans Downloads. Available at: https://netbeans.org/downloads/ (Accessed: 20 August 2026).

- Oracle. (2026) Java FileWriter Documentation. Available at: https://docs.oracle.com/javase/8/docs/api/java/io/FileWriter.html (Accessed: 10 October 2026).

- PROG5121 Module Outline. (2026) Available at: https://advtechonline.sharepoint.com/:w:/r/sites/TertiaryStudents/_layouts/15/Doc.aspx?sourcedoc=%7B5B770B77-2908-402A-A5DD-30ED47D82E66%7D&file=PROG5121_MO.docx&action=default&mobileredirect=true (Accessed: 20 August 2026).

- QuickBlox. (2026) Beginner's Guide to Chat App Architecture. Available at: https://quickblox.com/blog/beginners-guide-to-chat-app-architecture/ (Accessed: 20 August 2026).

- Stack Overflow. (2026) Regular expression to match South African cell phone numbers. Available at: https://stackoverflow.com/questions/16699007/ (Accessed: 20 August 2026).

- W3Schools. (2026) JSON Syntax. Available at: https://www.w3schools.com/js/js_json_syntax.asp (Accessed: 10 October 2026).

---
### What the Presentations Cover

**Part 1 – Registration and Login**

-  Overview of the project and its purpose
-  Explanation of the `Login` class and all methods
-  Demonstration of the registration and login process
-  Explanation of validation logic (username, password, cell phone)
-  Running and explaining the unit tests
-  Code walkthrough and flow explanation

https://youtu.be/FY2LNRlOIKI

**Part 2 – Sending Messages**

-  Demonstration of the welcome message, menu and the number of messages being set once at the start
-  Explanation of the `for` loop and `while` loop, and how the message number comes from the loop counter
-  Explanation of the `Message` class: Message ID, Message Hash (string manipulation), length check and recipient check
-  Demonstration of sending, disregarding and storing messages, and the total at the end
-  Showing `messages.json` and explaining how it is written
-  Running and explaining the unit tests and the GitHub Actions run

https://youtu.be/2U3F6mGnfsQ

**Part 3 – Store Data and Display Task Report**

-  Explanation of the parallel arrays and how the same index keeps each message's details together
-  Demonstration of reading `messages.json` back into the arrays when the app starts
-  Demonstration of the Stored Messages menu: sender and recipient, longest message, search by ID, search by recipient, delete by hash and the report
-  Showing that deleting a stored message also updates `messages.json`
-  Running and explaining the unit tests and the GitHub Actions run

https://youtu.be/oSexzGYxrGU?si=4QN7ibutuSfhYOwE

---

##  Disclaimer

This project was developed as part of an academic assessment for PROG5121. All code is original work except where explicitly credited. External resources are cited appropriately in the code and in this README file.

---

##  Contact

If you have any questions about this project, please contact:

**Student Name:** Khanyisa Ntsko Shikwambana  
**Student Number:** ST10476385  
**Email:** ST10476385@rcconnect.edu.za

---


**© 2026 – PROG5121 Chat App (Part 1, Part 2 and Part 3)**  
*All rights reserved.*
