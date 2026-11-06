
package com.eventticketing;

import java.sql.SQLException;
import java.util.Scanner;
import com.eventticketing.dao.ReservationDAO;

public class ExpiredPaymentTest {

    public static void main(String[] args) throws SQLException {

        ReservationDAO dao = new ReservationDAO();
        Scanner sc = new Scanner(System.in);

        int userId = 3;
        int eventSeatId = 15;

        int reservationId = dao.holdSeat(userId, eventSeatId);

        if (reservationId <= 0) {
            System.out.println("Seat unavailable. Choose another seat.");
            sc.close();
            return;
        }

        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Seat held successfully.");

        System.out.println(
            "Expire this reservation in MySQL, then type GO and press Enter."
        );

        String input = sc.nextLine();

        if (!input.equalsIgnoreCase("GO")) {
            System.out.println("Test stopped.");
            sc.close();
            return;
        }

        boolean result = dao.processPayment(reservationId, true);

        System.out.println("Payment processed: " + result);
        System.out.println("Expected: false");

        sc.close();
    }
}
