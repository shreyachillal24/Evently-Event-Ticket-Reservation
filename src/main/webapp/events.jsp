
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.eventticketing.model.Event" %>

<%
    List<Event> events = (List<Event>) request.getAttribute("events");

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

    String userName = (String) session.getAttribute("userName");
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Explore Events | Evently</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">

    <style>
        .brand-container {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            flex-shrink: 0;
        }

        .welcome-message {
            margin-top: 4px;
            color: #15345c;
            font-size: 13px;
        }

        .welcome-message strong {
            color: #2878e8;
            font-weight: 700;
        }
    </style>
</head>

<body>

<header class="navbar">

    <div class="brand-container">

        <a href="<%= request.getContextPath() %>/" class="logo">
            Event<span>ly</span>
        </a>

        <% if (userName != null) { %>
            <div class="welcome-message">
                Welcome, <strong><%= userName %></strong>!
            </div>
        <% } %>

    </div>

    <nav class="nav-links">
        <a href="<%= request.getContextPath() %>/">Home</a>
        <a href="<%= request.getContextPath() %>/events">Explore Events</a>
    </nav>

    <div class="nav-actions">

        <% if (userName != null) { %>

            <a href="<%= request.getContextPath() %>/logout">
                Logout
            </a>

        <% } else { %>

            <a href="<%= request.getContextPath() %>/login">
                Login
            </a>

            <a href="<%= request.getContextPath() %>/register">
                Sign Up
            </a>

        <% } %>

    </div>

</header>

<section class="events-section">

    <div class="section-heading">

        <div>
            <span class="section-label">FIND YOUR EXPERIENCE</span>
            <h2>Upcoming Events</h2>
            <p>Choose your next experience and reserve your seat.</p>
        </div>

    </div>

    <div class="event-grid">

        <% if (events != null && !events.isEmpty()) {

            for (Event event : events) {

                if (!"SCHEDULED".equalsIgnoreCase(event.getEventStatus())) {
                    continue;
                }

        %>

        <article class="event-card">

            <div class="event-image concert-image">
                <span class="category-tag">EVENT</span>
                <span class="event-symbol">&#9733;</span>
            </div>

            <div class="event-details">

                <span class="event-date">
                    <%= dateFormat.format(event.getEventDate()) %>
                    &middot;
                    <%= timeFormat.format(event.getEventDate()) %>
                </span>

                <h3><%= event.getEventName() %></h3>

                <p class="event-location">
                    <%= event.getEventDescription() == null
                        ? "Discover this upcoming event."
                        : event.getEventDescription() %>
                </p>

                <div class="event-footer">

                    <div class="price">
                        <small>Ticket price</small>
                        <strong>&#8377;<%= event.getTicketPrice() %></strong>
                    </div>

                    <a class="btn btn-outline"
                       href="<%= request.getContextPath() %>/event-details?id=<%= event.getEventId() %>">
                        View Event
                    </a>

                </div>

            </div>

        </article>

        <% 
            }
        } else {
        %>

            <p>No events are available right now.</p>

        <% } %>

    </div>

</section>

<footer>

    <a href="<%= request.getContextPath() %>/" class="logo">
        Event<span>ly</span>
    </a>

    <p>Your next great experience starts here.</p>

</footer>

</body>
</html>
