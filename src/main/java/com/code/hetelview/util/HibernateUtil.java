package com.code.hetelview.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.code.hetelview.model.Employee;
import com.code.hetelview.model.Reservation;
import java.util.Properties;
import java.io.InputStream;

/**
 * Utility class for managing Hibernate SessionFactory.
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            // Load database properties
            Properties dbProps = new Properties();
            try (InputStream input = HibernateUtil.class.getClassLoader()
                    .getResourceAsStream("database.properties")) {
                if (input == null) {
                    throw new RuntimeException("Unable to find database.properties");
                }
                dbProps.load(input);
            }

            // Create configuration programmatically
            Configuration cfg = new Configuration();
            
            // Set database connection properties
            cfg.setProperty("hibernate.connection.driver_class", dbProps.getProperty("db.driver"));
            cfg.setProperty("hibernate.connection.url", dbProps.getProperty("db.url"));
            cfg.setProperty("hibernate.connection.username", dbProps.getProperty("db.username"));
            cfg.setProperty("hibernate.connection.password", dbProps.getProperty("db.password"));
            
            // Set Hibernate properties
            cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            cfg.setProperty("hibernate.hbm2ddl.auto", "update");
            cfg.setProperty("hibernate.show_sql", "false");
            cfg.setProperty("hibernate.format_sql", "true");
            cfg.setProperty("hibernate.connection.pool_size", "10");
            cfg.setProperty("hibernate.current_session_context_class", "thread");
            
            // Add annotated classes
            cfg.addAnnotatedClass(Employee.class);
            cfg.addAnnotatedClass(Reservation.class);
            
            // Build session factory
            sessionFactory = cfg.buildSessionFactory();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Get the SessionFactory that can be used to create database sessions.
     * 
     * @return The SessionFactory instance
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Shutdown the SessionFactory.
     */
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}