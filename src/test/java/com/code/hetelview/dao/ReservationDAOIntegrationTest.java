package com.code.hetelview.dao;

import com.code.hetelview.model.Employee;
import com.code.hetelview.model.Reservation;
import com.code.hetelview.util.HibernateTestUtil;
import com.code.hetelview.util.PasswordUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import java.sql.Date;
import java.util.List;

/**
 * Tests d'intégration pour ReservationDAO.
 * Utilise une base de données de test MySQL.
 */
@DisplayName("Tests d'intégration ReservationDAO")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReservationDAOIntegrationTest {
    
    private ReservationDAO reservationDAO;
    private EmployeeDAO employeeDAO;
    private Employee testEmployee;
    private Reservation testReservation;
    
    @BeforeAll
    void setUpDatabase() {
        // Créer les tables et insérer des données de test
        try (Session session = HibernateTestUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            
            // Insérer un employé de test pour les réservations
            Employee receptionniste = new Employee();
            receptionniste.setUsername("receptionniste_test");
            receptionniste.setPassword(PasswordUtil.hashPassword("password123"));
            receptionniste.setFullName("Réceptionniste Test");
            receptionniste.setRole("réceptionniste");
            session.save(receptionniste);
            
            tx.commit();
        }
    }
    
    @BeforeEach
    void setUp() {
        reservationDAO = new ReservationDAO();
        employeeDAO = new EmployeeDAO();
        
        // Récupérer l'employé de test
        testEmployee = employeeDAO.getEmployeeByUsername("receptionniste_test");
        
        // Créer une réservation de test
        testReservation = new Reservation();
        testReservation.setGuestName("Client Test");
        testReservation.setGuestEmail("client.test@example.com");
        testReservation.setGuestPhone("555-0123");
        testReservation.setRoomNumber(101);
        testReservation.setCheckInDate(Date.valueOf("2025-06-15"));
        testReservation.setCheckOutDate(Date.valueOf("2025-06-20"));
        testReservation.setStatus("Confirmé");
        testReservation.setCreatedBy(testEmployee);
        testReservation.setNotes("Réservation de test");
    }
    
    @AfterAll
    void tearDownDatabase() {
        HibernateTestUtil.shutdown();
    }
    
    @Test
    @DisplayName("Devrait ajouter une nouvelle réservation")
    void shouldAddNewReservation() {
        // When
        boolean result = reservationDAO.addReservation(testReservation);
        
        // Then
        assertThat(result).isTrue();
        assertThat(testReservation.getId()).isGreaterThan(0);
    }
    
    @Test
    @DisplayName("Devrait récupérer toutes les réservations")
    void shouldGetAllReservations() {
        // Given
        reservationDAO.addReservation(testReservation);
        
        // When
        List<Reservation> reservations = reservationDAO.getAllReservations();
        
        // Then
        assertThat(reservations).isNotNull();
        assertThat(reservations).hasSizeGreaterThanOrEqualTo(1);
        
        // Vérifier que notre réservation de test est présente
        assertThat(reservations)
            .extracting(Reservation::getGuestName)
            .contains("Client Test");
    }
    
    @Test
    @DisplayName("Devrait récupérer une réservation par ID")
    void shouldGetReservationById() {
        // Given
        reservationDAO.addReservation(testReservation);
        int reservationId = testReservation.getId();
        
        // When
        Reservation reservation = reservationDAO.getReservationById(reservationId);
        
        // Then
        assertThat(reservation).isNotNull();
        assertThat(reservation.getId()).isEqualTo(reservationId);
        assertThat(reservation.getGuestName()).isEqualTo("Client Test");
        assertThat(reservation.getGuestEmail()).isEqualTo("client.test@example.com");
        assertThat(reservation.getRoomNumber()).isEqualTo(101);
    }
    
    @Test
    @DisplayName("Devrait retourner null pour un ID de réservation inexistant")
    void shouldReturnNullForNonExistentReservationId() {
        // When
        Reservation reservation = reservationDAO.getReservationById(99999);
        
        // Then
        assertThat(reservation).isNull();
    }
    
    @Test
    @DisplayName("Devrait mettre à jour une réservation existante")
    void shouldUpdateExistingReservation() {
        // Given
        reservationDAO.addReservation(testReservation);
        
        // Modifier les informations
        testReservation.setGuestName("Client Test Modifié");
        testReservation.setRoomNumber(205);
        testReservation.setStatus("En attente");
        testReservation.setNotes("Réservation modifiée");
        
        // When
        boolean result = reservationDAO.updateReservation(testReservation);
        
        // Then
        assertThat(result).isTrue();
        
        // Vérifier la mise à jour
        Reservation updatedReservation = reservationDAO.getReservationById(testReservation.getId());
        assertThat(updatedReservation.getGuestName()).isEqualTo("Client Test Modifié");
        assertThat(updatedReservation.getRoomNumber()).isEqualTo(205);
        assertThat(updatedReservation.getStatus()).isEqualTo("En attente");
        assertThat(updatedReservation.getNotes()).isEqualTo("Réservation modifiée");
    }
    
    @Test
    @DisplayName("Devrait supprimer une réservation existante")
    void shouldDeleteExistingReservation() {
        // Given
        reservationDAO.addReservation(testReservation);
        int reservationId = testReservation.getId();
        
        // When
        boolean result = reservationDAO.deleteReservation(reservationId);
        
        // Then
        assertThat(result).isTrue();
        
        // Vérifier la suppression
        Reservation deletedReservation = reservationDAO.getReservationById(reservationId);
        assertThat(deletedReservation).isNull();
    }
    
    @Test
    @DisplayName("Devrait échouer la suppression d'une réservation inexistante")
    void shouldFailToDeleteNonExistentReservation() {
        // When
        boolean result = reservationDAO.deleteReservation(99999);
        
        // Then
        assertThat(result).isFalse();
    }
    
    @Test
    @DisplayName("Devrait préserver la relation avec l'employé créateur")
    void shouldPreserveEmployeeRelationship() {
        // Given
        reservationDAO.addReservation(testReservation);
        
        // When
        Reservation savedReservation = reservationDAO.getReservationById(testReservation.getId());
        
        // Then
        assertThat(savedReservation.getCreatedBy()).isNotNull();
        assertThat(savedReservation.getCreatedBy().getId()).isEqualTo(testEmployee.getId());
        assertThat(savedReservation.getCreatedBy().getUsername()).isEqualTo("receptionniste_test");
        assertThat(savedReservation.getCreatedBy().getRole()).isEqualTo("réceptionniste");
    }
    
    @Test
    @DisplayName("Devrait supporter tous les statuts de réservation")
    void shouldSupportAllReservationStatuses() {
        // Given
        String[] statuses = {"Confirmé", "En attente", "Parti", "Annulé", "No-show"};
        
        // When & Then
        for (String status : statuses) {
            Reservation reservation = new Reservation();
            reservation.setGuestName("Client " + status);
            reservation.setGuestEmail("client." + status.toLowerCase() + "@example.com");
            reservation.setGuestPhone("555-000" + (statuses.length % 10));
            reservation.setRoomNumber(100 + statuses.length);
            reservation.setCheckInDate(Date.valueOf("2025-06-15"));
            reservation.setCheckOutDate(Date.valueOf("2025-06-20"));
            reservation.setStatus(status);
            reservation.setCreatedBy(testEmployee);
            reservation.setNotes("Test status: " + status);
            
            boolean result = reservationDAO.addReservation(reservation);
            assertThat(result).isTrue();
            
            Reservation savedReservation = reservationDAO.getReservationById(reservation.getId());
            assertThat(savedReservation.getStatus()).isEqualTo(status);
        }
    }
    
    @Test
    @DisplayName("Devrait gérer les dates de réservation correctement")
    void shouldHandleReservationDatesCorrectly() {
        // Given
        Date checkIn = Date.valueOf("2025-12-24");
        Date checkOut = Date.valueOf("2025-12-31");
        
        testReservation.setCheckInDate(checkIn);
        testReservation.setCheckOutDate(checkOut);
        
        // When
        reservationDAO.addReservation(testReservation);
        Reservation savedReservation = reservationDAO.getReservationById(testReservation.getId());
        
        // Then
        assertThat(savedReservation.getCheckInDate()).isEqualTo(checkIn);
        assertThat(savedReservation.getCheckOutDate()).isEqualTo(checkOut);
        assertThat(savedReservation.getCheckOutDate()).isAfter(savedReservation.getCheckInDate());
    }
    
    @Test
    @DisplayName("Devrait gérer les notes vides et nulles")
    void shouldHandleEmptyAndNullNotes() {
        // Test avec notes nulles
        testReservation.setNotes(null);
        reservationDAO.addReservation(testReservation);
        
        Reservation reservation1 = reservationDAO.getReservationById(testReservation.getId());
        assertThat(reservation1.getNotes()).isNull();
        
        // Test avec notes vides
        Reservation reservation2 = new Reservation();
        reservation2.setGuestName("Client Notes Vides");
        reservation2.setGuestEmail("empty@example.com");
        reservation2.setGuestPhone("555-0000");
        reservation2.setRoomNumber(102);
        reservation2.setCheckInDate(Date.valueOf("2025-06-15"));
        reservation2.setCheckOutDate(Date.valueOf("2025-06-20"));
        reservation2.setStatus("Confirmé");
        reservation2.setCreatedBy(testEmployee);
        reservation2.setNotes("");
        
        reservationDAO.addReservation(reservation2);
        Reservation savedReservation2 = reservationDAO.getReservationById(reservation2.getId());
        assertThat(savedReservation2.getNotes()).isEmpty();
    }
    
    @Test
    @DisplayName("Devrait trier les réservations par date d'arrivée")
    void shouldSortReservationsByCheckInDate() {
        // Given - Ajouter plusieurs réservations avec des dates différentes
        Reservation res1 = createReservationWithDates("2025-06-10", "2025-06-15", "Client 1");
        Reservation res2 = createReservationWithDates("2025-06-20", "2025-06-25", "Client 2");
        Reservation res3 = createReservationWithDates("2025-06-01", "2025-06-05", "Client 3");
        
        reservationDAO.addReservation(res1);
        reservationDAO.addReservation(res2);
        reservationDAO.addReservation(res3);
        
        // When
        List<Reservation> reservations = reservationDAO.getAllReservations();
        
        // Then - Vérifier que les réservations sont triées par date d'arrivée (DESC)
        assertThat(reservations).hasSizeGreaterThanOrEqualTo(3);
        
        // Trouver nos réservations de test dans la liste
        List<Reservation> testReservations = reservations.stream()
            .filter(r -> r.getGuestName().startsWith("Client "))
            .toList();
        
        assertThat(testReservations).hasSize(3);
        
        // Vérifier l'ordre (plus récent en premier)
        Date previousDate = null;
        for (Reservation reservation : testReservations) {
            if (previousDate != null) {
                assertThat(reservation.getCheckInDate())
                    .isBeforeOrEqualTo(previousDate);
            }
            previousDate = reservation.getCheckInDate();
        }
    }
    
    @Test
    @DisplayName("Devrait gérer les erreurs de base de données gracieusement")
    void shouldHandleDatabaseErrorsGracefully() {
        // Given - Réservation avec un employé inexistant
        Reservation invalidReservation = new Reservation();
        invalidReservation.setGuestName("Client Invalide");
        invalidReservation.setGuestEmail("invalid@example.com");
        invalidReservation.setGuestPhone("555-9999");
        invalidReservation.setRoomNumber(999);
        invalidReservation.setCheckInDate(Date.valueOf("2025-06-15"));
        invalidReservation.setCheckOutDate(Date.valueOf("2025-06-20"));
        invalidReservation.setStatus("Confirmé");
        
        Employee nonExistentEmployee = new Employee();
        nonExistentEmployee.setId(99999); // ID inexistant
        invalidReservation.setCreatedBy(nonExistentEmployee);
        
        // When
        boolean result = reservationDAO.addReservation(invalidReservation);
        
        // Then
        assertThat(result).isFalse();
    }
    
    private Reservation createReservationWithDates(String checkIn, String checkOut, String guestName) {
        Reservation reservation = new Reservation();
        reservation.setGuestName(guestName);
        reservation.setGuestEmail(guestName.toLowerCase().replace(" ", "") + "@example.com");
        reservation.setGuestPhone("555-123" + guestName.charAt(guestName.length() - 1));
        reservation.setRoomNumber(100 + Integer.parseInt(guestName.substring(guestName.length() - 1)));
        reservation.setCheckInDate(Date.valueOf(checkIn));
        reservation.setCheckOutDate(Date.valueOf(checkOut));
        reservation.setStatus("Confirmé");
        reservation.setCreatedBy(testEmployee);
        reservation.setNotes("Test reservation " + guestName);
        return reservation;
    }
}
