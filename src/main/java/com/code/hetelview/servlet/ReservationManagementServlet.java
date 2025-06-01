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
 * Servlet for handling reservation management operations (Edit/Delete).
 */
@WebServlet(name = "reservationManagementServlet", value = "/reservation-management")
public class ReservationManagementServlet extends HttpServlet {

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
     * Check if the user is staff (not admin) and authorized for reservation management operations.
     */
    private boolean isAuthorizedStaff(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        
        if (!isAuthenticated(request, response)) {
            return false;
        }
        
        Employee employee = (Employee) session.getAttribute("employee");
        if (!"staff".equalsIgnoreCase(employee.getRole())) {
            response.sendRedirect("dashboard?error=Accès refusé. Seuls les membres du personnel peuvent gérer les réservations.");
            return false;
        }
        
        return true;
    }

    /**
     * Handles GET requests for reservation management operations.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAuthorizedStaff(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        
        if ("edit".equals(action)) {
            showEditForm(request, response);
        } else {
            response.sendRedirect("dashboard");
        }
    }

    /**
     * Handles POST requests for updating and deleting reservations.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAuthorizedStaff(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        
        if ("edit".equals(action)) {
            updateReservation(request, response);
        } else if ("delete".equals(action)) {
            deleteReservation(request, response);
        } else {
            response.sendRedirect("dashboard");
        }
    }

    /**
     * Show edit reservation form.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("dashboard");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            Reservation reservation = reservationDAO.getReservationById(id);
            
            if (reservation == null) {
                request.setAttribute("error", "Réservation introuvable");
                response.sendRedirect("dashboard");
                return;
            }
            
            request.setAttribute("reservation", reservation);
            request.getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("dashboard");
        }
    }

    /**
     * Update an existing reservation.
     */
    private void updateReservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        String guestName = request.getParameter("guestName");
        String guestEmail = request.getParameter("guestEmail");
        String guestPhone = request.getParameter("guestPhone");
        String roomNumberStr = request.getParameter("roomNumber");
        String checkInDateStr = request.getParameter("checkInDate");
        String checkOutDateStr = request.getParameter("checkOutDate");
        String status = request.getParameter("status");
        String notes = request.getParameter("notes");

        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("dashboard");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            
            // Validate input
            if (guestName == null || guestName.trim().isEmpty() ||
                guestEmail == null || guestEmail.trim().isEmpty() ||
                guestPhone == null || guestPhone.trim().isEmpty() ||
                roomNumberStr == null || roomNumberStr.trim().isEmpty() ||
                checkInDateStr == null || checkInDateStr.trim().isEmpty() ||
                checkOutDateStr == null || checkOutDateStr.trim().isEmpty() ||
                status == null || status.trim().isEmpty()) {
                
                request.setAttribute("error", "Tous les champs sauf les notes sont obligatoires");
                Reservation reservation = reservationDAO.getReservationById(id);
                request.setAttribute("reservation", reservation);
                request.getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp").forward(request, response);
                return;
            }

            // Get the existing reservation
            Reservation reservation = reservationDAO.getReservationById(id);
            if (reservation == null) {
                request.setAttribute("error", "Réservation introuvable");
                response.sendRedirect("dashboard");
                return;
            }

            // Parse and validate data
            int roomNumber = Integer.parseInt(roomNumberStr);
            Date checkInDate = Date.valueOf(checkInDateStr);
            Date checkOutDate = Date.valueOf(checkOutDateStr);

            // Validate check-out date is after check-in date
            if (checkOutDate.before(checkInDate)) {
                request.setAttribute("error", "La date de départ doit être postérieure à la date d'arrivée");
                request.setAttribute("reservation", reservation);
                request.getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp").forward(request, response);
                return;
            }

            // Update reservation fields
            reservation.setGuestName(guestName.trim());
            reservation.setGuestEmail(guestEmail.trim());
            reservation.setGuestPhone(guestPhone.trim());
            reservation.setRoomNumber(roomNumber);
            reservation.setCheckInDate(checkInDate);
            reservation.setCheckOutDate(checkOutDate);
            reservation.setStatus(status.trim());
            reservation.setNotes(notes != null ? notes.trim() : "");

            if (reservationDAO.updateReservation(reservation)) {
                response.sendRedirect("dashboard?success=Réservation mise à jour avec succès");
            } else {
                request.setAttribute("error", "Échec de la mise à jour de la réservation");
                request.setAttribute("reservation", reservation);
                request.getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Numéro de chambre invalide");
            Reservation reservation = reservationDAO.getReservationById(Integer.parseInt(idParam));
            request.setAttribute("reservation", reservation);
            request.getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Format de date invalide");
            Reservation reservation = reservationDAO.getReservationById(Integer.parseInt(idParam));
            request.setAttribute("reservation", reservation);
            request.getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp").forward(request, response);
        }
    }

    /**
     * Delete a reservation.
     */
    private void deleteReservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("dashboard?error=ID de réservation invalide");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            
            // Check if reservation exists
            Reservation reservation = reservationDAO.getReservationById(id);
            if (reservation == null) {
                response.sendRedirect("dashboard?error=Réservation introuvable");
                return;
            }
            
            // Attempt to delete the reservation
            if (reservationDAO.deleteReservation(id)) {
                response.sendRedirect("dashboard?success=Réservation supprimée avec succès");
            } else {
                response.sendRedirect("dashboard?error=Échec de la suppression de la réservation");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("dashboard?error=ID de réservation invalide");
        }
    }
}
