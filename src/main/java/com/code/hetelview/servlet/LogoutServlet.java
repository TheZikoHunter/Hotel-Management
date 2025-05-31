package com.code.hetelview.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet for handling user logout.
 */
@WebServlet(name = "logoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Handles GET requests for logout.
     * Invalidates the user's session and redirects to the login page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect("login");
    }
}
