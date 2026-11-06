
package com.eventticketing;

import java.sql.SQLException;
import com.eventticketing.dao.ReservationDAO;

public class ExpiredHoldTest {

    public static void main(String[] args) throws SQLException {

        ReservationDAO dao = new ReservationDAO();

        int userId = 2;
        int eventSeatId = 18; // Replace with an AVAILABLE event_seat_id

        int reservationId = dao.holdSeat(userId, eventSeatId);

        if (reservationId <= 0) {
            System.out.println("Seat unavailable. Choose an AVAILABLE seat.");
            return;
        }

        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Seat held successfully.");

        System.out.println("Expire this reservation in MySQL,");
        System.out.println("then return here and run releaseExpiredReservations().");

        System.out.println("Waiting for you to update MySQL...");
        try {
            System.in.read();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        int releasedCount = dao.releaseExpiredReservations();

        System.out.println("Seats released: " + releasedCount);
        System.out.println("Expected: 1");
    }
}
