package com.code.hetelview.config;

import java.util.Properties;
import java.io.InputStream;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            Properties dbProps = new Properties();
            try (InputStream input = HibernateUtil.class.getClassLoader()
                    .getResourceAsStream("database.properties")) {
                dbProps.load(input);
            }

            Configuration cfg = new Configuration();
            cfg.setProperty("hibernate.connection.driver_class", dbProps.getProperty("db.driver"));
            cfg.setProperty("hibernate.connection.url", dbProps.getProperty("db.url"));
            cfg.setProperty("hibernate.connection.username", dbProps.getProperty("db.username"));
            cfg.setProperty("hibernate.connection.password", dbProps.getProperty("db.password"));

            // Add other hibernate settings (dialect, show_sql, etc.)
            cfg.configure(); // This loads hibernate.cfg.xml (if used)

            sessionFactory = cfg.buildSessionFactory();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
