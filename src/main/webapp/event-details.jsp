
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.eventticketing.model.Event" %>
<%@ page import="com.eventticketing.model.Seat" %>

<%
    Event event = (Event) request.getAttribute("event");
    List<Seat> seats = (List<Seat>) request.getAttribute("seats");
    boolean isLoggedIn = session.getAttribute("userId") != null;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= event.getEventName() %> | Evently</title>
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .details-container { max-width: 1050px; margin: 45px auto; padding: 0 24px; }
        .event-banner { background: #edf5ff; border-radius: 16px; padding: 32px; margin-bottom: 32px; }
        .event-banner h1 { font-size: 34px; color: #15345c; margin: 8px 0; }
        .event-banner p { color: #63738b; margin-top: 8px; }
        .event-meta { display: flex; gap: 24px; flex-wrap: wrap; margin-top: 18px; color: #245da6; font-weight: 600; }
        .seat-heading { margin-bottom: 20px; }
        .seat-heading h2 { color: #172f50; }
        .seat-legend { display: flex; gap: 18px; flex-wrap: wrap; margin: 16px 0 24px; font-size: 13px; color: #63738b; }
        .legend-item { display: flex; align-items: center; gap: 7px; }
        .seat-dot { width: 13px; height: 13px; border-radius: 4px; display: inline-block; }
        .available-dot { background: #d9eaff; border: 1px solid #2878e8; }
        .held-dot { background: #ffe7b0; border: 1px solid #d49a20; }
        .booked-dot { background: #d9dee7; border: 1px solid #a4afbf; }
        .seat-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(100px, 1fr)); gap: 12px; }
        .seat { border: 1px solid #cbdcf5; border-radius: 9px; padding: 14px 6px; text-align: center; background: #edf5ff; color: #245da6; }
        .seat strong, .seat small { display: block; }
        .seat small { margin-top: 4px; font-size: 11px; }
        .seat.held { background: #fff3d6; border-color: #e7b64c; color: #805900; }
        .seat.booked { background: #edf0f5; border-color: #cbd1dc; color: #7b8494; }
        .seat-status { font-size: 10px; text-transform: uppercase; letter-spacing: .4px; }
        .back-link { display: inline-block; margin-bottom: 20px; color: #2878e8; }
        @media (max-width: 600px) { .event-banner { padding: 22px; } .event-banner h1 { font-size: 27px; } }
        
        
/* Available seat interaction */
.seat.available {
    cursor: pointer;
    transition: transform 0.2s, box-shadow 0.2s;
}

.seat.available:hover {
    transform: translateY(-3px);
    box-shadow: 0 5px 14px rgba(40, 120, 232, 0.18);
}

/* Login/signup popup */
.login-popup-overlay {
    display: none;
    position: fixed;
    inset: 0;
    background: rgba(15, 35, 65, 0.55);
    z-index: 1000;
    justify-content: center;
    align-items: center;
    padding: 20px;
}

.login-popup-overlay.show {
    display: flex;
}

.login-popup {
    position: relative;
    width: 100%;
    max-width: 420px;
    box-sizing: border-box;
    background: #fff;
    border-radius: 16px;
    padding: 32px 26px;
    text-align: center;
    box-shadow: 0 15px 45px rgba(0, 0, 0, 0.2);
}

.login-popup h2 {
    color: #15345c;
    margin-top: 0;
}

.login-popup p {
    color: #63738b;
    line-height: 1.6;
}

.popup-button {
    display: block;
    width: 100%;
    box-sizing: border-box;
    padding: 12px;
    margin-top: 10px;
    border-radius: 8px;
    text-decoration: none;
    font-weight: 600;
    font-size: 15px;
    cursor: pointer;
}

.login-button {
    background: #2878e8;
    color: white;
    border: 1px solid #2878e8;
}

.signup-button {
    background: white;
    color: #2878e8;
    border: 1px solid #2878e8;
}

.back-button {
    background: #f1f5f9;
    color: #475569;
    border: none;
}

.popup-close {
    position: absolute;
    top: 10px;
    right: 14px;
    border: none;
    background: transparent;
    font-size: 26px;
    cursor: pointer;
    color: #63738b;
}
        
    </style>
</head>
<body>

<header class="navbar">
    <a href="<%= request.getContextPath() %>/" class="logo">
        Event<span>ly</span>
    </a>
    <nav class="nav-links">
        <a href="<%= request.getContextPath() %>/">Home</a>
        <a href="<%= request.getContextPath() %>/events">Explore Events</a>
    </nav>
</header>

<main class="details-container">
    <a class="back-link" href="<%= request.getContextPath() %>/events">
        &larr; Back to events
    </a>

    <section class="event-banner">
        <span class="section-label">EVENT DETAILS</span>
        <h1><%= event.getEventName() %></h1>

        <p>
            <%= event.getEventDescription() == null
                ? "Discover this upcoming event."
                : event.getEventDescription() %>
        </p>

        <div class="event-meta">
            <span>
                &#128197;
                <%= dateFormat.format(event.getEventDate()) %>
            </span>
            <span>
                &#128336;
                <%= timeFormat.format(event.getEventDate()) %>
            </span>
            <span>
                Ticket price: &#8377;<%= event.getTicketPrice() %>
            </span>
        </div>
    </section>

    <section>
        <div class="seat-heading">
            <h2>Select Your Seat</h2>
            <p>Review the current seat availability below.</p>
        </div>

        <div class="seat-legend">
            <span class="legend-item">
                <span class="seat-dot available-dot"></span> Available
            </span>
            <span class="legend-item">
                <span class="seat-dot held-dot"></span> Held
            </span>
            <span class="legend-item">
                <span class="seat-dot booked-dot"></span> Booked
            </span>
        </div>

        <div class="seat-grid">
            <% if (seats != null && !seats.isEmpty()) {
                   for (Seat seat : seats) {
                       String status = seat.getSeatStatus();
                       String cssClass = "available";

                       if ("HELD".equalsIgnoreCase(status)) {
                           cssClass = "held";
                       } else if ("BOOKED".equalsIgnoreCase(status)) {
                           cssClass = "booked";
                       }
            %>

            
<% if ("AVAILABLE".equalsIgnoreCase(status)) { %>

    <button type="button"
            class="seat available"
            onclick="<%= isLoggedIn
                ? "selectSeat(" + seat.getEventSeatId() + ", '"
                    + seat.getSeatNumber() + "')"
                : "showLoginPopup('" + seat.getSeatNumber() + "')"
            %>">

        <strong><%= seat.getSeatNumber() %></strong>
        <small><%= seat.getSeatType() %></small>
        <small class="seat-status"><%= status %></small>
    </button>

<% } else { %>

    <div class="seat <%= cssClass %>">
        <strong><%= seat.getSeatNumber() %></strong>
        <small><%= seat.getSeatType() %></small>
        <small class="seat-status"><%= status %></small>
    </div>

<% } %>


            <%     }
               } else {
            %>
                <p>No seats have been configured for this event yet.</p>
            <% } %>
        </div>
    </section>
    
    
<div id="loginPopupOverlay"
     class="login-popup-overlay"
     role="dialog"
     aria-modal="true"
     aria-labelledby="loginPopupTitle">

    <div class="login-popup">
        <button type="button"
                class="popup-close"
                onclick="closeLoginPopup()"
                aria-label="Close popup">&times;</button>

        <h2 id="loginPopupTitle">Login Required</h2>

        <p id="loginPopupMessage">
            Please log in or sign up to book this seat.
        </p>

        <a id="popupLoginLink"
           class="popup-button login-button"
           href="<%= request.getContextPath() %>/login">
            Log In
        </a>

        <a id="popupSignupLink"
           class="popup-button signup-button"
           href="<%= request.getContextPath() %>/register">
            Sign Up
        </a>

        <button type="button"
                class="popup-button back-button"
                onclick="closeLoginPopup()">
            Go Back
        </button>
    </div>
</div>
    
</main>

<footer>
    <a href="<%= request.getContextPath() %>/" class="logo">
        Event<span>ly</span>
    </a>
    <p>Your next great experience starts here.</p>
</footer>

<script src="<%= request.getContextPath() %>/js/script.js"></script>
</body>
</html>
