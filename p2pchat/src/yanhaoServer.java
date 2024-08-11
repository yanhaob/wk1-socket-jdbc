import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class yanhaoServer {
    public static void main(String[] args) {
        int port = 1234;
        try {
            // blind the listening port
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is running, listening on port " + port);

            while (true) {
                // wait and listen for the client's connection request
                Socket clientSocket = serverSocket.accept();
                // create a thread for the client to handle the request
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.writer = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // send a welcome message to the client
            writer.println("Welcome to our Chat! Type anything pressing enter to send message.(Type 'exit' to end the chat)");
            BufferedReader sysReader = new BufferedReader(new InputStreamReader(System.in));
            String message;
            while (true) {
                if (reader.ready()) {  // check if there is a message from the client
                    message = reader.readLine();
                    // listen to the client and close the client connection with "exit"
                    if ("exit".equalsIgnoreCase(message)) {
                        System.out.println("Client exit, System shutdown...");
                        // terminate the JVM immediately and release resources automatically
                        System.exit(0);
                    }
                    System.out.println("Client: " + message);
                }
                if (sysReader.ready()) {  // check if there is a custom message to send to the client
                    String messageToClient = sysReader.readLine();
                    writer.println(messageToClient);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close(); // close socket
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
