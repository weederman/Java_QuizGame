import java.io.*;
import java.util.Scanner;

public class Protocol {

    private static Scanner scanner = new Scanner(System.in); // Scanner to read user input

    // Method to handle different protocols based on the server message
    public static void handleProtocol(String serverMessage, BufferedReader in, PrintWriter out) throws IOException {
        switch (serverMessage) {
            case "R":
                // Handle the 'R' protocol where the server sends a response message
                String responseMessage = in.readLine(); // Read the response message from the server
                System.out.println("Server: " + responseMessage); // Print the server's response
                break;

            case "Q":
                // Handle the 'Q' protocol where the server asks a question
                String questionMessage = in.readLine(); // Read the question from the server
                System.out.println("Server: " + questionMessage); // Print the server's question
                System.out.print("Enter your answer (type 'q' to quit): "); // Prompt user for input
                String clientAnswer = scanner.nextLine().trim(); // Get the client's answer

                if (clientAnswer.equalsIgnoreCase("q")) { // Check if user wants to quit
                    out.println("q"); // Send quit message to server
                } else {
                    out.println(clientAnswer); // Send the client's answer to the server
                }

                String resultMessage = in.readLine(); // Read the result message from the server
                System.out.println("Server: " + resultMessage); // Print the result message from the server
                break;

            case "E":
                // Handle the 'E' protocol which indicates an error
                serverMessage = in.readLine(); // Read the error message from the server
                handleError(serverMessage); // Handle the error message
                break;
            default:
                // Handle unknown protocol codes
                System.out.println("Unknown protocol code: " + serverMessage);
                break;
        }
    }

    // Method to handle error messages based on the error code
    public static void handleError(String errorCode) {
        switch (errorCode) {
            case "E01":
                System.out.println("오류: 서버 연결이 종료되었습니다.");
                break;
            case "E02":
                System.out.println("서버 오류: 통신에 문제가 발생했습니다.");
                break;
            case "E03":
                System.out.println("파일 오류: 퀴즈 CSV 파일을 읽는 중 오류가 발생했습니다.");
                break;
            default:
                System.out.println("알 수 없는 오류");
        }
    }
}
