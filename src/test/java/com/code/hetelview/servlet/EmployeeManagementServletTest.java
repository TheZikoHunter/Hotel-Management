package com.code.hetelview.servlet;

import com.code.hetelview.dao.EmployeeDAO;
import com.code.hetelview.model.Employee;
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
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EmployeeManagementServlet
 */
@ExtendWith(MockitoExtension.class)
class EmployeeManagementServletTest {

    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private HttpSession session;
    
    @Mock
    private RequestDispatcher dispatcher;
    
    @Mock
    private EmployeeDAO employeeDAO;
    
    private EmployeeManagementServlet servlet;
    private Employee adminEmployee;
    private Employee chefReceptionEmployee;
    private Employee receptionnisteEmployee;

    @BeforeEach
    void setUp() throws ServletException {
        servlet = new EmployeeManagementServlet();
        
        // Create test employees
        adminEmployee = new Employee(1, "admin1", "password", "Admin User", "admin");
        
        chefReceptionEmployee = new Employee(2, "chef1", "password", "Chef Reception", "chef de réception");
        
        receptionnisteEmployee = new Employee(3, "receptionniste1", "password", "Receptionniste User", "réceptionniste");
        
        // Mock request dispatcher
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        
        // Use reflection to set the DAO
        try {
            java.lang.reflect.Field daoField = EmployeeManagementServlet.class.getDeclaredField("employeeDAO");
            daoField.setAccessible(true);
            daoField.set(servlet, employeeDAO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ======================== Authentication & Authorization Tests ========================

    @Test
    void testDoGet_NoSession_RedirectsToLogin() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(null);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("login");
        verify(employeeDAO, never()).getAllEmployees();
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
        verify(employeeDAO, never()).getAllEmployees();
    }

    @Test
    void testDoGet_ReceptionnisteRole_AccessDenied() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(receptionnisteEmployee);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé. Privilèges administrateur ou chef de réception requis.");
        verify(employeeDAO, never()).getAllEmployees();
    }

    @Test
    void testDoGet_AdminRole_AccessGranted() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        when(request.getParameter("action")).thenReturn(null);
        when(employeeDAO.getAllEmployees()).thenReturn(Arrays.asList(adminEmployee, chefReceptionEmployee));
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(employeeDAO).getAllEmployees();
        verify(request).setAttribute(eq("employees"), any(List.class));
        verify(request).getRequestDispatcher("/WEB-INF/views/employee-management.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_ChefReceptionRole_AccessGranted() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(chefReceptionEmployee);
        when(request.getParameter("action")).thenReturn(null);
        when(employeeDAO.getAllEmployees()).thenReturn(Arrays.asList(adminEmployee, chefReceptionEmployee));
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(employeeDAO).getAllEmployees();
        verify(request).setAttribute(eq("employees"), any(List.class));
        verify(request).getRequestDispatcher("/WEB-INF/views/employee-management.jsp");
        verify(dispatcher).forward(request, response);
    }

    // ======================== GET Action Tests ========================

    @Test
    void testDoGet_NoAction_ListsEmployees() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        when(request.getParameter("action")).thenReturn(null);
        
        List<Employee> employees = Arrays.asList(adminEmployee, chefReceptionEmployee, receptionnisteEmployee);
        when(employeeDAO.getAllEmployees()).thenReturn(employees);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(employeeDAO).getAllEmployees();
        verify(request).setAttribute("employees", employees);
        verify(request).getRequestDispatcher("/WEB-INF/views/employee-management.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_AddAction_ShowsAddForm() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        when(request.getParameter("action")).thenReturn("add");
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(request).getRequestDispatcher("/WEB-INF/views/add-employee.jsp");
        verify(dispatcher).forward(request, response);
        verify(employeeDAO, never()).getAllEmployees();
    }

    @Test
    void testDoGet_EditAction_ValidId_ShowsEditForm() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("id")).thenReturn("3");
        when(employeeDAO.getEmployeeById(3)).thenReturn(receptionnisteEmployee);
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(employeeDAO).getEmployeeById(3);
        verify(request).setAttribute("employee", receptionnisteEmployee);
        verify(request).getRequestDispatcher("/WEB-INF/views/edit-employee.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_EditAction_InvalidId_RedirectsToManagement() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("id")).thenReturn("invalid");
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(response).sendRedirect("employee-management");
        verify(employeeDAO, never()).getEmployeeById(anyInt());
    }

    @Test
    void testDoGet_EditAction_EmployeeNotFound_ShowsError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("id")).thenReturn("999");
        when(employeeDAO.getEmployeeById(999)).thenReturn(null);
        when(employeeDAO.getAllEmployees()).thenReturn(Arrays.asList(adminEmployee));
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(employeeDAO).getEmployeeById(999);
        verify(request).setAttribute("error", "Employé introuvable");
        verify(employeeDAO).getAllEmployees();
        verify(request).getRequestDispatcher("/WEB-INF/views/employee-management.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_DeleteAction_ValidId_DeletesEmployee() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("3");
        when(employeeDAO.getEmployeeById(3)).thenReturn(receptionnisteEmployee);
        when(employeeDAO.deleteEmployee(3)).thenReturn(true);
        when(employeeDAO.getAllEmployees()).thenReturn(Arrays.asList(adminEmployee, chefReceptionEmployee));
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(employeeDAO).getEmployeeById(3);
        verify(employeeDAO).deleteEmployee(3);
        verify(request).setAttribute("success", "Employé supprimé avec succès");
        verify(employeeDAO).getAllEmployees();
        verify(request).getRequestDispatcher("/WEB-INF/views/employee-management.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_DeleteAction_CannotDeleteSelf() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("1"); // Admin's own ID
        when(employeeDAO.getEmployeeById(1)).thenReturn(adminEmployee);
        when(employeeDAO.getAllEmployees()).thenReturn(Arrays.asList(adminEmployee, chefReceptionEmployee));
        
        // Act
        servlet.doGet(request, response);
        
        // Assert
        verify(employeeDAO).getEmployeeById(1);
        verify(request).setAttribute("error", "Impossible de supprimer votre propre compte");
        verify(employeeDAO, never()).deleteEmployee(anyInt());
        verify(employeeDAO).getAllEmployees();
        verify(request).getRequestDispatcher("/WEB-INF/views/employee-management.jsp");
        verify(dispatcher).forward(request, response);
    }

    // ======================== POST - Add Employee Tests ========================

    @Test
    void testDoPost_AddEmployee_ValidData_Success() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("username")).thenReturn("newuser");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("fullName")).thenReturn("New User");
        when(request.getParameter("role")).thenReturn("réceptionniste");
        
        when(employeeDAO.getEmployeeByUsername("newuser")).thenReturn(null);
        when(employeeDAO.addEmployee(any(Employee.class))).thenReturn(true);
        when(employeeDAO.getAllEmployees()).thenReturn(Arrays.asList(adminEmployee));
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(employeeDAO).getEmployeeByUsername("newuser");
        verify(employeeDAO).addEmployee(any(Employee.class));
        verify(request).setAttribute("success", "Employé ajouté avec succès");
        verify(employeeDAO).getAllEmployees();
        verify(request).getRequestDispatcher("/WEB-INF/views/employee-management.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_AddEmployee_MissingFields_ShowsError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("fullName")).thenReturn("New User");
        when(request.getParameter("role")).thenReturn("réceptionniste");
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(request).setAttribute("error", "Tous les champs sont obligatoires");
        verify(request).getRequestDispatcher("/WEB-INF/views/add-employee.jsp");
        verify(dispatcher).forward(request, response);
        verify(employeeDAO, never()).addEmployee(any(Employee.class));
    }

    @Test
    void testDoPost_AddEmployee_UnauthorizedRole_ShowsError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(chefReceptionEmployee);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("username")).thenReturn("newuser");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("fullName")).thenReturn("New User");
        when(request.getParameter("role")).thenReturn("admin"); // Chef can't create admin
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(request).setAttribute("error", "Vous n'avez pas l'autorisation de créer un employé avec ce rôle");
        verify(request).getRequestDispatcher("/WEB-INF/views/add-employee.jsp");
        verify(dispatcher).forward(request, response);
        verify(employeeDAO, never()).addEmployee(any(Employee.class));
    }

    @Test
    void testDoPost_AddEmployee_DuplicateUsername_ShowsError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("username")).thenReturn("admin1");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("fullName")).thenReturn("New User");
        when(request.getParameter("role")).thenReturn("réceptionniste");
        
        when(employeeDAO.getEmployeeByUsername("admin1")).thenReturn(adminEmployee);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(employeeDAO).getEmployeeByUsername("admin1");
        verify(request).setAttribute("error", "Nom d'utilisateur déjà existant");
        verify(request).getRequestDispatcher("/WEB-INF/views/add-employee.jsp");
        verify(dispatcher).forward(request, response);
        verify(employeeDAO, never()).addEmployee(any(Employee.class));
    }

    // ======================== POST - Update Employee Tests ========================

    @Test
    void testDoPost_UpdateEmployee_ValidData_Success() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("id")).thenReturn("3");
        when(request.getParameter("username")).thenReturn("updateduser");
        when(request.getParameter("password")).thenReturn("newpassword");
        when(request.getParameter("fullName")).thenReturn("Updated User");
        when(request.getParameter("role")).thenReturn("réceptionniste");
        
        when(employeeDAO.getEmployeeById(3)).thenReturn(receptionnisteEmployee);
        when(employeeDAO.getEmployeeByUsername("updateduser")).thenReturn(null);
        when(employeeDAO.updateEmployee(any(Employee.class))).thenReturn(true);
        when(employeeDAO.getAllEmployees()).thenReturn(Arrays.asList(adminEmployee));
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(employeeDAO).getEmployeeById(3);
        verify(employeeDAO).getEmployeeByUsername("updateduser");
        verify(employeeDAO).updateEmployee(any(Employee.class));
        verify(request).setAttribute("success", "Employé mis à jour avec succès");
        verify(employeeDAO).getAllEmployees();
        verify(request).getRequestDispatcher("/WEB-INF/views/employee-management.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_UpdateEmployee_MissingFields_ShowsError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("id")).thenReturn("3");
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("newpassword");
        when(request.getParameter("fullName")).thenReturn("Updated User");
        when(request.getParameter("role")).thenReturn("réceptionniste");
        
        when(employeeDAO.getEmployeeById(3)).thenReturn(receptionnisteEmployee);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(employeeDAO).getEmployeeById(3);
        verify(request).setAttribute("error", "Nom d'utilisateur, nom complet et rôle sont obligatoires");
        verify(request).setAttribute("employee", receptionnisteEmployee);
        verify(request).getRequestDispatcher("/WEB-INF/views/edit-employee.jsp");
        verify(dispatcher).forward(request, response);
        verify(employeeDAO, never()).updateEmployee(any(Employee.class));
    }

    @Test
    void testDoPost_UpdateEmployee_UnauthorizedRole_ShowsError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(chefReceptionEmployee);
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("id")).thenReturn("1"); // Admin employee
        when(request.getParameter("username")).thenReturn("admin1");
        when(request.getParameter("password")).thenReturn("newpassword");
        when(request.getParameter("fullName")).thenReturn("Admin User");
        when(request.getParameter("role")).thenReturn("admin");
        
        when(employeeDAO.getEmployeeById(1)).thenReturn(adminEmployee);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(employeeDAO).getEmployeeById(1);
        verify(request).setAttribute("error", "Vous n'avez pas l'autorisation de modifier cet employé ou ce rôle");
        verify(request).setAttribute("employee", adminEmployee);
        verify(request).getRequestDispatcher("/WEB-INF/views/edit-employee.jsp");
        verify(dispatcher).forward(request, response);
        verify(employeeDAO, never()).updateEmployee(any(Employee.class));
    }

    // ======================== Role Management Permission Tests ========================

    @Test
    void testCanManageRole_AdminCanManageAllRoles() throws ServletException, IOException {
        // Test that admin can manage all roles through successful operations
        String[] roles = {"admin", "chef de réception", "réceptionniste", "standardiste", "voiturier", "concierge"};
        
        for (String role : roles) {
            // Reset mocks
            reset(request, response, session, dispatcher, employeeDAO);
            when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
            
            // Arrange
            when(request.getSession(false)).thenReturn(session);
            when(session.getAttribute("employee")).thenReturn(adminEmployee);
            when(request.getParameter("action")).thenReturn("add");
            when(request.getParameter("username")).thenReturn("testuser" + role);
            when(request.getParameter("password")).thenReturn("password123");
            when(request.getParameter("fullName")).thenReturn("Test User");
            when(request.getParameter("role")).thenReturn(role);
            
            when(employeeDAO.getEmployeeByUsername("testuser" + role)).thenReturn(null);
            when(employeeDAO.addEmployee(any(Employee.class))).thenReturn(true);
            when(employeeDAO.getAllEmployees()).thenReturn(Arrays.asList(adminEmployee));
            
            // Act
            servlet.doPost(request, response);
            
            // Assert - Should succeed for admin
            verify(employeeDAO).addEmployee(any(Employee.class));
            verify(request).setAttribute("success", "Employé ajouté avec succès");
        }
    }

    @Test
    void testCanManageRole_ChefReceptionCannotManageAdmin() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(chefReceptionEmployee);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("username")).thenReturn("newadmin");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("fullName")).thenReturn("New Admin");
        when(request.getParameter("role")).thenReturn("admin");
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(request).setAttribute("error", "Vous n'avez pas l'autorisation de créer un employé avec ce rôle");
        verify(request).getRequestDispatcher("/WEB-INF/views/add-employee.jsp");
        verify(dispatcher).forward(request, response);
        verify(employeeDAO, never()).addEmployee(any(Employee.class));
    }

    // ======================== Edge Cases and Error Handling ========================

    @Test
    void testDoPost_NoAction_ListsEmployees() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        when(request.getParameter("action")).thenReturn(null);
        when(employeeDAO.getAllEmployees()).thenReturn(Arrays.asList(adminEmployee));
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(employeeDAO).getAllEmployees();
        verify(request).setAttribute(eq("employees"), any(List.class));
        verify(request).getRequestDispatcher("/WEB-INF/views/employee-management.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_UnknownAction_ListsEmployees() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        when(request.getParameter("action")).thenReturn("unknown");
        when(employeeDAO.getAllEmployees()).thenReturn(Arrays.asList(adminEmployee));
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(employeeDAO).getAllEmployees();
        verify(request).setAttribute(eq("employees"), any(List.class));
        verify(request).getRequestDispatcher("/WEB-INF/views/employee-management.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_DatabaseFailure_ShowsError() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("employee")).thenReturn(adminEmployee);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("username")).thenReturn("newuser");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("fullName")).thenReturn("New User");
        when(request.getParameter("role")).thenReturn("réceptionniste");
        
        when(employeeDAO.getEmployeeByUsername("newuser")).thenReturn(null);
        when(employeeDAO.addEmployee(any(Employee.class))).thenReturn(false);
        
        // Act
        servlet.doPost(request, response);
        
        // Assert
        verify(employeeDAO).addEmployee(any(Employee.class));
        verify(request).setAttribute("error", "Échec de l'ajout de l'employé");
        verify(request).getRequestDispatcher("/WEB-INF/views/add-employee.jsp");
        verify(dispatcher).forward(request, response);
    }
}
