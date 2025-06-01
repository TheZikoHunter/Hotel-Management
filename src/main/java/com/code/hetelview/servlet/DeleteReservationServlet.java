package com.code.hetelview.servlet;

import com.code.hetelview.dao.ReservationDAO;
import com.code.hetelview.model.Employee;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet for handling reservation deletion.
 */
@WebServlet(name = "deleteReservationServlet", value = "/delete-reservation")
public class DeleteReservationServlet extends HttpServlet {

    private ReservationDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        reservationDAO = new ReservationDAO();
    }

    /**
     * Check if the user is logged in.
     */
    private boolean isAuthenticated(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("employee") == null) {
            response.sendRedirect("login");
            return false;
        }
        
        return true;
    }

    /**
     * Handles GET requests for reservation deletion.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAuthenticated(request, response)) {
            return;
        }

        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("dashboard?error=Invalid reservation ID");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            
            if (reservationDAO.deleteReservation(id)) {
                response.sendRedirect("dashboard?success=Reservation deleted successfully");
            } else {
                response.sendRedirect("dashboard?error=Failed to delete reservation");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("dashboard?error=Invalid reservation ID");
        }
    }
}
