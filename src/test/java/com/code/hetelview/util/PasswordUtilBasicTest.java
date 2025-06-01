package com.code.hetelview.util;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Basic tests for utility classes
 */
class PasswordUtilBasicTest {

    @Test
    void testPasswordHashing() {
        // Arrange
        String password = "testPassword123";
        
        // Act
        String hashedPassword1 = PasswordUtil.hashPassword(password);
        String hashedPassword2 = PasswordUtil.hashPassword(password);
        
        // Assert
        assertThat(hashedPassword1).isNotNull();
        assertThat(hashedPassword1).isNotEmpty();
        assertThat(hashedPassword1).isNotEqualTo(password); // Should be hashed
        assertThat(hashedPassword1).isEqualTo(hashedPassword2); // Same input should give same hash
    }

    @Test
    void testPasswordVerification() {
        // Arrange
        String password = "mySecretPassword";
        String hashedPassword = PasswordUtil.hashPassword(password);
        
        // Act & Assert
        assertThat(PasswordUtil.verifyPassword(password, hashedPassword)).isTrue();
        assertThat(PasswordUtil.verifyPassword("wrongPassword", hashedPassword)).isFalse();
        assertThat(PasswordUtil.verifyPassword("", hashedPassword)).isFalse();
        assertThat(PasswordUtil.verifyPassword(null, hashedPassword)).isFalse();
    }

    @Test
    void testEmptyAndNullPasswords() {
        // Act & Assert
        assertThat(PasswordUtil.hashPassword("")).isNotNull();
        assertThat(PasswordUtil.hashPassword(null)).isNotNull();
        
        String emptyHash = PasswordUtil.hashPassword("");
        String nullHash = PasswordUtil.hashPassword(null);
        
        assertThat(PasswordUtil.verifyPassword("", emptyHash)).isTrue();
        assertThat(PasswordUtil.verifyPassword(null, nullHash)).isTrue();
    }

    @Test
    void testDifferentPasswords() {
        // Arrange
        String password1 = "password123";
        String password2 = "password456";
        
        // Act
        String hash1 = PasswordUtil.hashPassword(password1);
        String hash2 = PasswordUtil.hashPassword(password2);
        
        // Assert
        assertThat(hash1).isNotEqualTo(hash2);
        assertThat(PasswordUtil.verifyPassword(password1, hash2)).isFalse();
        assertThat(PasswordUtil.verifyPassword(password2, hash1)).isFalse();
    }
}
