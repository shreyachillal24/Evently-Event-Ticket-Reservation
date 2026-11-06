
package com.eventticketing.service;

import java.sql.SQLException;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

import com.eventticketing.dao.UserDAO;
import com.eventticketing.model.User;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Register a new user
    public int register(String fullName, String email, String password)
            throws SQLException {

        if (fullName == null || fullName.isBlank()
                || fullName.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "Name is required and must be at most 100 characters.");
        }

        if (email == null || email.isBlank()
                || email.length() > 150
                || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("Enter a valid email address.");
        }

        if (password == null || password.length() < 8
                || password.length() > 72) {
            throw new IllegalArgumentException(
                    "Password must be between 8 and 72 characters.");
        }

        String normalizedEmail = email.trim().toLowerCase();
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));

        User user = new User(
                fullName.trim(),
                normalizedEmail,
                passwordHash
        );

        return userDAO.registerUser(user);
    }

    // Login an existing user
    public User login(String email, String password) throws SQLException {

        if (email == null || email.isBlank()
                || password == null || password.isEmpty()) {
            return null;
        }

        User user = userDAO.findByEmail(email.trim().toLowerCase());

        if (user == null) {
            return null;
        }

        if (BCrypt.checkpw(password, user.getPasswordHash())) {
            return user;
        }

        return null;
    }
}
