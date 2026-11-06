
package com.eventticketing;

import java.sql.SQLException;
import com.eventticketing.dao.ReservationDAO;

public class SeatReuseTest {

    public static void main(String[] args) throws SQLException {

        ReservationDAO dao = new ReservationDAO();

        int user1 = 3;
        int user2 = 4;
        int eventSeatId = 18; // Use the seat from your previous test

        // User 1 reserves the seat
        int firstId = dao.holdSeat(user1, eventSeatId);
        System.out.println("User 1 reservation ID: " + firstId);

        if (firstId <= 0) {
            System.out.println("Test stopped: seat is not available.");
            return;
        }

        // User 1 cancels the reservation
        boolean cancelled = dao.cancelReservation(user1, firstId);
        System.out.println("User 1 cancelled: " + cancelled);

        if (!cancelled) {
            System.out.println("Test stopped: cancellation failed.");
            return;
        }

        // User 2 tries to reserve the same seat
        int secondId = dao.holdSeat(user2, eventSeatId);
        System.out.println("User 2 reservation ID: " + secondId);

        if (secondId > 0) {
            System.out.println("PASS: Another user can reserve the released seat.");
            dao.cancelReservation(user2, secondId);
        } else {
            System.out.println("FAIL: Another user could not reserve the released seat.");
        }
    }
}
