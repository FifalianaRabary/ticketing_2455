<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <h1>Login</h1>
    <form action="/ticketing/user/checkLogin" method="post">
        Email: <input type="text" name="user.username" required value="fifaliana"><br>
        Password: <input type="password" name="user.mdp" required value="admin"><br>
        <input type="submit" value="Login">
    </form>
    
</body>
</html>
