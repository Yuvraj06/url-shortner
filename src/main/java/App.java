import static spark.Spark.*;
import com.shortener.service.ShortenerService;

public class App {
    public static void main(String[] args) {
        ShortenerService service = new ShortenerService();

        // Serve static files (HTML/CSS) from src/main/resources/public
        staticFiles.location("/public");

        // Route to handle shortening the URL
        post("/shorten", (req, res) -> {
            String longUrl = req.queryParams("url");
            String shortCode = service.shortenURL(longUrl);
            return "Your short URL is: http://localhost:4567/" + shortCode;
        });

        // Route to handle REDIRECTION
        get("/:code", (req, res) -> {
        String code = req.params(":code");
        String originalUrl = service.getOriginalURL(code);

    if (originalUrl != null) {
        // Ensure the URL starts with http/https so the browser doesn't think it's a local path
        if (!originalUrl.startsWith("http")) {
            originalUrl = "https://" + originalUrl;
        }
        res.redirect(originalUrl);
        return null;
    } else {
        res.status(404);
        return "Error 404: Short URL not found!";
    }
});
    }
}