
package com.eventticketing;

import java.io.IOException;
import java.sql.SQLException;

import com.eventticketing.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/Register.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            int userId = userService.register(fullName, email, password);

            if (userId > 0) {
                // Registration succeeded. Send the user to login.
                response.sendRedirect(
                    request.getContextPath() + "/login?registered=true"
                );
            } else {
                request.setAttribute(
                    "error",
                    "Registration failed. Please try again."
                );

                request.getRequestDispatcher("/Register.jsp")
                       .forward(request, response);
            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());

            request.getRequestDispatcher("/Register.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            // SQLState class 23 indicates a database constraint violation,
            // such as registering an email that already exists.
            if (e.getSQLState() != null
                    && e.getSQLState().startsWith("23")) {

                request.setAttribute(
                    "error",
                    "This email may already be registered. Please try logging in."
                );

                request.getRequestDispatcher("/Register.jsp")
                       .forward(request, response);

            } else {
                throw new ServletException(
                    "Unable to register the user.", e
                );
            }
        }
    }
}
