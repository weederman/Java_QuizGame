# Java_QuizGame
Quiz Game Server &amp; Client with Java API

## 1. Requirements
    Client-Side Requirements
    - Connect to the server and start the quiz.
    - Receive questions from the server one at a time and provide answers.
    - Display feedback from the server (e.g., "Correct!" or "Incorrect") after each answer.
    - At the end of the quiz, receive the final score from the server.

    Server-Side Requirements
    - Store a set of questions and answers (can be stored in an array or list).
    - Send questions to the client and wait for responses.
    - Evaluate each response, update the client’s score, and send feedback.
    - After all questions are answered, send the final score to the client and close the connection.

    Client Configuration
    - Server connection details (IP and port) are stored in a configuration file (e.g., `server_info.dat`).
    - The client program reads these details from the file when it starts.
    - If the file is missing, default values (e.g., `localhost` and port `1234`) are used.

    Multi-Client Support
    - The server should handle multiple clients simultaneously.

## 2. Protocol
### `handleProtocol` Method
- **"R"**: If a response message is received from the server, it prints the message.
- **"Q"**: If the server sends a question, it prints the question and prompts the user to input an answer. If the user inputs "q", it sends a termination message to the server. Afterward, it prints the server's result message.
- **"E"**: If an error message is received from the server, the method calls the `handleError` method to process the error code and print the corresponding error message.
- **Other**: For unknown protocol codes, it prints the message, "Unknown protocol code."

---

### `handleError` Method
- **"E01"**: If the server connection is terminated, it prints "Server connection has been terminated."
- **"E02"**: If a communication issue occurs, it prints "Server error: Communication issue occurred."
- **"E03"**: If there is an error while reading the quiz CSV file, it prints "File error: An issue occurred while reading the quiz CSV file."
- **Other**: For unknown errors, it prints "Unknown error."

## 3. Report
![image](https://github.com/user-attachments/assets/8698c64e-cbd6-4895-838e-14e99a25b9d0)
