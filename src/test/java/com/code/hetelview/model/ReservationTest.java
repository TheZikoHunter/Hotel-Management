package com.code.hetelview.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;
import java.sql.Date;

/**
 * Unit tests for Reservation model class.
 */
@DisplayName("Tests du modèle Reservation")
class ReservationTest {
    
    private Reservation reservation;
    private Employee employee;
    
    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        employee = new Employee(1, "receptionniste1", "password", "Marie Leclerc", "réceptionniste");
    }
    
    @Test
    @DisplayName("Devrait créer une réservation avec le constructeur par défaut")
    void shouldCreateReservationWithDefaultConstructor() {
        // Given & When
        Reservation res = new Reservation();
        
        // Then
        assertThat(res).isNotNull();
        assertThat(res.getId()).isEqualTo(0);
        assertThat(res.getGuestName()).isNull();
        assertThat(res.getGuestEmail()).isNull();
        assertThat(res.getGuestPhone()).isNull();
        assertThat(res.getRoomNumber()).isEqualTo(0);
        assertThat(res.getCheckInDate()).isNull();
        assertThat(res.getCheckOutDate()).isNull();
        assertThat(res.getStatus()).isNull();
        assertThat(res.getCreatedBy()).isNull();
        assertThat(res.getNotes()).isNull();
    }
    
    @Test
    @DisplayName("Devrait créer une réservation avec le constructeur paramétré")
    void shouldCreateReservationWithParameterizedConstructor() {
        // Given
        Date checkIn = Date.valueOf("2025-06-15");
        Date checkOut = Date.valueOf("2025-06-20");
        
        // When
        Reservation res = new Reservation(1, "Alice Brown", "alice@example.com", "555-1234",
                                         101, checkIn, checkOut, "Confirmé", employee, "Client VIP");
        
        // Then
        assertThat(res.getId()).isEqualTo(1);
        assertThat(res.getGuestName()).isEqualTo("Alice Brown");
        assertThat(res.getGuestEmail()).isEqualTo("alice@example.com");
        assertThat(res.getGuestPhone()).isEqualTo("555-1234");
        assertThat(res.getRoomNumber()).isEqualTo(101);
        assertThat(res.getCheckInDate()).isEqualTo(checkIn);
        assertThat(res.getCheckOutDate()).isEqualTo(checkOut);
        assertThat(res.getStatus()).isEqualTo("Confirmé");
        assertThat(res.getCreatedBy()).isEqualTo(employee);
        assertThat(res.getNotes()).isEqualTo("Client VIP");
    }
    
    @Test
    @DisplayName("Devrait définir et récupérer l'ID correctement")
    void shouldSetAndGetIdCorrectly() {
        // Given
        int expectedId = 42;
        
        // When
        reservation.setId(expectedId);
        
        // Then
        assertThat(reservation.getId()).isEqualTo(expectedId);
    }
    
    @Test
    @DisplayName("Devrait définir et récupérer le nom du client correctement")
    void shouldSetAndGetGuestNameCorrectly() {
        // Given
        String expectedName = "Jean Dupont";
        
        // When
        reservation.setGuestName(expectedName);
        
        // Then
        assertThat(reservation.getGuestName()).isEqualTo(expectedName);
    }
    
    @Test
    @DisplayName("Devrait définir et récupérer l'email du client correctement")
    void shouldSetAndGetGuestEmailCorrectly() {
        // Given
        String expectedEmail = "jean.dupont@example.com";
        
        // When
        reservation.setGuestEmail(expectedEmail);
        
        // Then
        assertThat(reservation.getGuestEmail()).isEqualTo(expectedEmail);
    }
    
    @Test
    @DisplayName("Devrait définir et récupérer le téléphone du client correctement")
    void shouldSetAndGetGuestPhoneCorrectly() {
        // Given
        String expectedPhone = "01-23-45-67-89";
        
        // When
        reservation.setGuestPhone(expectedPhone);
        
        // Then
        assertThat(reservation.getGuestPhone()).isEqualTo(expectedPhone);
    }
    
    @Test
    @DisplayName("Devrait définir et récupérer le numéro de chambre correctement")
    void shouldSetAndGetRoomNumberCorrectly() {
        // Given
        int expectedRoomNumber = 205;
        
        // When
        reservation.setRoomNumber(expectedRoomNumber);
        
        // Then
        assertThat(reservation.getRoomNumber()).isEqualTo(expectedRoomNumber);
    }
    
    @Test
    @DisplayName("Devrait définir et récupérer la date d'arrivée correctement")
    void shouldSetAndGetCheckInDateCorrectly() {
        // Given
        Date expectedCheckIn = Date.valueOf("2025-07-15");
        
        // When
        reservation.setCheckInDate(expectedCheckIn);
        
        // Then
        assertThat(reservation.getCheckInDate()).isEqualTo(expectedCheckIn);
    }
    
    @Test
    @DisplayName("Devrait définir et récupérer la date de départ correctement")
    void shouldSetAndGetCheckOutDateCorrectly() {
        // Given
        Date expectedCheckOut = Date.valueOf("2025-07-20");
        
        // When
        reservation.setCheckOutDate(expectedCheckOut);
        
        // Then
        assertThat(reservation.getCheckOutDate()).isEqualTo(expectedCheckOut);
    }
    
    @Test
    @DisplayName("Devrait définir et récupérer le statut correctement")
    void shouldSetAndGetStatusCorrectly() {
        // Given
        String expectedStatus = "En attente";
        
        // When
        reservation.setStatus(expectedStatus);
        
        // Then
        assertThat(reservation.getStatus()).isEqualTo(expectedStatus);
    }
    
    @Test
    @DisplayName("Devrait supporter tous les statuts de réservation")
    void shouldSupportAllReservationStatuses() {
        // Given
        String[] statuses = {"Confirmé", "En attente", "Parti", "Annulé", "No-show"};
        
        // When & Then
        for (String status : statuses) {
            reservation.setStatus(status);
            assertThat(reservation.getStatus()).isEqualTo(status);
        }
    }
    
    @Test
    @DisplayName("Devrait définir et récupérer l'employé créateur correctement")
    void shouldSetAndGetCreatedByCorrectly() {
        // When
        reservation.setCreatedBy(employee);
        
        // Then
        assertThat(reservation.getCreatedBy()).isEqualTo(employee);
        assertThat(reservation.getCreatedBy().getRole()).isEqualTo("réceptionniste");
    }
    
    @Test
    @DisplayName("Devrait définir et récupérer les notes correctement")
    void shouldSetAndGetNotesCorrectly() {
        // Given
        String expectedNotes = "Client préfère une chambre avec vue sur mer";
        
        // When
        reservation.setNotes(expectedNotes);
        
        // Then
        assertThat(reservation.getNotes()).isEqualTo(expectedNotes);
    }
    
    @Test
    @DisplayName("Devrait gérer les notes vides et nulles")
    void shouldHandleEmptyAndNullNotes() {
        // Test avec null
        reservation.setNotes(null);
        assertThat(reservation.getNotes()).isNull();
        
        // Test avec chaîne vide
        reservation.setNotes("");
        assertThat(reservation.getNotes()).isEmpty();
        
        // Test avec espaces seulement
        reservation.setNotes("   ");
        assertThat(reservation.getNotes()).isEqualTo("   ");
    }
    
    @Test
    @DisplayName("Devrait valider la cohérence des dates de réservation")
    void shouldValidateReservationDateConsistency() {
        // Given
        Date checkIn = Date.valueOf("2025-06-15");
        Date checkOut = Date.valueOf("2025-06-20");
        
        // When
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);
        
        // Then
        assertThat(reservation.getCheckOutDate()).isAfter(reservation.getCheckInDate());
    }
    
    @Test
    @DisplayName("Devrait générer toString correctement")
    void shouldGenerateToStringCorrectly() {
        // Given
        Date checkIn = Date.valueOf("2025-06-15");
        Date checkOut = Date.valueOf("2025-06-20");
        
        reservation.setId(1);
        reservation.setGuestName("Test Guest");
        reservation.setGuestEmail("test@example.com");
        reservation.setGuestPhone("555-1234");
        reservation.setRoomNumber(101);
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);
        reservation.setStatus("Confirmé");
        reservation.setCreatedBy(employee);
        reservation.setNotes("Test notes");
        
        // When
        String result = reservation.toString();
        
        // Then
        assertThat(result)
            .contains("Reservation{")
            .contains("id=1")
            .contains("guestName='Test Guest'")
            .contains("guestEmail='test@example.com'")
            .contains("guestPhone='555-1234'")
            .contains("roomNumber=101")
            .contains("status='Confirmé'")
            .contains("notes='Test notes'");
    }
    
    @Test
    @DisplayName("Devrait gérer les valeurs nulles gracieusement")
    void shouldHandleNullValuesGracefully() {
        // When
        reservation.setGuestName(null);
        reservation.setGuestEmail(null);
        reservation.setGuestPhone(null);
        reservation.setCheckInDate(null);
        reservation.setCheckOutDate(null);
        reservation.setStatus(null);
        reservation.setCreatedBy(null);
        reservation.setNotes(null);
        
        // Then
        assertThat(reservation.getGuestName()).isNull();
        assertThat(reservation.getGuestEmail()).isNull();
        assertThat(reservation.getGuestPhone()).isNull();
        assertThat(reservation.getCheckInDate()).isNull();
        assertThat(reservation.getCheckOutDate()).isNull();
        assertThat(reservation.getStatus()).isNull();
        assertThat(reservation.getCreatedBy()).isNull();
        assertThat(reservation.getNotes()).isNull();
        
        // ToString ne doit pas lever d'exception avec des valeurs nulles
        assertThatCode(() -> reservation.toString()).doesNotThrowAnyException();
    }
}
