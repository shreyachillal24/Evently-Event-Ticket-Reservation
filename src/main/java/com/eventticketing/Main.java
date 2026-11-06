
package com.eventticketing;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.eventticketing.dao.EventDAO;
import com.eventticketing.dao.ReservationDAO;
import com.eventticketing.dao.SeatDAO;
import com.eventticketing.model.Event;
import com.eventticketing.model.Reservation;
import com.eventticketing.model.Seat;
import com.eventticketing.model.User;
import com.eventticketing.service.UserService;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    private static final UserService userService = new UserService();
    private static final EventDAO eventDAO = new EventDAO();
    private static final SeatDAO seatDAO = new SeatDAO();
    private static final ReservationDAO reservationDAO =
            new ReservationDAO();

    private static User loggedInUser;

    public static void main(String[] args) {

        boolean running = true;

        while (running) {
            try {
                System.out.println("\n==================================");
                System.out.println("       EVENT TICKETING SYSTEM");
                System.out.println("==================================");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Browse Events");
                System.out.println("4. View Available Seats");
                System.out.println("5. Reserve Seat and Simulate Payment");
                System.out.println("6. Cancel My Reservation");
                System.out.println("7. Logout");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");

                int choice = Integer.parseInt(sc.nextLine().trim());

                switch (choice) {
                    case 1 -> register();
                    case 2 -> login();
                    case 3 -> browseEvents();
                    case 4 -> viewAvailableSeats();
                    case 5 -> reserveSeat();
                    case 6 -> cancelReservation();
                    case 7 -> logout();
                    case 0 -> {
                        running = false;
                        System.out.println("Thank you for using Event Ticketing!");
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (SQLException e) {
                System.out.println("Database operation failed: "
                        + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Input error: " + e.getMessage());
            }
        }

        sc.close();
    }

    // 1. Register
    private static void register() throws SQLException {

        System.out.println("\n--- User Registration ---");

        System.out.print("Enter full name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        int userId = userService.register(name, email, password);

        if (userId > 0) {
            System.out.println("Registration successful!");
            System.out.println("Your User ID: " + userId);
        } else {
            System.out.println("Registration could not be completed.");
        }
    }

    // 2. Login
    private static void login() throws SQLException {

        if (loggedInUser != null) {
            System.out.println("You are already logged in as "
                    + loggedInUser.getFullName());
            return;
        }

        System.out.println("\n--- User Login ---");

        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        User user = userService.login(email, password);

        if (user != null) {
            loggedInUser = user;
            System.out.println("Login successful!");
            System.out.println("Welcome, " + user.getFullName() + "!");
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    // 3. Browse events
    private static void browseEvents() throws SQLException {

        System.out.println("\n--- Available Events ---");

        List<Event> events = eventDAO.getAllEvents();

        if (events.isEmpty()) {
            System.out.println("No events found.");
            return;
        }

        for (Event event : events) {
            System.out.println("------------------------------");
            System.out.println("Event ID: " + event.getEventId());
            System.out.println("Name: " + event.getEventName());
            System.out.println("Ticket Price: " + event.getTicketPrice());
        }
    }

    // 4. View available seats
    private static void viewAvailableSeats() throws SQLException {

        System.out.print("Enter Event ID: ");
        int eventId = readPositiveInt();

        List<Seat> seats = seatDAO.getAvailableSeats(eventId);

        if (seats.isEmpty()) {
            System.out.println("No available seats found for this event.");
            return;
        }

        System.out.println("\n--- Available Seats ---");

        for (Seat seat : seats) {

System.out.println(
    "Event Seat ID: " + seat.getEventSeatId()
    + " | Seat: " + seat.getSeatNumber()
    + " | Type: " + seat.getSeatType()
    + " | Status: " + seat.getSeatStatus()
);

        }

        System.out.println(
                "Use the corresponding event_seat_id when booking.");
    }

    // 5. Reserve a seat and simulate payment
    private static void reserveSeat() throws SQLException {

        if (loggedInUser == null) {
            System.out.println("Please log in before reserving a seat.");
            return;
        }

        System.out.print("Enter Event Seat ID: ");
        int eventSeatId = readPositiveInt();

        int reservationId = reservationDAO.holdSeat(
                loggedInUser.getUserId(), eventSeatId);

        if (reservationId <= 0) {
            System.out.println(
                    "Reservation failed. The seat may be unavailable.");
            return;
        }

        System.out.println("Seat held successfully!");
        System.out.println("Reservation ID: " + reservationId);

        System.out.print(
                "Simulate successful payment? Enter Y or N: ");

        String answer = sc.nextLine().trim();

        if (!answer.equalsIgnoreCase("Y")
                && !answer.equalsIgnoreCase("N")) {
            System.out.println(
                    "Invalid choice. Your seat remains held temporarily.");
            return;
        }

        boolean paymentSuccessful = answer.equalsIgnoreCase("Y");

        boolean result = reservationDAO.processPayment(
                reservationId, paymentSuccessful);

        Reservation reservation =
                reservationDAO.getReservationById(reservationId);

        if (reservation != null) {
            System.out.println("Reservation Status: "
                    + reservation.getReservationStatus());
            System.out.println("Payment Status: "
                    + reservation.getPaymentStatus());
        }

        if (result) {
            System.out.println("Payment processed successfully.");
        } else {
            System.out.println(
                    "Payment was not completed, or the reservation expired.");
        }
    }

    // 6. Cancel the logged-in user's reservation
    private static void cancelReservation() throws SQLException {

        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.print("Enter your Reservation ID: ");
        int reservationId = readPositiveInt();

        boolean cancelled = reservationDAO.cancelReservation(
                loggedInUser.getUserId(), reservationId);

        if (cancelled) {
            System.out.println("Reservation cancelled successfully.");
        } else {
            System.out.println(
                    "Cancellation failed. Check ownership, status, and expiry.");
        }
    }

    // 7. Logout
    private static void logout() {

        if (loggedInUser == null) {
            System.out.println("No user is currently logged in.");
            return;
        }

        System.out.println("Logged out: " + loggedInUser.getFullName());
        loggedInUser = null;
    }

    // Read and validate a positive integer
    private static int readPositiveInt() {

        int value = Integer.parseInt(sc.nextLine().trim());

        if (value <= 0) {
            throw new IllegalArgumentException(
                    "ID must be greater than zero.");
        }

        return value;
    }
}
