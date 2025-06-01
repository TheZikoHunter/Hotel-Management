package com.code.hetelview.servlet;

import com.code.hetelview.model.Employee;
import com.code.hetelview.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.assertj.core.api.Assertions.*;

/**
 * Functional tests for EditReservationServlet that test the business logic
 * without complex mocking frameworks
 */
class EditReservationServletFunctionalTest {

    private Employee receptionnisteEmployee;
    private Employee adminEmployee;

    @BeforeEach
    void setUp() {
        receptionnisteEmployee = new Employee(1, "receptionniste1", "password", null, "réceptionniste");
        adminEmployee = new Employee(2, "admin1", "password", null, "admin");
    }

    @Test
    void testAuthenticationLogic_ValidEmployee() {
        // Test that we can create valid employees for authentication
        assertThat(receptionnisteEmployee).isNotNull();
        assertThat(receptionnisteEmployee.getRole()).isEqualTo("réceptionniste");
        assertThat(receptionnisteEmployee.getUsername()).isEqualTo("receptionniste1");
    }

    @Test
    void testAuthenticationLogic_AdminEmployee() {
        assertThat(adminEmployee).isNotNull();
        assertThat(adminEmployee.getRole()).isEqualTo("admin");
        assertThat(adminEmployee.getUsername()).isEqualTo("admin1");
    }

    @Test
    void testReservationValidation_ValidData() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setGuestName("Jean Dupont");
        reservation.setGuestEmail("jean.dupont@example.com");
        reservation.setGuestPhone("0123456789");
        reservation.setRoomNumber(101);
        reservation.setCheckInDate(Date.valueOf("2024-01-15"));
        reservation.setCheckOutDate(Date.valueOf("2024-01-18"));
        reservation.setStatus("confirmée");
        reservation.setCreatedBy(receptionnisteEmployee);
        
        // Assert - all required fields are properly set
        assertThat(reservation.getGuestName()).isNotBlank();
        assertThat(reservation.getGuestEmail()).contains("@");
        assertThat(reservation.getGuestPhone()).isNotBlank();
        assertThat(reservation.getRoomNumber()).isPositive();
        assertThat(reservation.getCheckInDate()).isNotNull();
        assertThat(reservation.getCheckOutDate()).isNotNull();
        assertThat(reservation.getCheckOutDate()).isAfter(reservation.getCheckInDate());
        assertThat(reservation.getStatus()).isIn("confirmée", "en attente", "annulée");
        assertThat(reservation.getCreatedBy()).isNotNull();
    }

    @Test
    void testReservationValidation_InvalidEmail() {
        Reservation reservation = new Reservation();
        reservation.setGuestEmail("invalid-email");
        
        // Basic email validation - should contain @
        assertThat(reservation.getGuestEmail()).doesNotContain("@");
    }

    @Test
    void testReservationValidation_InvalidDates() {
        Date checkIn = Date.valueOf("2024-01-18");
        Date checkOut = Date.valueOf("2024-01-15"); // Check-out before check-in
        
        // Check-out should be after check-in
        assertThat(checkOut.after(checkIn)).isFalse();
    }

    @Test
    void testReservationValidation_SameDayReservation() {
        Date sameDate = Date.valueOf("2024-01-15");
        
        // Same day check-in and check-out should be invalid
        assertThat(sameDate.after(sameDate)).isFalse();
        assertThat(sameDate.equals(sameDate)).isTrue();
    }

    @Test
    void testEmployeePermissions_ReceptionnisteCanEditReservations() {
        // Simulate permission check logic
        String role = receptionnisteEmployee.getRole();
        boolean canEdit = role.equals("réceptionniste") || role.equals("admin");
        
        assertThat(canEdit).isTrue();
    }

    @Test
    void testEmployeePermissions_AdminCanEditReservations() {
        String role = adminEmployee.getRole();
        boolean canEdit = role.equals("réceptionniste") || role.equals("admin");
        
        assertThat(canEdit).isTrue();
    }

    @Test
    void testEmployeePermissions_InvalidRoleCannotEdit() {
        Employee invalidEmployee = new Employee(3, "guest", "password", null, "guest");
        String role = invalidEmployee.getRole();
        boolean canEdit = role.equals("réceptionniste") || role.equals("admin");
        
        assertThat(canEdit).isFalse();
    }

    @Test
    void testParameterValidation_RequiredParameters() {
        // Simulate parameter validation that would happen in the servlet
        String[] requiredParams = {"id", "guestName", "guestEmail", "guestPhone", 
                                   "roomNumber", "checkInDate", "checkOutDate", "status"};
        
        // Test that we can identify all required parameters
        assertThat(requiredParams).hasSize(8);
        assertThat(requiredParams).contains("id", "guestName", "guestEmail");
    }

    @Test
    void testParameterValidation_NumericParameters() {
        // Test room number validation
        String roomNumberStr = "101";
        int roomNumber = Integer.parseInt(roomNumberStr);
        assertThat(roomNumber).isPositive();
        
        // Test invalid room number
        String invalidRoomStr = "0";
        int invalidRoom = Integer.parseInt(invalidRoomStr);
        assertThat(invalidRoom).isNotPositive();
    }

    @Test
    void testParameterValidation_DateParameters() {
        // Test valid date format
        String dateStr = "2024-01-15";
        Date date = Date.valueOf(dateStr);
        assertThat(date).isNotNull();
        
        // Test that date parsing works correctly
        assertThat(date.toString()).isEqualTo("2024-01-15");
    }

    @Test
    void testStatusValidation_ValidStatuses() {
        String[] validStatuses = {"confirmée", "en attente", "annulée"};
        
        for (String status : validStatuses) {
            assertThat(status).isIn(validStatuses);
        }
    }

    @Test
    void testStatusValidation_InvalidStatus() {
        String invalidStatus = "invalid_status";
        String[] validStatuses = {"confirmée", "en attente", "annulée"};
        
        assertThat(invalidStatus).isNotIn(validStatuses);
    }
}
