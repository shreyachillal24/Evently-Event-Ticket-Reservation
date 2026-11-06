
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.eventticketing.model.Event" %>
<%@ page import="com.eventticketing.model.Seat" %>
<%@ page import="com.eventticketing.model.Reservation" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.Timestamp" %>

<%
    Event event = (Event) request.getAttribute("event");
    Seat seat = (Seat) request.getAttribute("seat");
    Reservation reservation =
        (Reservation) request.getAttribute("reservation");

    if (session.getAttribute("userId") == null) {
        response.sendRedirect(
            request.getContextPath() + "/login"
        );
        return;
    }

    if (event == null || seat == null || reservation == null) {
        response.sendRedirect(
            request.getContextPath() + "/events"
        );
        return;
    }

    SimpleDateFormat dateFormat =
        new SimpleDateFormat("dd MMM yyyy, hh:mm a");

    Timestamp expiry = reservation.getExpiresAt();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Booking Summary | Evently</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">

    <style>
        .booking-container {
            max-width: 650px;
            margin: 50px auto;
            padding: 30px;
            background: #fff;
            border-radius: 14px;
            box-shadow: 0 4px 18px rgba(0, 0, 0, 0.08);
        }

        .booking-container h1 {
            color: #173b70;
            margin-bottom: 8px;
        }

        .booking-details {
            margin: 25px 0;
            border-top: 1px solid #e5e7eb;
        }

        .detail-row {
            display: flex;
            justify-content: space-between;
            gap: 15px;
            padding: 14px 0;
            border-bottom: 1px solid #e5e7eb;
        }

        .detail-label {
            color: #64748b;
        }

        .detail-value {
            font-weight: 600;
            text-align: right;
        }

        .expiry-notice {
            padding: 14px;
            background: #fff7ed;
            color: #9a3412;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .booking-button {
            display: block;
            width: 100%;
            padding: 14px;
            background: #2563eb;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
        }

        .booking-button:hover {
            background: #1d4ed8;
        }

        .back-link {
            display: block;
            text-align: center;
            margin-top: 18px;
            color: #2563eb;
            text-decoration: none;
        }
    </style>
</head>

<body>

<main class="booking-container">

    <h1>Booking Summary</h1>
    <p>Review your seat reservation before proceeding.</p>

    <div class="booking-details">

        <div class="detail-row">
            <span class="detail-label">Event</span>
            <span class="detail-value">
                <%= event.getEventName() %>
            </span>
        </div>

        <div class="detail-row">
            <span class="detail-label">Date & Time</span>
            <span class="detail-value">
                <%= dateFormat.format(event.getEventDate()) %>
            </span>
        </div>

        <div class="detail-row">
            <span class="detail-label">Seat Number</span>
            <span class="detail-value">
                <%= seat.getSeatNumber() %>
            </span>
        </div>

        <div class="detail-row">
            <span class="detail-label">Seat Type</span>
            <span class="detail-value">
                <%= seat.getSeatType() %>
            </span>
        </div>

        <div class="detail-row">
            <span class="detail-label">Ticket Price</span>
            <span class="detail-value">
                &#8377;<%= event.getTicketPrice() %>
            </span>
        </div>

        <div class="detail-row">
            <span class="detail-label">Reservation Status</span>
            <span class="detail-value">
                <%= reservation.getReservationStatus() %>
            </span>
        </div>

        <div class="detail-row">
            <span class="detail-label">Payment Status</span>
            <span class="detail-value">
                <%= reservation.getPaymentStatus() %>
            </span>
        </div>

    </div>

    <div class="expiry-notice">
        <strong>Seat hold expires:</strong>
        <%= expiry == null ? "Not available" : dateFormat.format(expiry) %>
        <br>
        Complete the booking before the hold expires.
    </div>

    <form action="<%= request.getContextPath() %>/payment"
          method="post">

        <input type="hidden"
               name="reservationId"
               value="<%= reservation.getReservationId() %>">

        <button type="submit" class="booking-button">
            Proceed to Payment
        </button>

    </form>

    <a class="back-link"
       href="<%= request.getContextPath() %>/events">
        Back to Events
    </a>

</main>

</body>
</html>
