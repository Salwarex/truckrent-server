package utmn.truckrent.server.utils;

import org.hibernate.cfg.Configuration;
import utmn.truckrent.server.entity.TruckMark;

public class DatabaseConfigurator {
    public static Configuration buildHibernateConfig() {
        Configuration cfg = new Configuration();

        cfg.setProperty("hibernate.connection.driver_class", Config.getString("db.driver"));
        cfg.setProperty("hibernate.connection.url", Config.getString("db.url"));
        cfg.setProperty("hibernate.connection.username", Config.getString("db.username"));
        cfg.setProperty("hibernate.connection.password", Config.getString("db.password"));
        cfg.setProperty("hibernate.dialect", Config.getString("db.dialect"));

        cfg.setProperty("hibernate.hbm2ddl.auto", "update");
        cfg.setProperty("hibernate.show_sql", "true");
        cfg.setProperty("hibernate.format_sql", "true");
        cfg.setProperty("hibernate.connection.pool_size", Config.getString("db.pool_size"));
        cfg.setProperty("hibernate.current_session_context_class", "thread");

        cfg.addAnnotatedClass(TruckMark.class);

        return cfg;
    }
}
