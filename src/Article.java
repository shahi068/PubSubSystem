import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class Article implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String content;
    private List<Article> replies;

    public Article(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.replies = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<Article> getReplies() {
        return replies;
    }

    public void addReply(Article reply) {
        replies.add(reply);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append("\n");
        sb.append("Title: ").append(title).append("\n");
        sb.append("Content: ").append(content).append("\n");

        if (!replies.isEmpty()) {
            sb.append("Replies:\n");
            for (Article reply : replies) {
                sb.append(printIndented(reply, 2));
            }
        }

        return sb.toString();
    }

    private static String printIndented(Article article, int indentation) {
        String indent = String.join("", Collections.nCopies(indentation, " "));
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("- ID: ").append(article.id).append("\n");
        sb.append(indent).append("  Title: ").append(article.title).append("\n");
        sb.append(indent).append("  Content: ").append(article.content).append("\n");

        if (!article.replies.isEmpty()) {
            sb.append(indent).append("  Replies:\n");
            for (Article reply : article.replies) {
                sb.append(printIndented(reply, indentation + 4));
            }
        }

        return sb.toString();
    }


}
