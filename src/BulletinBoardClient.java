import java.io.*;
import java.net.*;
import java.util.List;

public class BulletinBoardClient {
    private static String serverAddress = "localhost";
    private static int serverPort = 5000;

    public static void main(String[] args) {
        System.out.println("Connected to Bulletin Board Server at " + serverAddress + ":" + serverPort);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                System.out.println("Enter the operation (POST, READ, REPLY, or QUIT):");
                String operation = reader.readLine().toUpperCase();

                if ("QUIT".equals(operation)) {
                    break;
                }

                try (Socket socket = new Socket(serverAddress, serverPort);
                     ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                     ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                    out.writeObject(operation);
                    out.flush();

                    switch (operation) {
                        case "POST":
                            System.out.println("Enter the title of the article:");
                            String title = reader.readLine();
                            System.out.println("Enter the content of the article:");
                            String content = reader.readLine();

                            out.writeObject(title);
                            out.writeObject(content);
                            out.flush();

                            String postResult = (String) in.readObject();
                            System.out.println(postResult);
                            break;
                        case "READ":
                            List<Article> articles = (List<Article>) in.readObject();
                            for (Article article : articles) {
                                System.out.println(article);
                            }
                            break;
                        case "REPLY":
                            System.out.println("Enter the ID of the parent article:");
                            int parentId = Integer.parseInt(reader.readLine());
                            System.out.println("Enter the title of the reply:");
                            String replyTitle = reader.readLine();
                            System.out.println("Enter the content of the reply:");
                            String replyContent = reader.readLine();

                            out.writeObject(parentId);
                            out.writeObject(replyTitle);
                            out.writeObject(replyContent);
                            out.flush();

                            String replyResult = (String) in.readObject();
                            System.out.println(replyResult);
                            break;
                        default:
                            System.out.println("Invalid operation. Please try again.");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error communicating with server: " + e.getMessage());
                }
            } catch (IOException e) {
                System.out.println("Error reading input: " + e.getMessage());
            }
        }
    }
}
