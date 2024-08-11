import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class yanhaoClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 1234;

        // "try-with-resources" syntax block automatically closes resources
        try(
                // create a socket for the specified host and port
                Socket socket = new Socket(host, port);
                // get the io streams of the socket
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // get the system standard input stream
                BufferedReader sysReader = new BufferedReader(new InputStreamReader(System.in));)
        {
            new Thread(() -> {
                try {
                    String receivedMessage;
                    while (true) {
                        // return immediately rather than waiting with "readLine()" to avoid blocking
                        if (reader.ready()) {
                            receivedMessage = reader.readLine();
                            System.out.println("Server: " + receivedMessage);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            String messageToServer;
            while (true) {
                if (sysReader.ready()) {
                    messageToServer = sysReader.readLine();
                    writer.println(messageToServer);
                    // exit the system with "exit"
                    if ("exit".equalsIgnoreCase(messageToServer)){
                        System.out.println("You have exited the chat");
                        System.exit(0);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
