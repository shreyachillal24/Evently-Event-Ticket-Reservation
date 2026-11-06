package com.eventticketing.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eventticketing.model.Event;
import com.eventticketing.util.DBConnection;

public class EventDAO {
	

	
//getting all the events with their id and price
    public List<Event> getAllEvents() throws SQLException {

        List<Event> events = new ArrayList<>();

        String sql = "SELECT * FROM events";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Event event = new Event(
                    rs.getInt("event_id"),
                    rs.getInt("venue_id"),
                    rs.getString("event_name"),
                    rs.getTimestamp("event_date"),
                    rs.getString("event_description"),
                    rs.getBigDecimal("ticket_price"),
                    rs.getString("event_status")
                );

                events.add(event);
            }
        }

        return events;
    }
    
    //getting events by their specific id
	public Event getEventById(int eventId) throws SQLException {

	    String sql = "SELECT * FROM events WHERE event_id = ?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, eventId);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return new Event(
	                    rs.getInt("event_id"),
	                    rs.getInt("venue_id"),
	                    rs.getString("event_name"),
	                    rs.getTimestamp("event_date"),
	                    rs.getString("event_description"),
	                    rs.getBigDecimal("ticket_price"),
	                    rs.getString("event_status")
	                );
	            }
	        }
	    }

	    return null;
	}
}


