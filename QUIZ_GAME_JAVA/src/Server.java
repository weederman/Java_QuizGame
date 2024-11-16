import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.IOException;

public class Server {
    private ServerSocket serverSocket; // ServerSocket to listen for client connections
    private Socket clientSocket;      // Socket for handling individual client connections
    private final static int port = 9304; // Server port number
    private ExecutorService executorService; // Executor service for managing threads
    
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        // Open the server system.
        server.startServer(port);
        // Close the server system.
        server.closeServer();
    }

    public void startServer(int port) throws IOException {
        // Create a new server socket to accept connections
        serverSocket = new ServerSocket(port);
        // Initialize the thread pool with a fixed number of threads
        executorService = Executors.newFixedThreadPool(2); //20 threads in the pool
        
        while (true) {
            /* Open the port and wait indefinitely for clients to connect. */
            System.out.println("SERVER RUNNING... [Port = " + port + "]");
            clientSocket = serverSocket.accept(); 
            
            // A new client has connected to the server. Start handling the connection!
            executorService.submit(new CoreServer(clientSocket)); // Use the thread pool to handle the client
            System.out.println("CONNECT START...");    
        }
    }
     
    // Close the server and release resources.
    public void closeServer() throws IOException {
        clientSocket.close(); // Close the client socket
        serverSocket.close(); // Close the server socket
        executorService.shutdown(); // Shutdown the thread pool
    }
}
