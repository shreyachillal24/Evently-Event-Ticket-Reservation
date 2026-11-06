
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String error = (String) request.getAttribute("error");
    if (error == null) {
        error = "Your seat could not be reserved. Please try again.";
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reservation Error | Evently</title>
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <main style="max-width:600px;margin:60px auto;padding:24px;text-align:center">
        <h1>Seat Reservation Unsuccessful</h1>
        <p><%= error.replace("&", "&amp;")
                     .replace("<", "&lt;")
                     .replace(">", "&gt;")
                     .replace("\"", "&quot;")
                     .replace("'", "&#39;") %></p>
        <a href="<%= request.getContextPath() %>/events">
            Browse Events
        </a>
    </main>
</body>
</html>
