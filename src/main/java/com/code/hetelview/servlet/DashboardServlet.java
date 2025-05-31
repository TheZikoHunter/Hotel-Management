package com.code.hetelview.servlet;

import com.code.hetelview.dao.ReservationDAO;
import com.code.hetelview.model.Reservation;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for handling the dashboard page.
 */
@WebServlet(name = "dashboardServlet", value = "/dashboard")
public class DashboardServlet extends HttpServlet {

    private ReservationDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        reservationDAO = new ReservationDAO();
    }

    /**
     * Handles GET requests to the dashboard page.
     * Retrieves all reservations and forwards them to the dashboard.jsp page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("employee") == null) {

            response.sendRedirect("login");
            return;
        }


        List<Reservation> reservations = reservationDAO.getAllReservations();


        request.setAttribute("reservations", reservations);


        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}
