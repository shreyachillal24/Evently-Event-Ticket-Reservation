package com.eventticketing.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class EventReader {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/event_ticketing";
        String username = "root";
        String password = "shreya@2425";

        String query = "SELECT * FROM events";

        try (
            Connection con = DriverManager.getConnection(url, username, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query)
        ) {
            while (rs.next()) {
                int id = rs.getInt("event_id");
                String name = rs.getString("event_name");
                double price = rs.getDouble("ticket_price");
                Timestamp date=rs.getTimestamp("event_date");
                String status=rs.getString("event_status");
                
                System.out.println("Event ID: " + id);
                System.out.println("Event Name: " + name);
                System.out.println("Ticket Price: " + price);
                System.out.println("Event Date: "+date);
                System.out.println("Event Status: "+status);
                
                System.out.println("--------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}