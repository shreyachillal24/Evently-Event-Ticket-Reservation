
package com.eventticketing.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            System.getenv().getOrDefault(
                    "EVENT_DB_URL",
                    "jdbc:mysql://localhost:3306/event_ticketing"
            );

    private static final String USERNAME =
            System.getenv("EVENT_DB_USERNAME");

    private static final String PASSWORD =
            System.getenv("EVENT_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {

        if (USERNAME == null || USERNAME.isBlank()
                || PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                    "Set EVENT_DB_USERNAME and EVENT_DB_PASSWORD "
                    + "environment variables."
            );
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver class was not found.", e);
        }

        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
