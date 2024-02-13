// BulletinBoardServerCoordinator.java
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class BulletinBoardServerCoordinator {
    private static int nextArticleId = 1;
    private static List<String> serverAddresses = new ArrayList<>();

    public static List<String> getBackupServers() {
        return serverAddresses.size() > 1 ? serverAddresses.subList(1, serverAddresses.size()) : new ArrayList<>();
    }

    public static String getPrimaryServer() {
        return serverAddresses.isEmpty() ? "" : serverAddresses.get(0);
    }
    public static void main(String[] args) throws IOException {
        int port = 6000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Bulletin Board Server Coordinator is running on port " + port);
            while (true) {
                try (Socket serverSocketConnection = serverSocket.accept();
                     ObjectInputStream in = new ObjectInputStream(serverSocketConnection.getInputStream());
                     ObjectOutputStream out = new ObjectOutputStream(serverSocketConnection.getOutputStream())) {

                    System.out.println("Server connected: " + serverSocketConnection.getInetAddress());
                    String operation = (String) in.readObject();

                    switch (operation) {
                        case "REGISTER":
                            String serverAddress = (String) in.readObject();
                            serverAddresses.add(serverAddress);
                            out.writeObject("SUCCESS");
                            break;
                        case "GET_ID":
                            int articleId = nextArticleId++;
                            out.writeObject(articleId);
                            break;
                        default:
                            out.writeObject("ERROR: Invalid operation");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error handling server: " + e.getMessage());
                }
            }
        }
    }
}