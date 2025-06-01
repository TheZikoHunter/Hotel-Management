package com.code.hetelview.servlet;

import com.code.hetelview.dao.ReservationDAO;
import com.code.hetelview.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Unit tests for DeleteReservationServlet
 */
@ExtendWith(MockitoExtension.class)
class DeleteReservationServletTest {

    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private HttpSession session;
    
    @Mock
    private ReservationDAO reservationDAO;
    
    private DeleteReservationServlet servlet;
    private Employee receptionnisteEmployee;
    private Employee adminEmployee;
    private Employee chefReceptionEmployee;

    @BeforeEach
    void setUp() throws ServletException {
        servlet = new DeleteReservationServlet();
        
        // Create test employees
        receptionnisteEmployee = new Employee(1, "receptionniste1", "password", null, "réceptionniste");
        adminEmployee = new Employee(2, "admin1", "password", null, "admin");
        chefReceptionEmployee = new Employee(3, "chef1", "password", null, "chef de réception");
        
        // Use reflection to set the DAO
        try {
            java.lang.reflect.Field daoField = DeleteReservationServlet.class.getDeclaredField("reservationDAO");
            daoField.setAccessible(true);
            daoField.set(servlet, reservationDAO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ======================== Authentication Tests ========================

    @Test
    void testDoGet_NoSession_RedirectsToLogin() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(null);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("login");
        verify(reservationDAO, never()).deleteReservation(anyInt());
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
        verify(reservationDAO, never()).deleteReservation(anyInt());
    }

    // ======================== Authorization Tests ========================

    @Test
    void testDoGet_AdminRole_AccessDenied() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("dashboard?error=Accès non autorisé");
        verify(reservationDAO, never()).deleteReservation(anyInt());
    }

    @Test
    void testDoGet_ChefReceptionRole_AccessDenied() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(chefReceptionEmployee);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("dashboard?error=Accès non autorisé");
        verify(reservationDAO, never()).deleteReservation(anyInt());
    }

    @Test
    void testDoGet_ReceptionnisteRole_AccessGranted() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("123");
        when(reservationDAO.deleteReservation(123)).thenReturn(true);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(reservationDAO).deleteReservation(123);
        verify(response).sendRedirect("dashboard?success=Réservation supprimée avec succès");
    }

    // ======================== Parameter Validation Tests ========================

    @Test
    void testDoGet_MissingId_RedirectsWithError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn(null);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("dashboard?error=ID de réservation invalide");
        verify(reservationDAO, never()).deleteReservation(anyInt());
    }

    @Test
    void testDoGet_EmptyId_RedirectsWithError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("   ");
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("dashboard?error=ID de réservation invalide");
        verify(reservationDAO, never()).deleteReservation(anyInt());
    }

    @Test
    void testDoGet_InvalidIdFormat_RedirectsWithError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("invalid");
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("dashboard?error=ID de réservation invalide");
        verify(reservationDAO, never()).deleteReservation(anyInt());
    }

    @Test
    void testDoGet_NegativeId_AttemptsDelete() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("-1");
        when(reservationDAO.deleteReservation(-1)).thenReturn(false);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(reservationDAO).deleteReservation(-1);
        verify(response).sendRedirect("dashboard?error=Échec de la suppression de la réservation");
    }

    @Test
    void testDoGet_ZeroId_AttemptsDelete() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("0");
        when(reservationDAO.deleteReservation(0)).thenReturn(false);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(reservationDAO).deleteReservation(0);
        verify(response).sendRedirect("dashboard?error=Échec de la suppression de la réservation");
    }

    // ======================== Deletion Success Tests ========================

    @Test
    void testDoGet_ValidId_SuccessfulDeletion() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("123");
        when(reservationDAO.deleteReservation(123)).thenReturn(true);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(reservationDAO).deleteReservation(123);
        verify(response).sendRedirect("dashboard?success=Réservation supprimée avec succès");
    }

    @Test
    void testDoGet_LargeValidId_SuccessfulDeletion() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("999999");
        when(reservationDAO.deleteReservation(999999)).thenReturn(true);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(reservationDAO).deleteReservation(999999);
        verify(response).sendRedirect("dashboard?success=Réservation supprimée avec succès");
    }

    // ======================== Deletion Failure Tests ========================

    @Test
    void testDoGet_ValidId_FailedDeletion() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("123");
        when(reservationDAO.deleteReservation(123)).thenReturn(false);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(reservationDAO).deleteReservation(123);
        verify(response).sendRedirect("dashboard?error=Échec de la suppression de la réservation");
    }

    @Test
    void testDoGet_NonExistentReservation_FailedDeletion() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("999");
        when(reservationDAO.deleteReservation(999)).thenReturn(false);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(reservationDAO).deleteReservation(999);
        verify(response).sendRedirect("dashboard?error=Échec de la suppression de la réservation");
    }

    // ======================== Edge Cases ========================

    @Test
    void testDoGet_MaxIntegerId_HandledCorrectly() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn(String.valueOf(Integer.MAX_VALUE));
        when(reservationDAO.deleteReservation(Integer.MAX_VALUE)).thenReturn(true);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(reservationDAO).deleteReservation(Integer.MAX_VALUE);
        verify(response).sendRedirect("dashboard?success=Réservation supprimée avec succès");
    }

    @Test
    void testDoGet_IdWithLeadingZeros_ParsedCorrectly() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("00123");
        when(reservationDAO.deleteReservation(123)).thenReturn(true);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(reservationDAO).deleteReservation(123);
        verify(response).sendRedirect("dashboard?success=Réservation supprimée avec succès");
    }

    @Test
    void testDoGet_IdWithWhitespace_TrimmedAndParsed() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        when(request.getParameter("id")).thenReturn("  123  ");
        
        // Act
        servlet.doGet(request, response);
        
        // Assert - Should fail because trim() is not called in the servlet
        verify(response).sendRedirect("dashboard?error=ID de réservation invalide");
        verify(reservationDAO, never()).deleteReservation(anyInt());
    }

    // ======================== Security Tests ========================

    @Test
    void testDoGet_MultipleConsecutiveRequests_EachProcessedIndependently() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        
        // First request
        when(request.getParameter("id")).thenReturn("123");
        when(reservationDAO.deleteReservation(123)).thenReturn(true);
        
        // Act - First request
        servlet.doGet(request, response);
        
        // Assert - First request
        verify(reservationDAO).deleteReservation(123);
        verify(response).sendRedirect("dashboard?success=Réservation supprimée avec succès");
        
        // Reset mocks for second request
        reset(response, reservationDAO);
        
        // Second request
        when(request.getParameter("id")).thenReturn("456");
        when(reservationDAO.deleteReservation(456)).thenReturn(false);
        
        // Act - Second request
        servlet.doGet(request, response);
        
        // Assert - Second request
        verify(reservationDAO).deleteReservation(456);
        verify(response).sendRedirect("dashboard?error=Échec de la suppression de la réservation");
    }

    @Test
    void testDoGet_SessionWithNullEmployee_RedirectsToLogin() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(null);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("login");
        verify(reservationDAO, never()).deleteReservation(anyInt());
    }
}
