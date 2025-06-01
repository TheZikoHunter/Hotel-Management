package com.code.hetelview.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for password hashing operations.
 */
public class PasswordUtil {

    /**
     * Hash a password using SHA-256.
     * 
     * @param password The plain text password
     * @return The hashed password as a hexadecimal string
     */
    public static String hashPassword(String password) {
        try {
            // Handle null password by treating it as empty string
            if (password == null) {
                password = "";
            }
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verify a password against a hash.
     * 
     * @param password The plain text password
     * @param hash The hashed password to compare against
     * @return true if the password matches the hash, false otherwise
     */
    public static boolean verifyPassword(String password, String hash) {
        if (hash == null) {
            return false;
        }
        return hashPassword(password).equals(hash);
    }
}
