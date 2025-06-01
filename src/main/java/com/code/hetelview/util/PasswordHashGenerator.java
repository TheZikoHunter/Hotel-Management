package com.code.hetelview.util;

/**
 * Utility to generate password hashes for database setup.
 */
public class PasswordHashGenerator {
    public static void main(String[] args) {
        // Generate hashes for default passwords
        System.out.println("admin123 hashed: " + PasswordUtil.hashPassword("admin123"));
        System.out.println("john123 hashed: " + PasswordUtil.hashPassword("john123"));
        System.out.println("mary123 hashed: " + PasswordUtil.hashPassword("mary123"));
    }
}
