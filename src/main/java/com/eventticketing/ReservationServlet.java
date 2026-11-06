
package com.eventticketing;

import java.io.IOException;
import java.sql.SQLException;

import com.eventticketing.dao.ReservationDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/reserve")
public class ReservationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ReservationDAO reservationDAO = new ReservationDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // A user must be logged in before reserving a seat.
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                request.getContextPath() + "/login"
            );
            return;
        }

        int userId;
        try {
            Object sessionUserId = session.getAttribute("userId");

            if (!(sessionUserId instanceof Number)) {
                session.invalidate();
                response.sendRedirect(
                    request.getContextPath() + "/login"
                );
                return;
            }

            userId = ((Number) sessionUserId).intValue();

        } catch (IllegalStateException e) {
            response.sendRedirect(
                request.getContextPath() + "/login"
            );
            return;
        }

        int eventSeatId;

        try {
            eventSeatId = Integer.parseInt(
                request.getParameter("eventSeatId")
            );

            if (eventSeatId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid seat selection."
            );
            return;
        }

        try {
            int reservationId =
                reservationDAO.holdSeat(userId, eventSeatId);

            if (reservationId == -1) {
                request.setAttribute(
                    "error",
                    "Sorry, this seat is no longer available. Please choose another seat."
                );

                request.getRequestDispatcher("/reservation-error.jsp")
                       .forward(request, response);
                return;
            }

            // The seat is held. Next, show the booking summary.
            response.sendRedirect(
                request.getContextPath()
                    + "/booking?reservationId=" + reservationId
            );

        } catch (SQLException e) {
            throw new ServletException(
                "Unable to reserve the selected seat.", e
            );
        }
    }
}
