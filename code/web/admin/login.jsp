<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <h1>Login</h1>
    <form action="/ticketing/admin/checkLogin" method="post">
        Email: <input type="text" name="admin.username" required value="admin"><br>
        Password: <input type="password" name="admin.mdp" required value="admin"><br>
        <input type="submit" value="Login">
    </form>
    
</body>
</html>
