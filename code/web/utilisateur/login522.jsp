<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <h1>Login</h1>
    <form action="/ticketing/utilisateur/checkLogin" method="post">
        Username: <input type="text" name="utilisateur.username" value="john" required><br>
        Password: <input type="password" name="utilisateur.password" value="aaa"required><br>
        <input type="submit" value="Login">
    </form>
    <%
        if (request.getParameter("error") != null) {
            out.println("<p style='color:red;'>Invalid credentials</p>");
        }
    %>
</body>
</html>
