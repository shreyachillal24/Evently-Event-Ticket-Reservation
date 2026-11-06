
package com.eventticketing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eventticketing.model.Seat;
import com.eventticketing.util.DBConnection;

public class SeatDAO {

	    public List<Seat> getSeatsByEventId(int eventId)
	            throws SQLException {

	        List<Seat> seats = new ArrayList<>();

	        String sql = """
	                SELECT es.event_seat_id,
	                       s.seat_id,
	                       s.seat_number,
	                       s.seat_type,
	                       es.seat_status
	                FROM event_seats es
	                JOIN seats s ON es.seat_id = s.seat_id
	                WHERE es.event_id = ?
	                """;

	        try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setInt(1, eventId);

	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    Seat seat = new Seat(
	                            rs.getInt("event_seat_id"),
	                            rs.getInt("seat_id"),
	                            rs.getString("seat_number"),
	                            rs.getString("seat_type"),
	                            rs.getString("seat_status")
	                    );

	                    seats.add(seat);
	                }
	            }
	        }

	        return seats;
	    }
	    
	    
	    
	    public List<Seat> getAvailableSeats(int eventId)
	            throws SQLException {

	        List<Seat> seats = new ArrayList<>();

	        String sql = """
	                SELECT es.event_seat_id,
	                       s.seat_id,
	                       s.seat_number,
	                       s.seat_type,
	                       es.seat_status
	                FROM event_seats es
	                JOIN seats s ON es.seat_id = s.seat_id
	                WHERE es.event_id = ?
	                  AND es.seat_status = 'AVAILABLE'
	                """;

	        try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setInt(1, eventId);

	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    Seat seat = new Seat(
	                            rs.getInt("event_seat_id"),
	                            rs.getInt("seat_id"),
	                            rs.getString("seat_number"),
	                            rs.getString("seat_type"),
	                            rs.getString("seat_status")
	                    );

	                    seats.add(seat);
	                }
	            }
	        }

	        return seats;
	    }
	    

public int getEventIdByEventSeatId(int eventSeatId) throws SQLException {

    String sql = """
            SELECT event_id
            FROM event_seats
            WHERE event_seat_id = ?
            """;

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, eventSeatId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("event_id");
            }
        }
    }

    return -1;
}



public Seat getSeatByEventSeatId(int eventSeatId) throws SQLException {

    String sql = """
            SELECT es.event_seat_id,
                   es.seat_id,
                   s.seat_number,
                   s.seat_type,
                   es.seat_status
            FROM event_seats es
            JOIN seats s ON es.seat_id = s.seat_id
            WHERE es.event_seat_id = ?
            """;

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, eventSeatId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {

                return new Seat(
                    rs.getInt("event_seat_id"),
                    rs.getInt("seat_id"),
                    rs.getString("seat_number"),
                    rs.getString("seat_type"),
                    rs.getString("seat_status")
                );
            }
        }
    }

    return null;
}



	}

