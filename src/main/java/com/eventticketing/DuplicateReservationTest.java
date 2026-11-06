
package com.eventticketing;

import java.sql.SQLException;
import com.eventticketing.dao.ReservationDAO;

public class DuplicateReservationTest {

    public static void main(String[] args) throws SQLException {

        ReservationDAO dao = new ReservationDAO();

        int userId = 3;
        int eventSeatId = 22; // Replace with an AVAILABLE seat ID

        // First reservation
        int firstId = dao.holdSeat(userId, eventSeatId);
        System.out.println("First reservation ID: " + firstId);

        if (firstId <= 0) {
            System.out.println("Test stopped: seat is not available.");
            return;
        }

        // Try reserving the same seat again
        int secondId = dao.holdSeat(userId, eventSeatId);
        System.out.println("Second reservation ID: " + secondId);

        if (secondId == -1) {
            System.out.println("PASS: Duplicate active reservation rejected.");
        } else {
            System.out.println("CHECK: Second reservation unexpectedly succeeded.");
        }

        // Cleanup: cancel the first held reservation
        boolean cancelled = dao.cancelReservation(userId, firstId);
        System.out.println("First reservation cancelled: " + cancelled);
    }
}
