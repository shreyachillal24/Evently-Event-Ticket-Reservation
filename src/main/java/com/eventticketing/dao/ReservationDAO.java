package com.eventticketing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.eventticketing.model.Reservation;
import com.eventticketing.util.DBConnection;

public class ReservationDAO {

    // 1. Get reservation by ID
    public Reservation getReservationById(int reservationId)
    
            throws SQLException {
    	if (reservationId <= 0) {
    	    return null;
    	}

        String sql = """
                SELECT reservation_id, user_id, event_seat_id,
                       reservation_status, reserved_at, expires_at,payment_status
                FROM reservations
                WHERE reservation_id = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reservationId);

            try (ResultSet rs = ps.executeQuery()) {
            	if (rs.next()) {
            	    		Reservation reservation = new Reservation(
            	    			    rs.getInt("reservation_id"),
            	    			    rs.getInt("user_id"),
            	    			    rs.getInt("event_seat_id"),
            	    			    rs.getString("reservation_status"),
            	    			    rs.getTimestamp("reserved_at"),
            	    			    rs.getTimestamp("expires_at"),
            	    			    rs.getString("payment_status")
            	    			);
            	    

            	    return reservation;
            	}
            }
        }

        return null;
    }


    // 2. Hold a seat and return the generated reservation ID
    public int holdSeat(int userId, int eventSeatId)
            throws SQLException {
    	if (userId <= 0 || eventSeatId <= 0) {
    	    return -1;
    	}

        // Release expired holds before attempting a new hold.
        releaseExpiredReservations();

        String checkSql = """
                SELECT seat_status
                FROM event_seats
                WHERE event_seat_id = ?
                FOR UPDATE
                """;
        
        

        String reservationSql = """
                INSERT INTO reservations
                (user_id, event_seat_id, reservation_status, expires_at)
                VALUES (?, ?, 'HELD', DATE_ADD(NOW(), INTERVAL 10 MINUTE))
                """;

        String updateSql = """
                UPDATE event_seats
                SET seat_status = 'HELD'
                WHERE event_seat_id = ?
                  AND seat_status = 'AVAILABLE'
                """;

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
            	
                // Check and lock the seat.
                try (PreparedStatement ps = con.prepareStatement(checkSql)) {
                    ps.setInt(1, eventSeatId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()
                                || !"AVAILABLE".equals(
                                        rs.getString("seat_status"))) {
                            con.rollback();
                            return -1;
                        }
                    }
                }

                // Insert the reservation only once.
                int reservationId;

                try (PreparedStatement ps = con.prepareStatement(
                        reservationSql,
                        java.sql.Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, userId);
                    ps.setInt(2, eventSeatId);
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "Failed to get reservation ID");
                        }

                        reservationId = rs.getInt(1);
                    }
                }
                
              

            

                // Mark the seat as HELD.
               
                try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                    ps.setInt(1, eventSeatId);

                    if (ps.executeUpdate() != 1) {
                        con.rollback();
                        return -1;
                    }
                }

                con.commit();
                return reservationId;

            } catch (SQLException | RuntimeException e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
                throw e;
            }
            }
        }
    


    


    // 3. Release expired reservations

public int releaseExpiredReservations() throws SQLException {

    String selectSql = """
            SELECT reservation_id, event_seat_id
            FROM reservations
            WHERE reservation_status = 'HELD'
              AND payment_status = 'PENDING'
              AND expires_at <= NOW()
            FOR UPDATE
            """;


    String expireSql = """
    		UPDATE reservations
    		SET reservation_status = 'EXPIRED'
        WHERE reservation_id = ?
          AND reservation_status = 'HELD'
          AND payment_status = 'PENDING'
          AND expires_at <= NOW()
        """;


    String releaseSeatSql = """
            UPDATE event_seats es
            SET seat_status = 'AVAILABLE'
            WHERE es.event_seat_id = ?
              AND es.seat_status = 'HELD'
              AND NOT EXISTS (
                  SELECT 1
                  FROM reservations r
                  WHERE r.event_seat_id = es.event_seat_id
                    AND r.reservation_status = 'HELD'
                    AND r.expires_at > NOW()
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM reservations r
                  WHERE r.event_seat_id = es.event_seat_id
                    AND r.reservation_status = 'CONFIRMED'
              )
            """;

    try (Connection con = DBConnection.getConnection()) {
        con.setAutoCommit(false);

        try {
            List<Integer> reservationIds = new ArrayList<>();
            List<Integer> eventSeatIds = new ArrayList<>();

            try (PreparedStatement ps =
                    con.prepareStatement(selectSql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    reservationIds.add(
                            rs.getInt("reservation_id"));
                    eventSeatIds.add(
                            rs.getInt("event_seat_id"));
                }
            }

            int releasedCount = 0;

            for (int i = 0; i < reservationIds.size(); i++) {
                int reservationId = reservationIds.get(i);
                int eventSeatId = eventSeatIds.get(i);

                try (PreparedStatement ps =
                        con.prepareStatement(expireSql)) {

                    ps.setInt(1, reservationId);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps =
                        con.prepareStatement(releaseSeatSql)) {

                    ps.setInt(1, eventSeatId);

                    if (ps.executeUpdate() == 1) {
                        releasedCount++;
                    }
                }
            }

            con.commit();
            return releasedCount;

        } catch (SQLException | RuntimeException e) {
            try {
                con.rollback();
            } catch (SQLException rollbackException) {
                e.addSuppressed(rollbackException);
            }
            throw e;
        }
        }
    
}

    
    

 // 4. Process simulated payment
 

public boolean processPayment(int reservationId, boolean paymentSuccessful)
        throws SQLException {
	if (reservationId <= 0) {
	    return false;
	}
    String checkSql = """
            SELECT event_seat_id, expires_at
            FROM reservations
            WHERE reservation_id = ?
              AND reservation_status = 'HELD'
              AND payment_status = 'PENDING'
            FOR UPDATE
            """;
    
    String lockSeatSql = """
            SELECT seat_status
            FROM event_seats
            WHERE event_seat_id = ?
            FOR UPDATE
            """;

    String expireSql = """
            UPDATE reservations
            SET reservation_status = 'EXPIRED'
            WHERE reservation_id = ?
              AND reservation_status = 'HELD'
            """;

    String releaseSeatSql = """
            UPDATE event_seats es
            SET seat_status = 'AVAILABLE'
            WHERE es.event_seat_id = ?
              AND es.seat_status = 'HELD'
              AND NOT EXISTS (
                  SELECT 1
                  FROM reservations r
                  WHERE r.event_seat_id = es.event_seat_id
                    AND r.reservation_status = 'HELD'
                    AND r.expires_at > NOW()
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM reservations r
                  WHERE r.event_seat_id = es.event_seat_id
                    AND r.reservation_status = 'CONFIRMED'
              )
            """;
    
    String expiryCheckSql = """
            SELECT expires_at <= NOW() AS is_expired
            FROM reservations
            WHERE reservation_id = ?
            """;

    String paymentSql = """
            UPDATE reservations
            SET payment_status = ?,
                reservation_status = ?
            WHERE reservation_id = ?
              AND reservation_status = 'HELD'
              AND payment_status = 'PENDING'
            """;

    String seatSql = """
            UPDATE event_seats
            SET seat_status = ?
            WHERE event_seat_id = ?
              AND seat_status = 'HELD'
            """;

    try (Connection con = DBConnection.getConnection()) {
        con.setAutoCommit(false);

        try {
            
            // Find and lock the reservation.

int eventSeatId;
// 1. Find and lock the reservation
try (PreparedStatement ps = con.prepareStatement(checkSql)) {
    ps.setInt(1, reservationId);

    try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) {
            con.rollback();
            return false;
        }

        eventSeatId = rs.getInt("event_seat_id");
    }
}

// 2. Lock the associated seat
try (PreparedStatement seatPs =
        con.prepareStatement(lockSeatSql)) {

    seatPs.setInt(1, eventSeatId);

    try (ResultSet seatRs = seatPs.executeQuery()) {
        if (!seatRs.next()
                || !"HELD".equals(seatRs.getString("seat_status"))) {
            con.rollback();
            return false;
        }
    }
}


            // Check whether the reservation has expired.
boolean isExpired;

try (PreparedStatement expiryPs =
        con.prepareStatement(expiryCheckSql)) {

    expiryPs.setInt(1, reservationId);

    try (ResultSet expiryRs = expiryPs.executeQuery()) {
        if (!expiryRs.next()) {
            con.rollback();
            return false;
        }

        isExpired = expiryRs.getBoolean("is_expired");
    }
}


if (isExpired) {

    // 1. Mark the reservation as EXPIRED
    try (PreparedStatement ps = con.prepareStatement(expireSql)) {
        ps.setInt(1, reservationId);

        if (ps.executeUpdate() != 1) {
            con.rollback();
            return false;
        }
    }

    // 2. Release the held seat
    try (PreparedStatement ps = con.prepareStatement(releaseSeatSql)) {
        ps.setInt(1, eventSeatId);
        ps.executeUpdate();
    }

    con.commit();
    return false;


}

// The reservation has not expired.
// Process payment normally.
String paymentStatus =
        paymentSuccessful ? "SUCCESS" : "FAILED";

String reservationStatus =
        paymentSuccessful ? "CONFIRMED" : "CANCELLED";

String seatStatus =
        paymentSuccessful ? "BOOKED" : "AVAILABLE";

// 3. Update payment and reservation
try (PreparedStatement ps = con.prepareStatement(paymentSql)) {
    ps.setString(1, paymentStatus);
    ps.setString(2, reservationStatus);
    ps.setInt(3, reservationId);

    if (ps.executeUpdate() != 1) {
        con.rollback();
        return false;
    }
}

// 4. Update the seat
try (PreparedStatement ps = con.prepareStatement(seatSql)) {
    ps.setString(1, seatStatus);
    ps.setInt(2, eventSeatId);

    if (ps.executeUpdate() != 1) {
        con.rollback();
        return false;
    }
}

con.commit();
return true;
        }

        catch (SQLException | RuntimeException e) {
            try {
                con.rollback();
            } catch (SQLException rollbackException) {
                e.addSuppressed(rollbackException);
            }
            throw e;
        }
    }
    
}



//5. Cancel an active reservation



public boolean cancelReservation(int userId, int reservationId)
        throws SQLException {

    if (userId <= 0 || reservationId <= 0) {
        return false;
    }

    String checkSql = """
            SELECT r.event_seat_id,
                   r.reservation_status,
                   r.payment_status,
                   (r.reservation_status = 'HELD'
                    AND r.payment_status = 'PENDING'
                    AND r.expires_at > NOW()) AS hold_valid,
                   (r.reservation_status = 'CONFIRMED'
                    AND r.payment_status = 'SUCCESS'
                    AND e.event_date > NOW()
                    AND e.event_status = 'SCHEDULED') AS confirmed_valid
            FROM reservations r
            JOIN event_seats es
                ON r.event_seat_id = es.event_seat_id
            JOIN events e
                ON es.event_id = e.event_id
            WHERE r.reservation_id = ?
              AND r.user_id = ?
            FOR UPDATE
            """;

    String lockSeatSql = """
            SELECT seat_status
            FROM event_seats
            WHERE event_seat_id = ?
            FOR UPDATE
            """;

    String cancelHeldSql = """
            UPDATE reservations
            SET reservation_status = 'CANCELLED'
            WHERE reservation_id = ?
              AND user_id = ?
              AND reservation_status = 'HELD'
              AND payment_status = 'PENDING'
            """;

    String cancelConfirmedSql = """
            UPDATE reservations
            SET reservation_status = 'CANCELLED',
                refund_status = 'PROCESSED'
            WHERE reservation_id = ?
              AND user_id = ?
              AND reservation_status = 'CONFIRMED'
              AND payment_status = 'SUCCESS'
            """;

    String releaseSeatSql = """
            UPDATE event_seats es
            SET seat_status = 'AVAILABLE'
            WHERE es.event_seat_id = ?
              AND es.seat_status IN ('HELD', 'BOOKED')
              AND NOT EXISTS (
                  SELECT 1
                  FROM reservations r
                  WHERE r.event_seat_id = es.event_seat_id
                    AND r.reservation_status = 'HELD'
                    AND r.expires_at > NOW()
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM reservations r
                  WHERE r.event_seat_id = es.event_seat_id
                    AND r.reservation_status = 'CONFIRMED'
              )
            """;

    try (Connection con = DBConnection.getConnection()) {
        con.setAutoCommit(false);

        try {
            int eventSeatId;
            String reservationStatus;
            boolean holdValid;
            boolean confirmedValid;

            // 1. Lock the reservation and verify ownership.
            try (PreparedStatement ps =
                    con.prepareStatement(checkSql)) {

                ps.setInt(1, reservationId);
                ps.setInt(2, userId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }

                    eventSeatId = rs.getInt("event_seat_id");
                    reservationStatus =
                            rs.getString("reservation_status");

                    holdValid = rs.getBoolean("hold_valid");
                    confirmedValid =
                            rs.getBoolean("confirmed_valid");
                }
            }

            // 2. Validate the reservation and its deadline.
            if (!holdValid && !confirmedValid) {
                con.rollback();
                return false;
            }

            // 3. Lock the associated seat.
            try (PreparedStatement ps =
                    con.prepareStatement(lockSeatSql)) {

                ps.setInt(1, eventSeatId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }

                    String seatStatus = rs.getString("seat_status");

                    if (holdValid && !"HELD".equals(seatStatus)) {
                        con.rollback();
                        return false;
                    }

                    if (confirmedValid
                            && !"BOOKED".equals(seatStatus)) {
                        con.rollback();
                        return false;
                    }
                }
            }

            // 4. Cancel the reservation.
            String cancelSql = holdValid
                    ? cancelHeldSql
                    : cancelConfirmedSql;

            try (PreparedStatement ps =
                    con.prepareStatement(cancelSql)) {

                ps.setInt(1, reservationId);
                ps.setInt(2, userId);

                if (ps.executeUpdate() != 1) {
                    con.rollback();
                    return false;
                }
            }

            // 5. Release the seat only if no active
            // reservation still requires it.
            try (PreparedStatement ps =
                    con.prepareStatement(releaseSeatSql)) {

                ps.setInt(1, eventSeatId);
                ps.executeUpdate();
            }

            con.commit();

            if (confirmedValid) {
                System.out.println(
                        "Booking cancelled. Simulated refund processed.");
            } else {
                System.out.println(
                        "Held reservation cancelled. Seat released if available.");
            }

            return true;

        } catch (SQLException | RuntimeException e) {
            try {
                con.rollback();
            } catch (SQLException rollbackException) {
                e.addSuppressed(rollbackException);
            }

            throw e;
        }
    }
}



 }

