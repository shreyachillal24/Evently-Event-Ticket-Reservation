
package com.eventticketing;

import java.io.IOException;
import java.sql.SQLException;

import com.eventticketing.model.User;
import com.eventticketing.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/Login.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = userService.login(email, password);

            if (user == null) {
                request.setAttribute(
                    "error",
                    "Invalid email or password."
                );

                request.getRequestDispatcher("/login.jsp")
                       .forward(request, response);
                return;
            }

            // Prevent session fixation after successful login.
            HttpSession oldSession = request.getSession(false);

            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userName", user.getFullName());

            String redirect = request.getParameter("redirect");

            if (redirect != null
                    && redirect.startsWith(request.getContextPath() + "/")
                    && !redirect.startsWith("//")) {

                response.sendRedirect(redirect);
            } else {
                response.sendRedirect(
                    request.getContextPath() + "/events"
                );
            }

        } catch (SQLException e) {
            throw new ServletException(
                "Unable to process login.", e
            );
        }
    }
}
