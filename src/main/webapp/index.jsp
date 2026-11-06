<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Evently | Discover Events</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    

<header class="navbar">

    <%
        String userName = (String) session.getAttribute("userName");
    %>

    <div class="brand-container">
        <a href="${pageContext.request.contextPath}/" class="logo">
            Event<span>ly</span>
        </a>

        <% if (userName != null) { %>
            <div class="welcome-message">
                Welcome, <strong><%= userName %></strong>!
            </div>
        <% } %>
    </div>

    <nav class="nav-links">
        <a href="#home">Home</a>
        <a href="#events">Explore Events</a>
    </nav>

    <div class="nav-actions">
        <% if (userName != null) { %>
            <a href="<%= request.getContextPath() %>/events">
                Browse Events
            </a>
            <a href="<%= request.getContextPath() %>/logout">
                Logout
            </a>
        <% } else { %>
            <a href="<%= request.getContextPath() %>/login">Login</a>
            <a href="<%= request.getContextPath() %>/register">Sign Up</a>
        <% } %>
    </div>

</header>


    <main>
        <section class="hero" id="home">
            <div class="hero-content">
                <span class="eyebrow">YOUR NEXT EXPERIENCE STARTS HERE</span>
                <h1>Discover Events.<br>Reserve Your <span>Seat.</span></h1>
                <p>
                    From unforgettable concerts to exciting shows,
                    find your next experience and book your seat with ease.
                </p>
                <a href="#events" class="btn btn-white">Explore Events &rarr;</a>

                <div class="hero-note">
                    <span class="status-dot"></span>
                    Simple booking. Secure reservations.
                </div>
            </div>

            <div class="hero-art">
                <div class="art-circle circle-one"></div>
                <div class="art-circle circle-two"></div>
                <div class="ticket-card">
                    <span class="ticket-label">YOUR NEXT EXPERIENCE</span>
                    <div class="ticket-icon">&#9835;</div>
                    <h2>Make It<br>Memorable.</h2>
                    <div class="ticket-line"></div>
                    <div class="ticket-bottom">
                        <span>LIVE EVENTS</span>
                        <span>EVENTLY</span>
                    </div>
                </div>
            </div>
        </section>

        <section class="events-section" id="events">
            <div class="section-heading">
                <div>
                    <span class="section-label">FIND YOUR EXPERIENCE</span>
                    <h2>Upcoming Events</h2>
                    <p>Choose an event and find your perfect seat.</p>
                </div>
                <span class="event-count">3 events</span>
            </div>

            <div class="event-grid">
                <article class="event-card">
                    <div class="event-image concert-image">
                        <span class="category-tag">MUSIC</span>
                        <span class="event-symbol">&#9835;</span>
                    </div>
                    <div class="event-details">
                        <span class="event-date">10 NOV 2026 · 6:00 PM</span>
                        <h3>Rock Concert</h3>
                        <p class="event-location">&#9679; Live music experience</p>
                        <div class="event-footer">
                            <div class="price"><small>Starting from</small><strong>View price</strong></div>
                            <a href="${pageContext.request.contextPath}/events" class="btn btn-outline">View Event</a>
                        </div>
                    </div>
                </article>

                <article class="event-card">
                    <div class="event-image comedy-image">
                        <span class="category-tag">COMEDY</span>
                        <span class="event-symbol">&#9786;</span>
                    </div>
                    <div class="event-details">
                        <span class="event-date">15 NOV 2026 · 7:00 PM</span>
                        <h3>Comedy Show</h3>
                        <p class="event-location">&#9679; An evening of laughter</p>
                        <div class="event-footer">
                            <div class="price"><small>Starting from</small><strong>View price</strong></div>
                            <a href="${pageContext.request.contextPath}/events" class="btn btn-outline">View Event</a>
                        </div>
                    </div>
                </article>

                <article class="event-card">
                    <div class="event-image cricket-image">
                        <span class="category-tag">SPORTS</span>
                        <span class="event-symbol">&#9918;</span>
                    </div>
                    <div class="event-details">
                        <span class="event-date">20 NOV 2026 · 3:00 PM</span>
                        <h3>Cricket Tournament</h3>
                        <p class="event-location">&#9679; Live sporting action</p>
                        <div class="event-footer">
                            <div class="price"><small>Starting from</small><strong>View price</strong></div>
                            <a href="${pageContext.request.contextPath}/events" class="btn btn-outline">View Event</a>
                        </div>
                    </div>
                </article>
            </div>
        </section>
    </main>

    <footer>
        <a href="${pageContext.request.contextPath}/" class="logo">Event<span>ly</span></a>
        <p>Your next great experience starts here.</p>
        <span>&copy; 2026 Evently. Built for unforgettable experiences.</span>
    </footer>

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
