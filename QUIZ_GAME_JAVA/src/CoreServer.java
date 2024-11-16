import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.*;

public class CoreServer extends Thread {
    private Socket clientSocket;
    private BufferedReader in;  // Input stream to read from the client
    private PrintWriter out;    // Output stream to write to the client
    private List<Quiz> quizList; // List to hold quizzes
    private int score;           // Variable to store the player's score

    // Constructor to initialize the client socket
    public CoreServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            // Initialize input and output streams for communication
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Load quiz data from CSV file
            quizList = loadQuizFromCSV("quiz.csv");

            // Loop through each quiz in the list
            for (Quiz quiz : quizList) {
                out.println("Q");  // Indicating that a question is being asked
                out.println("문제 : " + quiz.getQuestion());  // Send the question to the client

                // Read the client's answer
                String clientAnswer = in.readLine();
                if (clientAnswer == null) {
                    sendError("E01"); // Send error if the answer is invalid
                    break;
                }

                // Check if the client wants to quit the quiz
                if (clientAnswer.equalsIgnoreCase("q")) {  // Indicating that the quiz has ended
                    out.println("퀴즈가 종료되었습니다.");
                    break;
                }

                // Check if the answer is correct
                if (clientAnswer.equalsIgnoreCase(quiz.getAnswer())) {
                    score++;  // Increment score if the answer is correct
                    //out.println("R");
                    out.println("정답입니다!");  // Send "Y" for correct answer
                } else {
                    //out.println("R");
                    out.println("오답입니다.");  // Send "N" for incorrect answer
                }
            }

            // Send the final score after the quiz ends
            out.println("R");
            out.println("퀴즈가 끝났습니다!");
            out.println("R");
            out.println("최종 점수: " + score);
        } catch (IOException e) {
            sendError("E02"); // Send error code for general I/O error
        } finally {
            try {
                clientSocket.close();  // Close the client socket
            } catch (IOException e) {
                e.printStackTrace();  // Print stack trace if closing socket fails
            }
        }
    }

    // Method to load quiz questions from a CSV file
    private List<Quiz> loadQuizFromCSV(String fileName) {
        List<Quiz> quizList = new ArrayList<>();
        try {
            // Read all lines from the CSV file
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            for (String line : lines) {
                // Split each line by comma and create a Quiz object
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    quizList.add(new Quiz(parts[0], parts[1])); // Add question and answer to list
                }
            }
        } catch (IOException e) {
            sendError("E03"); // Send error code if there is an issue with reading the file
        }
        return quizList;
    }

    // Method to send error messages to the client
    private void sendError(String errorCode) {
        out.println("E");
        out.println(errorCode); // Send the error code to the client
    }

    // Nested class to represent a quiz with a question and answer
    private static class Quiz {
        private String question;
        private String answer;

        public Quiz(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }
    }
}
