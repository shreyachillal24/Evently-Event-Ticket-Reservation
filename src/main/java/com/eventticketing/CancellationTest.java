
package com.eventticketing;

import java.sql.SQLException;
import com.eventticketing.dao.ReservationDAO;

public class CancellationTest {

    public static void main(String[] args) throws SQLException {

        ReservationDAO reservationDAO = new ReservationDAO();

        // User ID 3, event seat ID 14
        int reservationId = reservationDAO.holdSeat(3, 11);

        System.out.println("New reservation ID: " + reservationId);

        if (reservationId > 0) {
            boolean cancelled =
                    reservationDAO.cancelReservation(3, reservationId);

            System.out.println(
                    "Held reservation cancelled: " + cancelled);
        }
    }
}
