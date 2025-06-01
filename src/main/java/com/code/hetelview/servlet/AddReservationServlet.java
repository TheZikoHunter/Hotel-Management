package com.code.hetelview.servlet;

import com.code.hetelview.dao.ReservationDAO;
import com.code.hetelview.model.Employee;
import com.code.hetelview.model.Reservation;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
/**
 * Servlet for handling adding new reservations.
 */
@WebServlet(name = "addReservationServlet", value = "/add-reservation")
public class AddReservationServlet extends HttpServlet {

    private ReservationDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        reservationDAO = new ReservationDAO();
    }

    /**
     * Check if the user is a réceptionniste (can manage reservations).
     */
    private boolean isReceptionniste(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Employee employee = (Employee) session.getAttribute("employee");
            if (employee != null) {
                return "réceptionniste".equals(employee.getRole());
            }
        }
        return false;
    }

    /**
     * Handles GET requests to the add reservation page.
     * Displays the form for adding a new reservation.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("employee") == null) {
            response.sendRedirect("login");
            return;
        }

        // Check if user is réceptionniste
        if (!isReceptionniste(request)) {
            response.sendRedirect("dashboard?error=Accès refusé. Seuls les réceptionnistes peuvent ajouter des réservations.");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/add-reservation.jsp").forward(request, response);
    }

    /**
     * Handles POST requests for adding a new reservation.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("employee") == null) {
            // User is not logged in, redirect to login page
            response.sendRedirect("login");
            return;
        }

        // Check if user is réceptionniste
        if (!isReceptionniste(request)) {
            response.sendRedirect("dashboard?error=Accès refusé. Seuls les réceptionnistes peuvent ajouter des réservations.");
            return;
        }

        // Get employee for creating reservation
        Employee employee = (Employee) session.getAttribute("employee");

        // Get form parameters
        String guestName = request.getParameter("guestName");
        String guestEmail = request.getParameter("guestEmail");
        String guestPhone = request.getParameter("guestPhone");
        String roomNumberStr = request.getParameter("roomNumber");
        String checkInDateStr = request.getParameter("checkInDate");
        String checkOutDateStr = request.getParameter("checkOutDate");
        String status = request.getParameter("status");
        String notes = request.getParameter("notes");

        // Validate input
        if (guestName == null || guestName.trim().isEmpty() ||
            guestEmail == null || guestEmail.trim().isEmpty() ||
            guestPhone == null || guestPhone.trim().isEmpty() ||
            roomNumberStr == null || roomNumberStr.trim().isEmpty() ||
            checkInDateStr == null || checkInDateStr.trim().isEmpty() ||
            checkOutDateStr == null || checkOutDateStr.trim().isEmpty() ||
            status == null || status.trim().isEmpty()) {

            request.setAttribute("error", "Tous les champs sont obligatoires");
            request.getRequestDispatcher("/WEB-INF/views/add-reservation.jsp").forward(request, response);
            return;
        }

        try {
            // Parse room number
            int roomNumber = Integer.parseInt(roomNumberStr);

            // Parse dates
            Date checkInDate = Date.valueOf(checkInDateStr);
            Date checkOutDate = Date.valueOf(checkOutDateStr);

            // Validate check-out date is after check-in date
            if (checkOutDate.before(checkInDate)) {
                request.setAttribute("error", "La date de départ doit être postérieure à la date d'arrivée");
                request.getRequestDispatcher("/WEB-INF/views/add-reservation.jsp").forward(request, response);
                return;
            }

            // Create reservation object
            Reservation reservation = new Reservation();
            reservation.setGuestName(guestName);
            reservation.setGuestEmail(guestEmail);
            reservation.setGuestPhone(guestPhone);
            reservation.setRoomNumber(roomNumber);
            reservation.setCheckInDate(checkInDate);
            reservation.setCheckOutDate(checkOutDate);
            reservation.setStatus(status);
            reservation.setCreatedBy(employee);
            reservation.setNotes(notes != null ? notes : "");

            // Add reservation to database
            boolean success = reservationDAO.addReservation(reservation);

            if (success) {
                // Redirect to dashboard
                response.sendRedirect("dashboard");
            } else {
                request.setAttribute("error", "Échec de l'ajout de la réservation. Veuillez réessayer.");
                request.getRequestDispatcher("/WEB-INF/views/add-reservation.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Numéro de chambre invalide");
            request.getRequestDispatcher("/WEB-INF/views/add-reservation.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Format de date invalide");
            request.getRequestDispatcher("/WEB-INF/views/add-reservation.jsp").forward(request, response);
        }
    }
}
