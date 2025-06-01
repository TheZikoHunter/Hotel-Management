package com.code.hetelview.servlet;

import com.code.hetelview.model.Employee;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Basic validation tests for servlet logic without mocking
 */
class ServletValidationTest {

    @Test
    void testEmployeeRoleValidation() {
        // Arrange
        Employee receptionniste = new Employee(1, "user1", "pass", "User One", "réceptionniste");
        Employee admin = new Employee(2, "user2", "pass", "User Two", "admin");
        Employee manager = new Employee(3, "user3", "pass", "User Three", "manager");
        
        // Act & Assert - Test role-based access logic that would be in servlets
        assertThat(isReceptionniste(receptionniste)).isTrue();
        assertThat(isReceptionniste(admin)).isFalse();
        assertThat(isReceptionniste(manager)).isFalse();
        
        assertThat(isAdmin(admin)).isTrue();
        assertThat(isAdmin(receptionniste)).isFalse();
        assertThat(isAdmin(manager)).isFalse();
    }

    @Test
    void testParameterValidation() {
        // Test the kind of validation logic that would be in servlets
        assertThat(isValidRoomNumber("101")).isTrue();
        assertThat(isValidRoomNumber("999")).isTrue();
        assertThat(isValidRoomNumber("1")).isTrue();
        
        assertThat(isValidRoomNumber("")).isFalse();
        assertThat(isValidRoomNumber(null)).isFalse();
        assertThat(isValidRoomNumber("abc")).isFalse();
        assertThat(isValidRoomNumber("0")).isFalse();
        assertThat(isValidRoomNumber("-1")).isFalse();
    }

    @Test
    void testDateValidation() {
        // Test date validation logic that would be in servlets
        assertThat(isValidDateFormat("2024-01-15")).isTrue();
        assertThat(isValidDateFormat("2023-12-31")).isTrue();
        assertThat(isValidDateFormat("2025-06-01")).isTrue();
        
        assertThat(isValidDateFormat("")).isFalse();
        assertThat(isValidDateFormat(null)).isFalse();
        assertThat(isValidDateFormat("invalid-date")).isFalse();
        assertThat(isValidDateFormat("2024/01/15")).isFalse();
        assertThat(isValidDateFormat("15-01-2024")).isFalse();
        assertThat(isValidDateFormat("2024-13-01")).isFalse();
        assertThat(isValidDateFormat("2024-01-32")).isFalse();
    }

    @Test
    void testEmailValidation() {
        // Test email validation logic
        assertThat(isValidEmail("user@example.com")).isTrue();
        assertThat(isValidEmail("test.email@domain.org")).isTrue();
        assertThat(isValidEmail("user+tag@example.co.uk")).isTrue();
        
        assertThat(isValidEmail("")).isFalse();
        assertThat(isValidEmail(null)).isFalse();
        assertThat(isValidEmail("invalid-email")).isFalse();
        assertThat(isValidEmail("@example.com")).isFalse();
        assertThat(isValidEmail("user@")).isFalse();
        assertThat(isValidEmail("user@.com")).isFalse();
    }

    @Test
    void testPhoneValidation() {
        // Test phone validation logic
        assertThat(isValidPhone("0123456789")).isTrue();
        assertThat(isValidPhone("123456789")).isTrue();
        assertThat(isValidPhone("+33123456789")).isTrue();
        
        assertThat(isValidPhone("")).isFalse();
        assertThat(isValidPhone(null)).isFalse();
        assertThat(isValidPhone("abc")).isFalse();
        assertThat(isValidPhone("123")).isFalse(); // Too short
        assertThat(isValidPhone("12345678901234567890")).isFalse(); // Too long
    }

    // Helper methods that simulate validation logic from servlets
    private boolean isReceptionniste(Employee employee) {
        return employee != null && "réceptionniste".equals(employee.getRole());
    }

    private boolean isAdmin(Employee employee) {
        return employee != null && "admin".equals(employee.getRole());
    }

    private boolean isValidRoomNumber(String roomNumber) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            return false;
        }
        try {
            int room = Integer.parseInt(roomNumber);
            return room > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDateFormat(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        try {
            java.sql.Date.valueOf(dateStr);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        // Basic email validation
        if (!email.contains("@")) {
            return false;
        }
        
        int atIndex = email.indexOf("@");
        
        // @ should not be at the beginning or end
        if (atIndex <= 0 || atIndex >= email.length() - 1) {
            return false;
        }
        
        // Should have only one @
        if (email.indexOf("@", atIndex + 1) != -1) {
            return false;
        }
        
        String domain = email.substring(atIndex + 1);
        
        // Domain should contain a dot and not start with a dot
        if (!domain.contains(".") || domain.startsWith(".")) {
            return false;
        }
        
        int lastDotIndex = domain.lastIndexOf(".");
        
        // Domain should not end with a dot and should have content after the last dot
        if (lastDotIndex >= domain.length() - 1) {
            return false;
        }
        
        return true;
    }

    private boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String cleanPhone = phone.replaceAll("[^0-9+]", "");
        return cleanPhone.length() >= 8 && cleanPhone.length() <= 15;
    }
}
