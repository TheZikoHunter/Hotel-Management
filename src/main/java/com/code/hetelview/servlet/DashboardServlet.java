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
     * Retrieves reservations (filtered if search parameters provided) and forwards them to the dashboard.jsp page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("employee") == null) {
            response.sendRedirect("login");
            return;
        }

        // Get search parameters
        String searchGuestName = request.getParameter("searchGuestName");
        String searchGuestEmail = request.getParameter("searchGuestEmail");
        String searchRoomNumber = request.getParameter("searchRoomNumber");
        String searchStatus = request.getParameter("searchStatus");
        String searchCheckInFrom = request.getParameter("searchCheckInFrom");
        String searchCheckInTo = request.getParameter("searchCheckInTo");

        List<Reservation> reservations;

        // Check if any search parameters are provided
        boolean hasSearchParams = (searchGuestName != null && !searchGuestName.trim().isEmpty()) ||
                                 (searchGuestEmail != null && !searchGuestEmail.trim().isEmpty()) ||
                                 (searchRoomNumber != null && !searchRoomNumber.trim().isEmpty()) ||
                                 (searchStatus != null && !searchStatus.trim().isEmpty()) ||
                                 (searchCheckInFrom != null && !searchCheckInFrom.trim().isEmpty()) ||
                                 (searchCheckInTo != null && !searchCheckInTo.trim().isEmpty());

        if (hasSearchParams) {
            // Parse room number if provided
            Integer roomNumber = null;
            if (searchRoomNumber != null && !searchRoomNumber.trim().isEmpty()) {
                try {
                    roomNumber = Integer.parseInt(searchRoomNumber.trim());
                } catch (NumberFormatException e) {
                    // Invalid room number, ignore
                }
            }

            // Parse dates if provided
            java.sql.Date checkInFrom = null;
            java.sql.Date checkInTo = null;
            try {
                if (searchCheckInFrom != null && !searchCheckInFrom.trim().isEmpty()) {
                    checkInFrom = java.sql.Date.valueOf(searchCheckInFrom);
                }
                if (searchCheckInTo != null && !searchCheckInTo.trim().isEmpty()) {
                    checkInTo = java.sql.Date.valueOf(searchCheckInTo);
                }
            } catch (IllegalArgumentException e) {
                // Invalid date format, ignore
            }

            // Perform search
            reservations = reservationDAO.searchReservations(
                searchGuestName, searchGuestEmail, roomNumber, searchStatus, checkInFrom, checkInTo
            );
        } else {
            // No search parameters, get all reservations
            reservations = reservationDAO.getAllReservations();
        }

        // Set attributes for JSP
        request.setAttribute("reservations", reservations);
        request.setAttribute("searchGuestName", searchGuestName);
        request.setAttribute("searchGuestEmail", searchGuestEmail);
        request.setAttribute("searchRoomNumber", searchRoomNumber);
        request.setAttribute("searchStatus", searchStatus);
        request.setAttribute("searchCheckInFrom", searchCheckInFrom);
        request.setAttribute("searchCheckInTo", searchCheckInTo);

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
