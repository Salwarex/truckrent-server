package utmn.truckrent.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import ru.vit4liy.jwt.JwtManager;
import ru.vit4liy.jwt.JwtSigner;
import utmn.truckrent.server.entity.account.AccountController;
import utmn.truckrent.server.entity.container.ContainerController;
import utmn.truckrent.server.entity.delivery.DeliveryController;
import utmn.truckrent.server.entity.driver.DriverController;
import utmn.truckrent.server.entity.finance.FinanceController;
import utmn.truckrent.server.entity.partner.PartnerController;
import utmn.truckrent.server.entity.trademark.TradeMarkController;
import utmn.truckrent.server.entity.truck.TruckController;
import utmn.truckrent.server.entity.truckmark.TruckMarkController;
import utmn.truckrent.server.utils.Config;
import utmn.truckrent.server.utils.TokenMaster;

import java.nio.file.Path;

public class Application {
    private static final int PORT = Config.getInt("port");
    private final static Javalin app;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JwtManager jwtManager = new JwtManager(new JwtSigner());
    private static final TokenMaster tokenMaster = new TokenMaster(64);

    static {
        Path basePath = Path.of(".");
        app = Javalin.create().start(PORT);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static void main(String[] args) {
        System.out.println("Truckrent server started with port %d".formatted(PORT));

        new AccountController(app);
        new ContainerController(app);
        new DeliveryController(app);
        new DriverController(app);
        new FinanceController(app);
        new PartnerController(app);
        new TradeMarkController(app);
        new TruckController(app);
        new TruckMarkController(app);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static Javalin getApp() {
        return app;
    }

    public static JwtManager getJwtManager(){ return jwtManager; }
    public static TokenMaster getTokenMaster64(){ return tokenMaster; }
}