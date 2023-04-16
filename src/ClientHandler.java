import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final PrintWriter out;
    private String name;

    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while (true) {
                String inputLine = in.readLine();
                if (inputLine == null) {
                    break;
                }
                if (name == null) {
                    name = inputLine;
                    System.out.println("New user connected: " + name);
                } else {
                    System.out.println("Message from " + name + ": " + inputLine);
                    Server.clients.stream()
                            .filter(c -> c != this)
                            .forEach(c -> c.sendMessage(name + ": " + inputLine));
                }
            }
        } catch (IOException e) {
            System.out.println("Exception occurred: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Exception occurred while closing client socket: " + e.getMessage());
            }
            if (name != null) {
                System.out.println(name + " disconnected from server.");
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}