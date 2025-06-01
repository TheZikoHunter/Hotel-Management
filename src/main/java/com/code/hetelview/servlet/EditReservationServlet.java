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
 * Servlet for handling individual reservation editing.
 */
@WebServlet(name = "editReservationServlet", value = "/edit-reservation")
public class EditReservationServlet extends HttpServlet {

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
     * Handles GET requests to show the edit reservation form.
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
            Reservation reservation = reservationDAO.getReservationById(id);
            
            if (reservation == null) {
                response.sendRedirect("dashboard?error=Reservation not found");
                return;
            }
            
            request.setAttribute("reservation", reservation);
            request.getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("dashboard?error=Invalid reservation ID");
        }
    }

    /**
     * Handles POST requests for updating reservations.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAuthenticated(request, response)) {
            return;
        }

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
            response.sendRedirect("dashboard?error=Invalid reservation ID");
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
                
                request.setAttribute("error", "All fields except notes are required");
                Reservation reservation = reservationDAO.getReservationById(id);
                request.setAttribute("reservation", reservation);
                request.getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp").forward(request, response);
                return;
            }

            // Get the existing reservation
            Reservation reservation = reservationDAO.getReservationById(id);
            if (reservation == null) {
                response.sendRedirect("dashboard?error=Reservation not found");
                return;
            }

            // Parse and validate data
            int roomNumber = Integer.parseInt(roomNumberStr);
            Date checkInDate = Date.valueOf(checkInDateStr);
            Date checkOutDate = Date.valueOf(checkOutDateStr);

            // Validate check-out date is after check-in date
            if (checkOutDate.before(checkInDate)) {
                request.setAttribute("error", "Check-out date must be after check-in date");
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
                response.sendRedirect("dashboard?success=Reservation updated successfully");
            } else {
                request.setAttribute("error", "Failed to update reservation");
                request.setAttribute("reservation", reservation);
                request.getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid room number");
            try {
                int id = Integer.parseInt(idParam);
                Reservation reservation = reservationDAO.getReservationById(id);
                request.setAttribute("reservation", reservation);
                request.getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp").forward(request, response);
            } catch (NumberFormatException ex) {
                response.sendRedirect("dashboard?error=Invalid reservation ID");
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date format");
            try {
                int id = Integer.parseInt(idParam);
                Reservation reservation = reservationDAO.getReservationById(id);
                request.setAttribute("reservation", reservation);
                request.getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp").forward(request, response);
            } catch (NumberFormatException ex) {
                response.sendRedirect("dashboard?error=Invalid reservation ID");
            }
        }
    }
}
