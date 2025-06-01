package com.code.hetelview.servlet;

import com.code.hetelview.dao.EmployeeDAO;
import com.code.hetelview.model.Employee;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for handling employee management operations (CRUD).
 * Only accessible by admin users.
 */
@WebServlet(name = "employeeManagementServlet", value = "/employee-management")
public class EmployeeManagementServlet extends HttpServlet {

    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Check if the current user is an admin.
     */
    private boolean isAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("employee") == null) {
            response.sendRedirect("login");
            return false;
        }
        
        Employee currentEmployee = (Employee) session.getAttribute("employee");
        if (!"admin".equalsIgnoreCase(currentEmployee.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé. Privilèges administrateur requis.");
            return false;
        }
        
        return true;
    }

    /**
     * Handles GET requests to display employee management page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        
        if (action == null) {
            // Default action: list all employees
            listEmployees(request, response);
        } else {
            switch (action) {
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteEmployee(request, response);
                    break;
                default:
                    listEmployees(request, response);
                    break;
            }
        }
    }

    /**
     * Handles POST requests for creating and updating employees.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            addEmployee(request, response);
        } else if ("edit".equals(action)) {
            updateEmployee(request, response);
        } else if ("delete".equals(action)) {
            deleteEmployee(request, response);
        } else {
            listEmployees(request, response);
        }
    }

    /**
     * List all employees.
     */
    private void listEmployees(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Employee> employees = employeeDAO.getAllEmployees();
        request.setAttribute("employees", employees);
        request.getRequestDispatcher("/WEB-INF/views/employee-management.jsp").forward(request, response);
    }

    /**
     * Show add employee form.
     */
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/add-employee.jsp").forward(request, response);
    }

    /**
     * Show edit employee form.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("employee-management");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            Employee employee = employeeDAO.getEmployeeById(id);
            
            if (employee == null) {
                request.setAttribute("error", "Employé introuvable");
                listEmployees(request, response);
                return;
            }
            
            request.setAttribute("employee", employee);
            request.getRequestDispatcher("/WEB-INF/views/edit-employee.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("employee-management");
        }
    }

    /**
     * Add a new employee.
     */
    private void addEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String role = request.getParameter("role");

        // Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            fullName == null || fullName.trim().isEmpty() ||
            role == null || role.trim().isEmpty()) {
            
            request.setAttribute("error", "Tous les champs sont obligatoires");
            request.getRequestDispatcher("/WEB-INF/views/add-employee.jsp").forward(request, response);
            return;
        }

        // Check if username already exists
        if (employeeDAO.getEmployeeByUsername(username) != null) {
            request.setAttribute("error", "Nom d'utilisateur déjà existant");
            request.getRequestDispatcher("/WEB-INF/views/add-employee.jsp").forward(request, response);
            return;
        }

        Employee employee = new Employee();
        employee.setUsername(username.trim());
        employee.setPassword(password);
        employee.setFullName(fullName.trim());
        employee.setRole(role.trim());

        if (employeeDAO.addEmployee(employee)) {
            request.setAttribute("success", "Employé ajouté avec succès");
            listEmployees(request, response);
        } else {
            request.setAttribute("error", "Échec de l'ajout de l'employé");
            request.getRequestDispatcher("/WEB-INF/views/add-employee.jsp").forward(request, response);
        }
    }

    /**
     * Update an existing employee.
     */
    private void updateEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String role = request.getParameter("role");

        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("employee-management");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            
            // Validate input
            if (username == null || username.trim().isEmpty() ||
                fullName == null || fullName.trim().isEmpty() ||
                role == null || role.trim().isEmpty()) {
                
                request.setAttribute("error", "Nom d'utilisateur, nom complet et rôle sont obligatoires");
                Employee employee = employeeDAO.getEmployeeById(id);
                request.setAttribute("employee", employee);
                request.getRequestDispatcher("/WEB-INF/views/edit-employee.jsp").forward(request, response);
                return;
            }

            Employee employee = employeeDAO.getEmployeeById(id);
            if (employee == null) {
                request.setAttribute("error", "Employé introuvable");
                listEmployees(request, response);
                return;
            }

            // Check if username is being changed and if it already exists
            if (!employee.getUsername().equals(username.trim()) && 
                employeeDAO.getEmployeeByUsername(username.trim()) != null) {
                request.setAttribute("error", "Nom d'utilisateur déjà existant");
                request.setAttribute("employee", employee);
                request.getRequestDispatcher("/WEB-INF/views/edit-employee.jsp").forward(request, response);
                return;
            }

            // Create a new employee object for the update
            Employee updatedEmployee = new Employee();
            updatedEmployee.setId(id);
            updatedEmployee.setUsername(username.trim());
            updatedEmployee.setFullName(fullName.trim());
            updatedEmployee.setRole(role.trim());
            
            // Only set password if provided (DAO will handle hashing)
            if (password != null && !password.trim().isEmpty()) {
                updatedEmployee.setPassword(password.trim());
            }

            if (employeeDAO.updateEmployee(updatedEmployee)) {
                request.setAttribute("success", "Employé mis à jour avec succès");
                listEmployees(request, response);
            } else {
                request.setAttribute("error", "Échec de la mise à jour de l'employé");
                request.setAttribute("employee", employee);
                request.getRequestDispatcher("/WEB-INF/views/edit-employee.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("employee-management");
        }
    }

    /**
     * Delete an employee.
     */
    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("employee-management");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            
            // Prevent deleting the current user
            HttpSession session = request.getSession();
            Employee currentEmployee = (Employee) session.getAttribute("employee");
            if (currentEmployee.getId() == id) {
                request.setAttribute("error", "Impossible de supprimer votre propre compte");
                listEmployees(request, response);
                return;
            }
            
            if (employeeDAO.deleteEmployee(id)) {
                request.setAttribute("success", "Employé supprimé avec succès");
            } else {
                request.setAttribute("error", "Échec de la suppression de l'employé");
            }
            
            listEmployees(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("employee-management");
        }
    }
}
