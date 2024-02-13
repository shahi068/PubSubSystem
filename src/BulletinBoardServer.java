// BulletinBoardServer.java
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class BulletinBoardServer {
    private static List<Article> articles = new ArrayList<>();
    private static String coordinatorAddress = "localhost";
    private static int coordinatorPort = 6000;

    public static void main(String[] args) throws IOException {
        int port = 5000;

        // Register with the coordinator
        try (Socket coordinatorSocket = new Socket(coordinatorAddress, coordinatorPort);
             ObjectOutputStream coordOut = new ObjectOutputStream(coordinatorSocket.getOutputStream());
             ObjectInputStream coordIn = new ObjectInputStream(coordinatorSocket.getInputStream())) {
            coordOut.writeObject("REGISTER");
            coordOut.writeObject("localhost:" + port);
            coordOut.flush();
            String registerResponse = (String) coordIn.readObject();
            System.out.println("Received response from coordinator: " + registerResponse);
            System.out.println(registerResponse);
        } catch (ClassNotFoundException e) {
            System.out.println("Error registering with coordinator: " + e.getMessage());
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Bulletin Board Server is running on port " + port);

            while (true) {
                Socket clientSocket = null;
                ObjectInputStream in = null;
                ObjectOutputStream out = null;

                try {
                    clientSocket = serverSocket.accept();
                    in = new ObjectInputStream(clientSocket.getInputStream());
                    out = new ObjectOutputStream(clientSocket.getOutputStream());

                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                    String title;
                    String content;
                    int articleId;

                    String operation = (String) in.readObject();

                    switch (operation) {
                        case "POST":
                            if (coordinatorAddress.equals("localhost") && coordinatorPort == 6000) {
                                // Primary server
                                title = (String) in.readObject();
                                content = (String) in.readObject();
                                articleId = getNextArticleId();
                                articles.add(new Article(articleId, title, content));
                                out.writeObject("SUCCESS");
                                out.flush();


                                // Propagate updates to backup servers
                                for (String backupAddress : BulletinBoardServerCoordinator.getBackupServers()) {
                                    String[] backupParts = backupAddress.split(":");
                                    String backupHost = backupParts[0];
                                    int backupPort = Integer.parseInt(backupParts[1]);

                                    try (Socket backupSocket = new Socket(backupHost, backupPort);
                                         ObjectOutputStream backupOut = new ObjectOutputStream(backupSocket.getOutputStream())) {
                                        backupOut.writeObject("UPDATE");
                                        backupOut.writeObject(new Article(articleId, title, content));
                                        out.flush();
                                    } catch (IOException e) {
                                        System.out.println("Error updating backup server: " + e.getMessage());
                                    }
                                }
                            } else {
                                // Backup server
                                out.writeObject("ERROR: Cannot POST on backup server");
                            }
                            break;
                        case "UPDATE":
                            // Update backup server
                            Article updatedArticle = (Article) in.readObject();
                            articles.add(updatedArticle);
                            break;
                        case "READ":
                            out.writeObject(articles);
                            out.flush();
                            break;
                        case "REPLY":
                            int parentId = (int) in.readObject();
                            String replyTitle = (String) in.readObject();
                            String replyContent = (String) in.readObject();
                            int replyId = getNextArticleId();

                            Article parentArticle = findArticleById(articles, parentId);
                            if (parentArticle != null) {
                                parentArticle.addReply(new Article(replyId, replyTitle, replyContent));
                                out.writeObject("SUCCESS");
                                out.flush();
                            } else {
                                out.writeObject("ERROR: Parent article not found");
                            }
                            break;
                        default:
                            out.writeObject("ERROR: Invalid operation");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            System.out.println("Error closing ObjectOutputStream: " + e.getMessage());
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            System.out.println("Error closing ObjectInputStream: " + e.getMessage());
                        }
                    }
                    if (clientSocket != null) {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            System.out.println("Error closing client socket: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error setting up the server socket: " + e.getMessage());
        }
            }

    private static int getNextArticleId() throws IOException, ClassNotFoundException {
        try (Socket coordinatorSocket = new Socket(coordinatorAddress, coordinatorPort);
             ObjectOutputStream coordOut = new ObjectOutputStream(coordinatorSocket.getOutputStream());
             ObjectInputStream coordIn = new ObjectInputStream(coordinatorSocket.getInputStream())) {
            coordOut.writeObject("GET_ID");
            int articleId = (int) coordIn.readObject();
            return articleId;


        }
    }

    private static Article findArticleById(List<Article> articles, int id) {
        for (Article article : articles) {
            if (article.getId() == id) {
                return article;
            }

            Article replyArticle = findArticleById(article.getReplies(), id);
            if (replyArticle != null) {
                return replyArticle;
            }
        }

        return null;
    }
}