package com.code.hetelview.servlet;

import com.code.hetelview.dao.ReservationDAO;
import com.code.hetelview.model.Employee;
import com.code.hetelview.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for EditReservationServlet
 */
@ExtendWith(MockitoExtension.class)
class EditReservationServletTest {

    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private HttpSession session;
    
    @Mock
    private RequestDispatcher dispatcher;
    
    @Mock
    private ReservationDAO reservationDAO;
    
    private EditReservationServlet servlet;
    private Employee receptionnisteEmployee;
    private Employee adminEmployee;
    private Reservation testReservation;

    @BeforeEach
    void setUp() throws ServletException {
        servlet = new EditReservationServlet();
        
        // Create test employees
        receptionnisteEmployee = new Employee(1, "receptionniste1", "password", null, "réceptionniste");
        adminEmployee = new Employee(2, "admin1", "password", null, "admin");
        
        // Create test reservation
        testReservation = new Reservation();
        testReservation.setId(123);
        testReservation.setGuestName("Jean Dupont");
        testReservation.setGuestEmail("jean.dupont@example.com");
        testReservation.setGuestPhone("0123456789");
        testReservation.setRoomNumber(101);
        testReservation.setCheckInDate(Date.valueOf("2024-01-15"));
        testReservation.setCheckOutDate(Date.valueOf("2024-01-18"));
        testReservation.setStatus("confirmée");
        testReservation.setCreatedBy(receptionnisteEmployee);
        testReservation.setNotes("Notes existantes");
        
        // Mock request dispatcher
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        
        // Use reflection to set the DAO
        try {
            java.lang.reflect.Field daoField = EditReservationServlet.class.getDeclaredField("reservationDAO");
            daoField.setAccessible(true);
            daoField.set(servlet, reservationDAO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ======================== doGet Tests ========================

    @Test
    void testDoGet_NoSession_RedirectsToLogin() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(null);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("login");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoGet_NoEmployeeInSession_RedirectsToLogin() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(null);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("login");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoGet_NonReceptionniste_RedirectsToDashboardWithError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("dashboard?error=Accès refusé. Seuls les réceptionnistes peuvent modifier les réservations.");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoGet_MissingId_RedirectsToDashboardWithError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn(null);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("dashboard?error=Invalid reservation ID");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoGet_EmptyId_RedirectsToDashboardWithError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("  ");
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("dashboard?error=Invalid reservation ID");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoGet_InvalidIdFormat_RedirectsToDashboardWithError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("invalid");
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("dashboard?error=ID de réservation invalide");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoGet_ReservationNotFound_RedirectsToDashboardWithError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("123");
        when(reservationDAO.getReservationById(123)).thenReturn(null);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("dashboard?error=Réservation non trouvée");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoGet_ValidReservation_ForwardsToEditPage() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("123");
        when(reservationDAO.getReservationById(123)).thenReturn(testReservation);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(request).setAttribute("reservation", testReservation);
        verify(request).getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp");
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    // ======================== doPost Tests ========================

    @Test
    void testDoPost_NoSession_RedirectsToLogin() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(null);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(response).sendRedirect("login");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoPost_NonReceptionniste_RedirectsToDashboardWithError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(response).sendRedirect("dashboard?error=Accès refusé. Seuls les réceptionnistes peuvent modifier les réservations.");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoPost_MissingId_RedirectsToDashboardWithError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn(null);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(response).sendRedirect("dashboard?error=Invalid reservation ID");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoPost_MissingRequiredFields_ShowsError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("123");
        
        // Missing guestName
        when(request.getParameter("guestName")).thenReturn("");
        when(request.getParameter("guestEmail")).thenReturn("guest@example.com");
        when(request.getParameter("guestPhone")).thenReturn("123456789");
        when(request.getParameter("roomNumber")).thenReturn("101");
        when(request.getParameter("checkInDate")).thenReturn("2024-01-15");
        when(request.getParameter("checkOutDate")).thenReturn("2024-01-18");
        when(request.getParameter("status")).thenReturn("confirmée");
        
        when(reservationDAO.getReservationById(123)).thenReturn(testReservation);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(request).setAttribute("error", "All fields except notes are required");
        verify(request).setAttribute("reservation", testReservation);
        verify(request).getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_ReservationNotFound_RedirectsToDashboardWithError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("123");
        
        setupValidUpdateParameters();
        when(reservationDAO.getReservationById(123)).thenReturn(null);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(response).sendRedirect("dashboard?error=Réservation non trouvée");
    }

    @Test
    void testDoPost_InvalidRoomNumber_ShowsError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("123");
        
        setupValidUpdateParameters();
        when(request.getParameter("roomNumber")).thenReturn("invalid");
        when(reservationDAO.getReservationById(123)).thenReturn(testReservation);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(request).setAttribute("error", "Numéro de chambre invalide");
        verify(request).setAttribute("reservation", testReservation);
        verify(request).getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_InvalidDateFormat_ShowsError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("123");
        
        setupValidUpdateParameters();
        when(request.getParameter("checkInDate")).thenReturn("invalid-date");
        when(reservationDAO.getReservationById(123)).thenReturn(testReservation);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(request).setAttribute("error", "Format de date invalide");
        verify(request).setAttribute("reservation", testReservation);
        verify(request).getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_CheckOutBeforeCheckIn_ShowsError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("123");
        
        setupValidUpdateParameters();
        when(request.getParameter("checkInDate")).thenReturn("2024-01-18");
        when(request.getParameter("checkOutDate")).thenReturn("2024-01-15"); // Before check-in
        when(reservationDAO.getReservationById(123)).thenReturn(testReservation);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(request).setAttribute("error", "La date de départ doit être postérieure à la date d'arrivée");
        verify(request).setAttribute("reservation", testReservation);
        verify(request).getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_ValidUpdate_Success() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("123");
        
        setupValidUpdateParameters();
        when(reservationDAO.getReservationById(123)).thenReturn(testReservation);
        when(reservationDAO.updateReservation(any(Reservation.class))).thenReturn(true);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(reservationDAO).updateReservation(any(Reservation.class));
        verify(response).sendRedirect("dashboard?success=Réservation mise à jour avec succès");
        verify(request, never()).setAttribute(eq("error"), anyString());
    }

    @Test
    void testDoPost_DatabaseUpdateFailure_ShowsError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("123");
        
        setupValidUpdateParameters();
        when(reservationDAO.getReservationById(123)).thenReturn(testReservation);
        when(reservationDAO.updateReservation(any(Reservation.class))).thenReturn(false);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(reservationDAO).updateReservation(any(Reservation.class));
        verify(request).setAttribute("error", "Failed to update reservation");
        verify(request).setAttribute("reservation", testReservation);
        verify(request).getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_ReservationUpdatedCorrectly() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("123");
        
        setupValidUpdateParameters();
        when(request.getParameter("notes")).thenReturn("Notes mises à jour");
        when(reservationDAO.getReservationById(123)).thenReturn(testReservation);
        when(reservationDAO.updateReservation(any(Reservation.class))).thenReturn(true);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(reservationDAO).updateReservation(argThat(reservation -> {
            assertThat(reservation.getGuestName()).isEqualTo("Marie Martin");
            assertThat(reservation.getGuestEmail()).isEqualTo("marie.martin@example.com");
            assertThat(reservation.getGuestPhone()).isEqualTo("0987654321");
            assertThat(reservation.getRoomNumber()).isEqualTo(201);
            assertThat(reservation.getCheckInDate()).isEqualTo(Date.valueOf("2024-02-15"));
            assertThat(reservation.getCheckOutDate()).isEqualTo(Date.valueOf("2024-02-18"));
            assertThat(reservation.getStatus()).isEqualTo("en attente");
            assertThat(reservation.getNotes()).isEqualTo("Notes mises à jour");
            return true;
        }));
    }

    @Test
    void testDoPost_NullNotes_SetsEmptyString() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("123");
        
        setupValidUpdateParameters();
        when(request.getParameter("notes")).thenReturn(null);
        when(reservationDAO.getReservationById(123)).thenReturn(testReservation);
        when(reservationDAO.updateReservation(any(Reservation.class))).thenReturn(true);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(reservationDAO).updateReservation(argThat(reservation -> {
            assertThat(reservation.getNotes()).isEqualTo("");
            return true;
        }));
    }

    // ======================== Helper Methods ========================

    private void setupValidUpdateParameters() {
        when(request.getParameter("guestName")).thenReturn("Marie Martin");
        when(request.getParameter("guestEmail")).thenReturn("marie.martin@example.com");
        when(request.getParameter("guestPhone")).thenReturn("0987654321");
        when(request.getParameter("roomNumber")).thenReturn("201");
        when(request.getParameter("checkInDate")).thenReturn("2024-02-15");
        when(request.getParameter("checkOutDate")).thenReturn("2024-02-18");
        when(request.getParameter("status")).thenReturn("en attente");
    }
}
