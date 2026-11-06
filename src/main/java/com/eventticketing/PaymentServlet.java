
package com.eventticketing;

import java.io.IOException;

import com.eventticketing.dao.ReservationDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

    private final ReservationDAO reservationDAO = new ReservationDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int userId = ((Number) session.getAttribute("userId")).intValue();

            int reservationId = Integer.parseInt(
                request.getParameter("reservationId")
            );

            var reservation = reservationDAO.getReservationById(reservationId);

            if (reservation == null || reservation.getUserId() != userId) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                                   "Reservation not found or access denied.");
                return;
            }

            boolean paymentSuccessful =
                reservationDAO.processPayment(reservationId, true);

            if (paymentSuccessful) {
                response.sendRedirect(
                    request.getContextPath()
                    + "/payment-result?status=success&reservationId="
                    + reservationId
                );
            } else {
                response.sendRedirect(
                    request.getContextPath()
                    + "/payment-result?status=failed&reservationId="
                    + reservationId
                );
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                               "Invalid reservation ID.");
        } catch (Exception e) {
            throw new ServletException("Unable to process payment.", e);
        }
    }
}
