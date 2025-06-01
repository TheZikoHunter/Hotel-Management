package com.code.hetelview.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Basic tests for Employee model
 */
class EmployeeBasicTest {

    @Test
    void testEmployeeCreation() {
        // Arrange & Act
        Employee employee = new Employee(1, "testuser", "password123", "Test User", "admin");
        
        // Assert
        assertThat(employee.getId()).isEqualTo(1);
        assertThat(employee.getUsername()).isEqualTo("testuser");
        assertThat(employee.getPassword()).isEqualTo("password123");
        assertThat(employee.getFullName()).isEqualTo("Test User");
        assertThat(employee.getRole()).isEqualTo("admin");
    }

    @Test
    void testEmployeeSetters() {
        // Arrange
        Employee employee = new Employee();
        
        // Act
        employee.setId(2);
        employee.setUsername("newuser");
        employee.setPassword("newpass");
        employee.setFullName("New User");
        employee.setRole("réceptionniste");
        
        // Assert
        assertThat(employee.getId()).isEqualTo(2);
        assertThat(employee.getUsername()).isEqualTo("newuser");
        assertThat(employee.getPassword()).isEqualTo("newpass");
        assertThat(employee.getFullName()).isEqualTo("New User");
        assertThat(employee.getRole()).isEqualTo("réceptionniste");
    }

    @Test
    void testEmployeeEquality() {
        // Arrange
        Employee employee1 = new Employee(1, "user1", "pass1", "User One", "admin");
        Employee employee2 = new Employee(1, "user1", "pass1", "User One", "admin");
        Employee employee3 = new Employee(2, "user2", "pass2", "User Two", "réceptionniste");
        
        // Assert
        assertThat(employee1).isEqualTo(employee2);
        assertThat(employee1).isNotEqualTo(employee3);
        assertThat(employee1.hashCode()).isEqualTo(employee2.hashCode());
    }
}
