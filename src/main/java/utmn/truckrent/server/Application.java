package utmn.truckrent.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import utmn.truckrent.server.utils.Config;

import java.nio.file.Path;

public class Application {
    private static final int PORT = Config.getInt("port");
    private final static Javalin app;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        Path basePath = Path.of(".");
        app = Javalin.create().start(PORT);
    }

    public static void main(String[] args) {
        System.out.println("Truckrent server started with port %d".formatted(PORT));

    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static Javalin getApp() {
        return app;
    }
}