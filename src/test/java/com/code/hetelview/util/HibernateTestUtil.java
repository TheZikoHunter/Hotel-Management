package com.code.hetelview.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Hibernate utility class for test environment.
 * Uses a separate test configuration with test database.
 */
public class HibernateTestUtil {
    
    private static SessionFactory sessionFactory;
    
    static {
        try {
            // Create registry using test configuration
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate-test.cfg.xml")
                    .build();
            
            // Create SessionFactory
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Failed to create SessionFactory for tests");
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
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
