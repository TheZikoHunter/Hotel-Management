package com.code.hetelview.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.code.hetelview.model.Employee;
import com.code.hetelview.model.Reservation;
import java.util.Properties;
import java.io.InputStream;

public class HibernateTestUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            Properties dbProps = new Properties();
            try (InputStream input = HibernateTestUtil.class.getClassLoader()
                    .getResourceAsStream("test-database.properties")) {
                dbProps.load(input);
            }

            Configuration cfg = new Configuration();
            cfg.setProperty("hibernate.connection.driver_class", dbProps.getProperty("db.driver"));
            cfg.setProperty("hibernate.connection.url", dbProps.getProperty("db.url"));
            cfg.setProperty("hibernate.connection.username", dbProps.getProperty("db.username"));
            cfg.setProperty("hibernate.connection.password", dbProps.getProperty("db.password"));
            cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            cfg.setProperty("hibernate.hbm2ddl.auto", "create-drop");
            cfg.setProperty("hibernate.show_sql", "false");
            cfg.setProperty("hibernate.format_sql", "true");
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

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
