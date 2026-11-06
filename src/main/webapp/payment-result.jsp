
<%@ page import="com.eventticketing.model.Reservation" %>

<%
    Reservation reservation =
        (Reservation) request.getAttribute("reservation");

    String paymentStatus =
        (String) request.getAttribute("paymentStatus");

    if (reservation == null) {
        response.sendRedirect(request.getContextPath() + "/events");
        return;
    }

    boolean success = "SUCCESS".equalsIgnoreCase(paymentStatus);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Result | Evently</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">

    <style>
        .result-card {
            max-width: 520px;
            margin: 70px auto;
            padding: 35px;
            text-align: center;
            background: white;
            border-radius: 14px;
            box-shadow: 0 4px 18px rgba(0, 0, 0, 0.08);
        }

        .result-icon {
            font-size: 54px;
            margin-bottom: 15px;
        }

        .result-card h1 {
            color: <%= success ? "#15803d" : "#b91c1c" %>;
        }

        .result-button {
            display: inline-block;
            margin-top: 20px;
            padding: 12px 22px;
            background: #2563eb;
            color: white;
            text-decoration: none;
            border-radius: 8px;
        }
    </style>
</head>

<body>

<main class="result-card">

    <div class="result-icon">
        <%= success ? "&#10004;" : "&#10006;" %>
    </div>

    <h1>
        <%= success ? "Booking Confirmed!" : "Payment Failed" %>
    </h1>

    <p>
        <%= success
            ? "Your reservation has been confirmed successfully."
            : "Your payment was not completed. Please check your reservation status before trying again."
        %>
    </p>

    <p>
        <strong>Reservation ID:</strong>
        <%= reservation.getReservationId() %>
    </p>

    <p>
        <strong>Payment Status:</strong>
        <%= reservation.getPaymentStatus() %>
    </p>

    <a class="result-button"
       href="<%= request.getContextPath() %>/events">
        Browse Events
    </a>

</main>

</body>
</html>
