package com.code.hetelview.servlet;

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
 * Unit tests for LogoutServlet
 */
@ExtendWith(MockitoExtension.class)
class LogoutServletTest {

    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private HttpSession session;
    
    private LogoutServlet servlet;
    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        servlet = new LogoutServlet();
        
        // Create test employee
        testEmployee = new Employee(1, "testuser", "password", "Test User", "réceptionniste");
    }

    // ======================== Basic Logout Tests ========================

    @Test
    void testDoGet_ValidSession_InvalidatesSessionAndRedirects() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(session).invalidate();
        verify(response).sendRedirect("login");
    }

    @Test
    void testDoGet_NoSession_RedirectsToLogin() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(null);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("login");
        // Verify no attempt to invalidate null session
        verify(session, never()).invalidate();
    }

    @Test
    void testDoGet_SessionWithEmployee_InvalidatesSessionAndRedirects() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(testEmployee);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(session).invalidate();
        verify(response).sendRedirect("login");
    }

    @Test
    void testDoGet_EmptySession_InvalidatesSessionAndRedirects() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(null);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(session).invalidate();
        verify(response).sendRedirect("login");
    }

    // ======================== Edge Cases ========================

    @Test
    void testDoGet_SessionInvalidationThrowsException_StillRedirects() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        doThrow(new IllegalStateException("Session already invalidated")).when(session).invalidate();
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(session).invalidate();
        verify(response).sendRedirect("login");
    }

    @Test
    void testDoGet_MultipleLogoutRequests_EachHandledIndependently() throws ServletException, IOException {
        // First logout request
        when(request.getSession(false)).thenReturn(session);
        
        servlet.doGet(request, response);
        
        verify(session).invalidate();
        verify(response).sendRedirect("login");
        
        // Reset mocks for second request
        reset(request, response, session);
        
        // Second logout request (no session)
        when(request.getSession(false)).thenReturn(null);
        
        servlet.doGet(request, response);
        
        verify(response).sendRedirect("login");
        verify(session, never()).invalidate();
    }

    // ======================== Security Tests ========================

    @Test
    void testDoGet_SessionWithSensitiveData_ProperlyInvalidated() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        
        // Simulate session with sensitive data
        when(session.getAttribute("employee")).thenReturn(testEmployee);
        when(session.getAttribute("lastLoginTime")).thenReturn(System.currentTimeMillis());
        when(session.getAttribute("permissions")).thenReturn("admin");
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(session).invalidate(); // This should clear all session data
        verify(response).sendRedirect("login");
    }

    @Test
    void testDoGet_ConcurrentLogoutRequests_HandledSafely() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        
        // Simulate concurrent access where session might be invalidated by another request
        doThrow(new IllegalStateException("Session already invalidated"))
            .when(session).invalidate();
        
        // Act - Should not throw exception
        servlet.doGet(request, response);
        
        // Assert
        verify(session).invalidate();
        verify(response).sendRedirect("login");
    }

    // ======================== Different Session States ========================

    @Test
    void testDoGet_NewSession_InvalidatesAndRedirects() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.isNew()).thenReturn(true);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(session).invalidate();
        verify(response).sendRedirect("login");
    }

    @Test
    void testDoGet_ExpiredSession_StillAttempsInvalidation() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        
        // Simulate expired session
        doThrow(new IllegalStateException("Session expired"))
            .when(session).invalidate();
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(session).invalidate();
        verify(response).sendRedirect("login");
    }

    // ======================== Response Verification ========================

    @Test
    void testDoGet_RedirectionUrl_IsCorrect() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("login");
        // Verify exactly the "login" string is used
        verify(response, never()).sendRedirect("login.jsp");
        verify(response, never()).sendRedirect("/login");
        verify(response, never()).sendRedirect("./login");
    }

    @Test
    void testDoGet_NoAdditionalResponseOperations() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("login");
        // Verify no other response operations are performed
        verify(response, never()).setStatus(anyInt());
        verify(response, never()).setContentType(anyString());
        verify(response, never()).getWriter();
        verify(response, never()).getOutputStream();
    }

    // ======================== Session Management Verification ========================

    @Test
    void testDoGet_SessionOperations_InCorrectOrder() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert - Verify operations happen in the correct order
        var inOrder = inOrder(request, session, response);
        inOrder.verify(request).getSession(false);
        inOrder.verify(session).invalidate();
        inOrder.verify(response).sendRedirect("login");
    }

    @Test
    void testDoGet_SessionCreation_NotAttempted() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(null);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(request).getSession(false); // Only getSession(false) should be called
        verify(request, never()).getSession(); // getSession() or getSession(true) should not be called
        verify(request, never()).getSession(true);
        verify(response).sendRedirect("login");
    }

    // ======================== Logout Flow Completeness ========================

    @Test
    void testDoGet_CompleteLogoutFlow_AllStepsExecuted() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(testEmployee);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert - Verify complete logout flow
        verify(request).getSession(false); // 1. Get existing session
        verify(session).invalidate();      // 2. Invalidate session
        verify(response).sendRedirect("login"); // 3. Redirect to login
        
        // Verify no session creation or other operations
        verify(request, never()).getSession(true);
        verifyNoMoreInteractions(request, session, response);
    }
}
