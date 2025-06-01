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
 * Unit tests for AddReservationServlet
 */
@ExtendWith(MockitoExtension.class)
class AddReservationServletTest {

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
    
    private AddReservationServlet servlet;
    private Employee receptionnisteEmployee;
    private Employee adminEmployee;

    @BeforeEach
    void setUp() throws ServletException {
        servlet = new AddReservationServlet();
        
        // Create test employees
        receptionnisteEmployee = new Employee(1, "receptionniste1", "password", null, "réceptionniste");
        adminEmployee = new Employee(2, "admin1", "password", null, "admin");
        
        // Mock request dispatcher
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
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
        verify(response).sendRedirect("dashboard?error=Accès refusé. Seuls les réceptionnistes peuvent ajouter des réservations.");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoGet_ValidReceptionniste_ForwardsToAddReservationPage() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(request).getRequestDispatcher("/WEB-INF/views/add-reservation.jsp");
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
    void testDoPost_NoEmployeeInSession_RedirectsToLogin() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(null);
        
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
        verify(response).sendRedirect("dashboard?error=Accès refusé. Seuls les réceptionnistes peuvent ajouter des réservations.");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoPost_MissingRequiredFields_ShowsError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        
        // Missing guestName
        when(request.getParameter("guestName")).thenReturn("");
        when(request.getParameter("guestEmail")).thenReturn("guest@example.com");
        when(request.getParameter("guestPhone")).thenReturn("123456789");
        when(request.getParameter("roomNumber")).thenReturn("101");
        when(request.getParameter("checkInDate")).thenReturn("2024-01-15");
        when(request.getParameter("checkOutDate")).thenReturn("2024-01-18");
        when(request.getParameter("status")).thenReturn("confirmée");
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(request).setAttribute("error", "Tous les champs sont obligatoires");
        verify(request).getRequestDispatcher("/WEB-INF/views/add-reservation.jsp");
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void testDoPost_InvalidRoomNumber_ShowsError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        
        setupValidRequestParameters();
        when(request.getParameter("roomNumber")).thenReturn("invalid");
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(request).setAttribute("error", "Numéro de chambre invalide");
        verify(request).getRequestDispatcher("/WEB-INF/views/add-reservation.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_InvalidDateFormat_ShowsError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        
        setupValidRequestParameters();
        when(request.getParameter("checkInDate")).thenReturn("invalid-date");
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(request).setAttribute("error", "Format de date invalide");
        verify(request).getRequestDispatcher("/WEB-INF/views/add-reservation.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_CheckOutBeforeCheckIn_ShowsError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        
        setupValidRequestParameters();
        when(request.getParameter("checkInDate")).thenReturn("2024-01-18");
        when(request.getParameter("checkOutDate")).thenReturn("2024-01-15"); // Before check-in
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(request).setAttribute("error", "La date de départ doit être postérieure à la date d'arrivée");
        verify(request).getRequestDispatcher("/WEB-INF/views/add-reservation.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_ValidReservation_Success() throws ServletException, IOException {
        // Arrange
        servlet = spy(servlet);
        
        // Use reflection to set the DAO
        try {
            java.lang.reflect.Field daoField = AddReservationServlet.class.getDeclaredField("reservationDAO");
            daoField.setAccessible(true);
            daoField.set(servlet, reservationDAO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        
        setupValidRequestParameters();
        when(reservationDAO.addReservation(any(Reservation.class))).thenReturn(true);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(reservationDAO).addReservation(any(Reservation.class));
        verify(response).sendRedirect("dashboard");
        verify(request, never()).setAttribute(eq("error"), anyString());
    }

    @Test
    void testDoPost_DatabaseFailure_ShowsError() throws ServletException, IOException {
        // Arrange
        servlet = spy(servlet);
        
        // Use reflection to set the DAO
        try {
            java.lang.reflect.Field daoField = AddReservationServlet.class.getDeclaredField("reservationDAO");
            daoField.setAccessible(true);
            daoField.set(servlet, reservationDAO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        
        setupValidRequestParameters();
        when(reservationDAO.addReservation(any(Reservation.class))).thenReturn(false);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(reservationDAO).addReservation(any(Reservation.class));
        verify(request).setAttribute("error", "Échec de l'ajout de la réservation. Veuillez réessayer.");
        verify(request).getRequestDispatcher("/WEB-INF/views/add-reservation.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_ReservationObjectCreatedCorrectly() throws ServletException, IOException {
        // Arrange
        servlet = spy(servlet);
        
        // Use reflection to set the DAO
        try {
            java.lang.reflect.Field daoField = AddReservationServlet.class.getDeclaredField("reservationDAO");
            daoField.setAccessible(true);
            daoField.set(servlet, reservationDAO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        
        setupValidRequestParameters();
        when(request.getParameter("notes")).thenReturn("Notes spéciales");
        when(reservationDAO.addReservation(any(Reservation.class))).thenReturn(true);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(reservationDAO).addReservation(argThat(reservation -> {
            assertThat(reservation.getGuestName()).isEqualTo("Jean Dupont");
            assertThat(reservation.getGuestEmail()).isEqualTo("jean.dupont@example.com");
            assertThat(reservation.getGuestPhone()).isEqualTo("0123456789");
            assertThat(reservation.getRoomNumber()).isEqualTo(101);
            assertThat(reservation.getCheckInDate()).isEqualTo(Date.valueOf("2024-01-15"));
            assertThat(reservation.getCheckOutDate()).isEqualTo(Date.valueOf("2024-01-18"));
            assertThat(reservation.getStatus()).isEqualTo("confirmée");
            assertThat(reservation.getCreatedBy()).isEqualTo(receptionnisteEmployee);
            assertThat(reservation.getNotes()).isEqualTo("Notes spéciales");
            return true;
        }));
    }

    @Test
    void testDoPost_EmptyNotes_SetsEmptyString() throws ServletException, IOException {
        // Arrange
        servlet = spy(servlet);
        
        // Use reflection to set the DAO
        try {
            java.lang.reflect.Field daoField = AddReservationServlet.class.getDeclaredField("reservationDAO");
            daoField.setAccessible(true);
            daoField.set(servlet, reservationDAO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        
        setupValidRequestParameters();
        when(request.getParameter("notes")).thenReturn(null);
        when(reservationDAO.addReservation(any(Reservation.class))).thenReturn(true);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(reservationDAO).addReservation(argThat(reservation -> {
            assertThat(reservation.getNotes()).isEqualTo("");
            return true;
        }));
    }

    // ======================== Helper Methods ========================

    private void setupValidRequestParameters() {
        when(request.getParameter("guestName")).thenReturn("Jean Dupont");
        when(request.getParameter("guestEmail")).thenReturn("jean.dupont@example.com");
        when(request.getParameter("guestPhone")).thenReturn("0123456789");
        when(request.getParameter("roomNumber")).thenReturn("101");
        when(request.getParameter("checkInDate")).thenReturn("2024-01-15");
        when(request.getParameter("checkOutDate")).thenReturn("2024-01-18");
        when(request.getParameter("status")).thenReturn("confirmée");
    }
}
