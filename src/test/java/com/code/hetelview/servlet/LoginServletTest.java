package com.code.hetelview.servlet;

import com.code.hetelview.dao.EmployeeDAO;
import com.code.hetelview.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour LoginServlet.
 * Utilise Mockito pour simuler les dépendances.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests LoginServlet")
class LoginServletTest {
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private HttpSession session;
    
    @Mock
    private RequestDispatcher requestDispatcher;
    
    @Mock
    private EmployeeDAO employeeDAO;
    
    private LoginServlet loginServlet;
    private Employee testEmployee;
    
    @BeforeEach
    void setUp() {
        loginServlet = new LoginServlet();
        
        // Créer un employé de test
        testEmployee = new Employee();
        testEmployee.setId(1);
        testEmployee.setUsername("receptionniste1");
        testEmployee.setPassword("hashedPassword");
        testEmployee.setFullName("Marie Leclerc");
        testEmployee.setRole("réceptionniste");
        
        // Configuration des mocks de base
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        
        // Use reflection to set the DAO
        try {
            java.lang.reflect.Field daoField = LoginServlet.class.getDeclaredField("employeeDAO");
            daoField.setAccessible(true);
            daoField.set(loginServlet, employeeDAO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test
    @DisplayName("Devrait afficher la page de connexion pour une requête GET")
    void shouldShowLoginPageForGetRequest() throws ServletException, IOException {
        // When
        loginServlet.doGet(request, response);
        
        // Then
        verify(request).getRequestDispatcher("/WEB-INF/views/login.jsp");
        verify(requestDispatcher).forward(request, response);
    }
    
    @Test
    @DisplayName("Devrait rediriger vers le tableau de bord si l'utilisateur est déjà connecté")
    void shouldRedirectToDashboardIfAlreadyLoggedIn() throws ServletException, IOException {
        // Given
        when(session.getAttribute("employee")).thenReturn(testEmployee);
        
        // When
        loginServlet.doGet(request, response);
        
        // Then
        verify(response).sendRedirect("dashboard");
        verify(request, never()).getRequestDispatcher(anyString());
    }
    
    @Test
    @DisplayName("Devrait connecter l'utilisateur avec des identifiants valides")
    void shouldLoginUserWithValidCredentials() throws ServletException, IOException {
        // Given
        when(request.getParameter("username")).thenReturn("receptionniste1");
        when(request.getParameter("password")).thenReturn("password123");
        when(session.getAttribute("employee")).thenReturn(null);
        when(employeeDAO.authenticate("receptionniste1", "password123")).thenReturn(testEmployee);
        
        // When
        loginServlet.doPost(request, response);
        
        // Then
        verify(session).setAttribute("employee", testEmployee);
        verify(response).sendRedirect("dashboard");
        verify(session, never()).setAttribute(eq("error"), anyString());
    }
    
    @Test
    @DisplayName("Devrait rejeter les identifiants invalides")
    void shouldRejectInvalidCredentials() throws ServletException, IOException {
        // Given
        when(request.getParameter("username")).thenReturn("receptionniste1");
        when(request.getParameter("password")).thenReturn("mauvais_mot_de_passe");
        when(session.getAttribute("employee")).thenReturn(null);
        when(employeeDAO.authenticate("receptionniste1", "mauvais_mot_de_passe")).thenReturn(null);
        
        // When
        loginServlet.doPost(request, response);
        
        // Then
        verify(session).setAttribute("error", "Nom d'utilisateur ou mot de passe incorrect");
        verify(response).sendRedirect("login");
        verify(session, never()).setAttribute(eq("employee"), any());
    }
    
    @Test
    @DisplayName("Devrait gérer les paramètres manquants")
    void shouldHandleMissingParameters() throws ServletException, IOException {
        // Given
        when(request.getParameter("username")).thenReturn(null);
        when(request.getParameter("password")).thenReturn("password123");
        when(session.getAttribute("employee")).thenReturn(null);
        
        // When
        loginServlet.doPost(request, response);
        
        // Then
        verify(session).setAttribute("error", "Nom d'utilisateur ou mot de passe incorrect");
        verify(response).sendRedirect("login");
    }
    
    @Test
    @DisplayName("Devrait gérer les paramètres vides")
    void shouldHandleEmptyParameters() throws ServletException, IOException {
        // Given
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("   ");
        when(session.getAttribute("employee")).thenReturn(null);
        
        // When
        loginServlet.doPost(request, response);
        
        // Then
        verify(session).setAttribute("error", "Nom d'utilisateur ou mot de passe incorrect");
        verify(response).sendRedirect("login");
    }
    
    @Test
    @DisplayName("Devrait rediriger vers le tableau de bord si déjà connecté lors du POST")
    void shouldRedirectToDashboardIfAlreadyLoggedInOnPost() throws ServletException, IOException {
        // Given
        when(session.getAttribute("employee")).thenReturn(testEmployee);
        
        // When
        loginServlet.doPost(request, response);
        
        // Then
        verify(response).sendRedirect("dashboard");
        verify(request, never()).getParameter(anyString());
    }
    
    @Test
    @DisplayName("Devrait connecter différents rôles d'employés")
    void shouldLoginDifferentEmployeeRoles() throws ServletException, IOException {
        // Given
        String[] roles = {"admin", "chef de réception", "réceptionniste", "concierge", "voiturier"};
        
        for (String role : roles) {
            // Reset mocks
            reset(session, response);
            when(request.getSession()).thenReturn(session);
            when(session.getAttribute("employee")).thenReturn(null);
            
            Employee employee = new Employee();
            employee.setId(1);
            employee.setUsername("user_" + role);
            employee.setRole(role);
            
            when(request.getParameter("username")).thenReturn("user_" + role);
            when(request.getParameter("password")).thenReturn("password123");
            when(employeeDAO.authenticate("user_" + role, "password123")).thenReturn(employee);
            
            // When
            loginServlet.doPost(request, response);
            
            // Then
            verify(session).setAttribute("employee", employee);
            verify(response).sendRedirect("dashboard");
        }
    }
    
    @Test
    @DisplayName("Devrait gérer les erreurs de base de données gracieusement")
    void shouldHandleDatabaseErrorsGracefully() throws ServletException, IOException {
        // Given
        when(request.getParameter("username")).thenReturn("receptionniste1");
        when(request.getParameter("password")).thenReturn("password123");
        when(session.getAttribute("employee")).thenReturn(null);
        when(employeeDAO.authenticate("receptionniste1", "password123")).thenThrow(new RuntimeException("Erreur de base de données"));
        
        // When
        loginServlet.doPost(request, response);
        
        // Then
        verify(session).setAttribute("error", "Nom d'utilisateur ou mot de passe incorrect");
        verify(response).sendRedirect("login");
    }
    
    @Test
    @DisplayName("Devrait nettoyer les espaces dans les paramètres")
    void shouldTrimParameterSpaces() throws ServletException, IOException {
        // Given
        when(request.getParameter("username")).thenReturn("  receptionniste1  ");
        when(request.getParameter("password")).thenReturn("  password123  ");
        when(session.getAttribute("employee")).thenReturn(null);
        when(employeeDAO.authenticate("receptionniste1", "password123")).thenReturn(testEmployee);
        
        // When
        loginServlet.doPost(request, response);
        
        // Then
        verify(employeeDAO).authenticate("receptionniste1", "password123");
        verify(session).setAttribute("employee", testEmployee);
        verify(response).sendRedirect("dashboard");
    }
}
