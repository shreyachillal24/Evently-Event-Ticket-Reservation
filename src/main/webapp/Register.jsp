
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up | Evently</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .auth-container {
            max-width: 420px;
            margin: 55px auto;
            padding: 32px;
            background: #fff;
            border: 1px solid #e0e9f5;
            border-radius: 16px;
            box-shadow: 0 8px 25px rgba(21, 52, 92, .08);
        }
        .auth-container h1 { color: #15345c; margin-bottom: 10px; }
        .auth-container p { color: #63738b; margin-bottom: 24px; }
        .auth-container label {
            display: block;
            margin: 16px 0 7px;
            color: #15345c;
            font-weight: 600;
        }
        .auth-container input {
            box-sizing: border-box;
            width: 100%;
            padding: 12px;
            border: 1px solid #cbdcf5;
            border-radius: 8px;
            font-size: 15px;
        }
        .auth-container button {
            width: 100%;
            margin-top: 22px;
            padding: 13px;
            border: none;
            border-radius: 8px;
            background: #2878e8;
            color: white;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
        }
        .auth-error {
            color: #b42318;
            background: #fff0ee;
            padding: 10px;
            border-radius: 8px;
        }
        .auth-link { text-align: center; margin-top: 20px; }
    </style>
</head>
<body>
<header class="navbar">
    <a href="${pageContext.request.contextPath}/" class="logo">
        Event<span>ly</span>
    </a>
</header>

<main class="auth-container">
    <h1>Create Account</h1>
    <p>Sign up to book your favorite events.</p>

    <% if (request.getAttribute("error") != null) { %>
        <div class="auth-error">
            <%= request.getAttribute("error") %>
        </div>
    <% } %>

    <form action="${pageContext.request.contextPath}/register"
          method="post">
        <label for="fullName">Full Name</label>
        <input type="text" id="fullName" name="fullName"
               maxlength="100" autocomplete="name" required>

        <label for="email">Email Address</label>
        <input type="email" id="email" name="email"
               maxlength="150" autocomplete="email" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password"
               minlength="8" maxlength="72"
               autocomplete="new-password" required>

        <button type="submit">Create Account</button>
    </form>

    <div class="auth-link">
        Already have an account?
        <a href="${pageContext.request.contextPath}/login">Log In</a>
    </div>
</main>
</body>
</html>
