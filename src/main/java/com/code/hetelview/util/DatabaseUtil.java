package com.code.hetelview.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections.
 */
public class DatabaseUtil {
    // Database connection
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/hotel";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "root";

    // Static block to load the JDBC driver
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    /**
     * Get a connection to the database.
     *
     * @return A Connection object representing a connection to the database
     * @throws SQLException If a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    /**
     * Close a database connection safely.
     *
     * @param connection The connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection!");
                e.printStackTrace();
            }
        }
    }
}