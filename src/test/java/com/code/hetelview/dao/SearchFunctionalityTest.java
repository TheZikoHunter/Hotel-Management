package com.code.hetelview.dao;

import com.code.hetelview.model.Employee;
import com.code.hetelview.model.Reservation;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.sql.Date;
import java.util.List;

/**
 * Basic tests for search functionality in DAOs
 */
class SearchFunctionalityTest {

    @Test
    void testReservationSearchParameters() {
        // Test that search method accepts all parameters without compilation errors
        ReservationDAO reservationDAO = new ReservationDAO();
        
        // This should compile without errors - we're testing the method signature
        assertThatCode(() -> {
            List<Reservation> results = reservationDAO.searchReservations(
                "testGuest", 
                "test@email.com", 
                101, 
                "confirmed", 
                Date.valueOf("2024-01-01"), 
                Date.valueOf("2024-12-31")
            );
            assertThat(results).isNotNull();
        }).doesNotThrowAnyException();
    }

    @Test
    void testReservationSearchByStatus() {
        // Test status filtering method
        ReservationDAO reservationDAO = new ReservationDAO();
        
        assertThatCode(() -> {
            List<Reservation> results = reservationDAO.getReservationsByStatus("confirmed");
            assertThat(results).isNotNull();
        }).doesNotThrowAnyException();
    }

    @Test
    void testEmployeeSearchParameters() {
        // Test that employee search method accepts all parameters without compilation errors
        EmployeeDAO employeeDAO = new EmployeeDAO();
        
        assertThatCode(() -> {
            List<Employee> results = employeeDAO.searchEmployees(
                "testuser", 
                "Test User", 
                "admin"
            );
            assertThat(results).isNotNull();
        }).doesNotThrowAnyException();
    }

    @Test
    void testEmployeeSearchByRole() {
        // Test role filtering method
        EmployeeDAO employeeDAO = new EmployeeDAO();
        
        assertThatCode(() -> {
            List<Employee> results = employeeDAO.getEmployeesByRole("réceptionniste");
            assertThat(results).isNotNull();
        }).doesNotThrowAnyException();
    }

    @Test
    void testSearchParameterValidation() {
        // Test that null/empty parameters are handled gracefully
        ReservationDAO reservationDAO = new ReservationDAO();
        EmployeeDAO employeeDAO = new EmployeeDAO();
        
        assertThatCode(() -> {
            // Test with null parameters
            reservationDAO.searchReservations(null, null, null, null, null, null);
            employeeDAO.searchEmployees(null, null, null);
            
            // Test with empty strings
            reservationDAO.searchReservations("", "", null, "", null, null);
            employeeDAO.searchEmployees("", "", "");
        }).doesNotThrowAnyException();
    }
}
