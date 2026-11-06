
package com.eventticketing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.eventticketing.dao.EventDAO;
import com.eventticketing.dao.SeatDAO;
import com.eventticketing.model.Event;
import com.eventticketing.model.Seat;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/event-details")
public class EventDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final EventDAO eventDAO = new EventDAO();
    private final SeatDAO seatDAO = new SeatDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        try {
            int eventId = Integer.parseInt(id);

            if (eventId <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                   "Invalid event ID.");
                return;
            }

            Event event = eventDAO.getEventById(eventId);

            if (event == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                                   "Event not found.");
                return;
            }

            List<Seat> seats = seatDAO.getSeatsByEventId(eventId);

            request.setAttribute("event", event);
            request.setAttribute("seats", seats);

            request.getRequestDispatcher("/event-details.jsp")
                   .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                               "Event ID must be a number.");
        } catch (SQLException e) {
            throw new ServletException("Unable to load event details.", e);
        }
    }
}
