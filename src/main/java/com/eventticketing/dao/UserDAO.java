
package com.eventticketing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.eventticketing.model.User;
import com.eventticketing.util.DBConnection;

public class UserDAO {

    // Register a new user
    public int registerUser(User user) throws SQLException {

        String sql = """
                INSERT INTO users (full_name, email, password_hash)
                VALUES (?, ?, ?)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                return -1;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return -1;
    }

    // Find a user by email for login
    public User findByEmail(String email) throws SQLException {

        String sql = """
                SELECT user_id, full_name, email,
                       password_hash, created_at
                FROM users
                WHERE email = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        }

        return null;
    }
}
