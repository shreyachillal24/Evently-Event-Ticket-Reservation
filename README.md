# Event Ticket & Seat Reservation Engine

A Java-based event ticket reservation system built using JDBC and MySQL. The project manages seat reservations, simulated payments, reservation expiry, cancellation, and concurrent booking attempts.

## Features

- Retrieve reservation details by reservation ID
- Hold an available seat for a limited period
- Process simulated successful and failed payments
- Expire unpaid reservations and release seats
- Cancel an eligible reservation with ownership validation
- Prevent duplicate seat bookings during concurrent booking attempts
- Use database transactions, row locking, commit, and rollback
- Validate reservation, user, and event-seat IDs

## Technologies Used

- Java 21
- Apache Maven
- JDBC
- MySQL 8
- MySQL Connector/J 9.4.0
- Eclipse IDE

## Project Structure

```text
event-ticketing-jdbc/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/eventticketing/
│   │   │       ├── dao/
│   │   │       ├── model/
│   │   │       ├── service/
│   │   │       ├── util/
│   │   │       └── Main.java
│   │   └── resources/
│   └── test/
├── pom.xml
└── README.md
```

## Prerequisites

- JDK 21
- MySQL Server 8
- Maven support in Eclipse or a Maven installation

## Database Configuration

1. Create a MySQL database named `event_ticketing`.
2. Create and populate the tables required by the application.
3. Configure the database connection using environment variables.

| Environment variable | Example value |
|---|---|
| `EVENT_DB_URL` | `jdbc:mysql://localhost:3306/event_ticketing` |
| `EVENT_DB_USERNAME` | Your MySQL username |
| `EVENT_DB_PASSWORD` | Your MySQL password |

Do not commit real database credentials to version control.

## Run the Project

1. Import the project into Eclipse as an existing Maven project.
2. Confirm that JDK 21 is configured.
3. Configure the database and environment variables.
4. Allow Maven dependencies to resolve.
5. Run `Main.java` as a Java Application.

## Reservation Workflow

1. Check whether the requested event seat is available.
2. Create a temporary reservation with status `HELD`.
3. Set the payment status to `PENDING`.
4. Process payment before the reservation expires.
5. On successful payment, confirm the reservation and mark the seat `BOOKED`.
6. On failed payment, cancel the reservation and release the seat.
7. Expire unpaid holds and release eligible seats.

## Test Scenarios

The following scenarios have been exercised during development:

- Hold an available seat
- Process successful payment
- Process failed payment
- Cancel an eligible reservation
- Reject cancellation by a different user
- Reject cancellation of a confirmed reservation
- Expire a reservation and release its seat
- Reject a second booking for an already-held seat
- Test simultaneous attempts to reserve the same seat
- Verify transaction rollback after a simulated failure

## Future Improvements

- Add a user-facing interface
- Integrate a real payment gateway
- Add automated unit and integration tests
- Add structured logging and improved error reporting
- Add deployment configuration

## Author

Developed as a Java, JDBC, and MySQL learning project.
