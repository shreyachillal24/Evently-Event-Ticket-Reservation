
package com.eventticketing;

import com.eventticketing.dao.ReservationDAO;

public class ConcurrentBookingTest {

    public static void main(String[] args) throws InterruptedException {

        int eventSeatId = 17; // Choose an AVAILABLE seat
        int user1 = 3;
        int user2 = 4;

        Thread t1 = new Thread(() -> {
            try {
                ReservationDAO dao = new ReservationDAO();
                int result = dao.holdSeat(user1, eventSeatId);
                System.out.println("User 1 reservation ID: " + result);
            } catch (Exception e) {
                System.out.println("User 1 error: " + e.getMessage());
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                ReservationDAO dao = new ReservationDAO();
                int result = dao.holdSeat(user2, eventSeatId);
                System.out.println("User 2 reservation ID: " + result);
            } catch (Exception e) {
                System.out.println("User 2 error: " + e.getMessage());
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
