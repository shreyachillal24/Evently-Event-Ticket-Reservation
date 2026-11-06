
package com.eventticketing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.eventticketing.dao.EventDAO;
import com.eventticketing.model.Event;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/events")
public class EventListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final EventDAO eventDAO = new EventDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Event> events = eventDAO.getAllEvents();

            request.setAttribute("events", events);

            request.getRequestDispatcher("/events.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Unable to load events from database.", e);
        }
    }
}
