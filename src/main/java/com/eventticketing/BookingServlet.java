
/*booking summary page*/
package com.eventticketing;

import java.io.IOException;
import java.sql.SQLException;

import com.eventticketing.dao.EventDAO;
import com.eventticketing.dao.ReservationDAO;
import com.eventticketing.dao.SeatDAO;
import com.eventticketing.model.Event;
import com.eventticketing.model.Reservation;
import com.eventticketing.model.Seat;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final EventDAO eventDAO = new EventDAO();
    private final SeatDAO seatDAO = new SeatDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Object sessionUserId = session.getAttribute("userId");

        if (!(sessionUserId instanceof Number)) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = ((Number) sessionUserId).intValue();
        
        int reservationId;
        try {
            reservationId = Integer.parseInt(
                request.getParameter("reservationId")
            );

            if (reservationId <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid reservation ID."
            );
            return;
        }

        try {
            Reservation reservation =
                reservationDAO.getReservationById(reservationId);

            // Never show another user's reservation.
            if (reservation == null
                    || reservation.getUserId() != userId) {
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Reservation not found."
                );
                return;
            }

            // Only an active hold may proceed to payment.
            if (!"HELD".equalsIgnoreCase(
                        reservation.getReservationStatus())
                    || !"PENDING".equalsIgnoreCase(
                        reservation.getPaymentStatus())
                    || reservation.getExpiresAt() == null
                    || !reservation.getExpiresAt().after(
                        new java.sql.Timestamp(System.currentTimeMillis()))) {

                request.setAttribute(
                    "error",
                    "This seat hold has expired or is no longer active. Please select an available seat again."
                );

                request.getRequestDispatcher("/reservation-error.jsp")
                       .forward(request, response);
                return;
            }

            // Resolve the selected seat and event.
            Seat seat = seatDAO.getSeatByEventSeatId(
                reservation.getEventSeatId()
            );

            if (seat == null) {
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Seat not found."
                );
                return;
            }

            Event event = eventDAO.getEventById(
                seatDAO.getEventIdByEventSeatId(
                    reservation.getEventSeatId()
                )
            );

            if (event == null) {
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Event not found."
                );
                return;
            }

            request.setAttribute("reservation", reservation);
            request.setAttribute("seat", seat);
            request.setAttribute("event", event);

            request.getRequestDispatcher("/booking.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                "Unable to load booking details.", e
            );
        }
    }
}
