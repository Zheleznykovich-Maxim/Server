import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 8080;
    static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        System.out.println("Server is running");
        while (true) {
            Socket clientSocket = listener.accept();
            System.out.println("Client connected");
            ClientHandler client = new ClientHandler(clientSocket);
            clients.add(client);
            new Thread(client).start();
        }
    }
}