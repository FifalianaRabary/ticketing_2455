<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <h1>Login</h1>
    <form action="/ticketing/utilisateur/checkLogin" method="post">
        Email: <input type="text" name="utilisateur.mail" required value="fifaliana@gmail.com"><br>
        Password: <input type="password" name="utilisateur.mdp" required value="aaa"><br>
        <input type="submit" value="Login">
    </form>
    
</body>
</html>
