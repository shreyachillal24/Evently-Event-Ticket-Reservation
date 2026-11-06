/*gives the result whether the payment was successful or failed*/
package com.eventticketing;

import java.io.IOException;

import com.eventticketing.dao.ReservationDAO;
import com.eventticketing.model.Reservation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/payment-result")
public class PaymentResultServlet extends HttpServlet {

    private final ReservationDAO reservationDAO = new ReservationDAO();

    @Override
    protected void doGet(HttpServletRequest request,
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

            Reservation reservation =
                reservationDAO.getReservationById(reservationId);

            if (reservation == null || reservation.getUserId() != userId) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                                   "Reservation not found or access denied.");
                return;
            }

            request.setAttribute("reservation", reservation);
            request.setAttribute("paymentStatus", reservation.getPaymentStatus());

            request.getRequestDispatcher("/payment-result.jsp")
                   .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                               "Invalid reservation ID.");
        } catch (Exception e) {
            throw new ServletException(
                "Unable to load payment result.", e
            );
        }
    }
}
