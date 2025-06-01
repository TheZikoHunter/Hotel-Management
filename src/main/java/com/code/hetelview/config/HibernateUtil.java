package com.code.hetelview.config;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.code.hetelview.model.Employee;
import com.code.hetelview.model.Reservation;
import java.util.Properties;
import java.io.InputStream;

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

            // Add required Hibernate properties
            cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            cfg.setProperty("hibernate.hbm2ddl.auto", "update");
            cfg.setProperty("hibernate.show_sql", "true");
            cfg.setProperty("hibernate.format_sql", "true");

            // Register your entity classes
            cfg.addAnnotatedClass(Employee.class);
            cfg.addAnnotatedClass(Reservation.class);
            // Add other entities as needed

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