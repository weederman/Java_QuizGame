import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String CONFIG_FILE = "server_info.dat";  // Path to the file containing server info
    private static final String DEFAULT_SERVER_ADDRESS = "localhost";  // Default server address
    private static final int DEFAULT_SERVER_PORT = 9304;             // Default server port number
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);  // Scanner for reading user input
        
        String serverAddress = DEFAULT_SERVER_ADDRESS;  // Initialize server address
        int serverPort = DEFAULT_SERVER_PORT;            // Initialize server port

        // Check if the server info file exists
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists() && configFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
                // Read server address and port number from the file
                serverAddress = reader.readLine().trim();  // First line is the server address
                String portLine = reader.readLine().trim(); // Second line is the port number
                if (!portLine.isEmpty()) {
                    serverPort = Integer.parseInt(portLine);  // Convert the port number to integer
                }
                System.out.println("Loaded server info from file.");
            } catch (IOException e) {
                System.err.println("Error reading server_info.dat. Using default values.");
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number in server_info.dat. Using default values.");
            }
        } else {
            System.out.println("server_info.dat not found. Using default server info.");
        }

        // Attempt to connect to the server
        try {
            Socket socket = new Socket(serverAddress, serverPort);  // Connect to the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // Input stream to read data from the server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);  // Output stream to send data to the server

            System.out.println("Connected to the server. The quiz will start.");

            while (true) {
                String serverMessage = in.readLine();  // Read message from the server
                if (serverMessage == null) {
                    Protocol.handleError("E01"); // Call error handler if server disconnects
                    break;  // Exit if the server closes the connection
                }

                Protocol.handleProtocol(serverMessage, in, out);  // Handle server's message based on protocol
            }

            socket.close();  // Close the socket connection
            System.out.println("Connection with the server has been closed.");
        } catch (IOException e) {
            Protocol.handleError("E02");  // Handle communication error
        } finally {
            scanner.close();  // Close the scanner resource
        }
    }
}
