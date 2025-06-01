package com.code.hetelview.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import java.sql.Date;

/**
 * Basic tests for Reservation model
 */
class ReservationBasicTest {

    @Test
    void testReservationCreation() {
        // Arrange
        Employee employee = new Employee(1, "testuser", "password", "Test User", "réceptionniste");
        Date checkIn = Date.valueOf("2024-01-15");
        Date checkOut = Date.valueOf("2024-01-18");
        
        // Act
        Reservation reservation = new Reservation(
            1, 
            "John Doe", 
            "john@example.com", 
            "123456789", 
            101, 
            checkIn, 
            checkOut, 
            "confirmée", 
            employee, 
            "Test notes"
        );
        
        // Assert
        assertThat(reservation.getId()).isEqualTo(1);
        assertThat(reservation.getGuestName()).isEqualTo("John Doe");
        assertThat(reservation.getGuestEmail()).isEqualTo("john@example.com");
        assertThat(reservation.getGuestPhone()).isEqualTo("123456789");
        assertThat(reservation.getRoomNumber()).isEqualTo(101);
        assertThat(reservation.getCheckInDate()).isEqualTo(checkIn);
        assertThat(reservation.getCheckOutDate()).isEqualTo(checkOut);
        assertThat(reservation.getStatus()).isEqualTo("confirmée");
        assertThat(reservation.getCreatedBy()).isEqualTo(employee);
        assertThat(reservation.getNotes()).isEqualTo("Test notes");
    }

    @Test
    void testReservationSetters() {
        // Arrange
        Reservation reservation = new Reservation();
        Employee employee = new Employee(2, "user2", "pass", "User Two", "admin");
        Date checkIn = Date.valueOf("2024-02-01");
        Date checkOut = Date.valueOf("2024-02-05");
        
        // Act
        reservation.setId(5);
        reservation.setGuestName("Jane Smith");
        reservation.setGuestEmail("jane@example.com");
        reservation.setGuestPhone("987654321");
        reservation.setRoomNumber(205);
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);
        reservation.setStatus("en attente");
        reservation.setCreatedBy(employee);
        reservation.setNotes("Updated notes");
        
        // Assert
        assertThat(reservation.getId()).isEqualTo(5);
        assertThat(reservation.getGuestName()).isEqualTo("Jane Smith");
        assertThat(reservation.getGuestEmail()).isEqualTo("jane@example.com");
        assertThat(reservation.getGuestPhone()).isEqualTo("987654321");
        assertThat(reservation.getRoomNumber()).isEqualTo(205);
        assertThat(reservation.getCheckInDate()).isEqualTo(checkIn);
        assertThat(reservation.getCheckOutDate()).isEqualTo(checkOut);
        assertThat(reservation.getStatus()).isEqualTo("en attente");
        assertThat(reservation.getCreatedBy()).isEqualTo(employee);
        assertThat(reservation.getNotes()).isEqualTo("Updated notes");
    }

    @Test
    void testReservationEquality() {
        // Arrange
        Employee employee = new Employee(1, "user", "pass", "User", "réceptionniste");
        Date checkIn = Date.valueOf("2024-01-15");
        Date checkOut = Date.valueOf("2024-01-18");
        
        Reservation reservation1 = new Reservation(1, "John Doe", "john@example.com", "123456789", 
                                                  101, checkIn, checkOut, "confirmée", employee, "Notes");
        Reservation reservation2 = new Reservation(1, "John Doe", "john@example.com", "123456789", 
                                                  101, checkIn, checkOut, "confirmée", employee, "Notes");
        Reservation reservation3 = new Reservation(2, "Jane Smith", "jane@example.com", "987654321", 
                                                  102, checkIn, checkOut, "en attente", employee, "Other notes");
        
        // Assert
        assertThat(reservation1).isEqualTo(reservation2);
        assertThat(reservation1).isNotEqualTo(reservation3);
        assertThat(reservation1.hashCode()).isEqualTo(reservation2.hashCode());
    }

    @Test
    void testDefaultConstructor() {
        // Act
        Reservation reservation = new Reservation();
        
        // Assert
        assertThat(reservation.getId()).isEqualTo(0);
        assertThat(reservation.getGuestName()).isNull();
        assertThat(reservation.getGuestEmail()).isNull();
        assertThat(reservation.getGuestPhone()).isNull();
        assertThat(reservation.getRoomNumber()).isEqualTo(0);
        assertThat(reservation.getCheckInDate()).isNull();
        assertThat(reservation.getCheckOutDate()).isNull();
        assertThat(reservation.getStatus()).isNull();
        assertThat(reservation.getCreatedBy()).isNull();
        assertThat(reservation.getNotes()).isNull();
    }
}
