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

/**
 * Servlet for handling employee login.
 */
@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handles GET requests to the login page.
     * If user is already logged in, redirects to dashboard.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Check if user is already logged in
        if (session != null && session.getAttribute("employee") != null) {

            response.sendRedirect("dashboard");
            return;
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    /**
     * Handles POST requests for login form submission.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate input
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        Employee employee = employeeDAO.authenticate(username, password);

        if (employee != null) {
            // Authentication successful
            HttpSession session = request.getSession();
            session.setAttribute("employee", employee);
            session.setAttribute("employeeId", employee.getId());
            session.setAttribute("employeeName", employee.getFullName());

            response.sendRedirect("dashboard");
        } else {

            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
