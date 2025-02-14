<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>utilisateur Grades</title>
    <!-- Inclure ici vos ressources CSS et JavaScript nécessaires -->
    <style>
        /* Ajoutez votre CSS pour styliser le tableau ici */
        table {
            width: 50%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
    <h1>utilisateur Grades</h1>
    
    <%@ page import="controller.Utilisateur" %>
    <%@ page import="session.MySession" %>
    <%@ page import="java.util.HashMap" %>
    <%@ page import="jakarta.servlet.http.HttpSession" %>
    
    <%
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur"); // ✅ Bon type

        if(utilisateur!=null){
            System.out.println("HELL YEAH");%>
       <h1><%= utilisateur.getMail() %></h1>

            
        <% } else {
            out.println("<p>Error: Session data is missing.</p>");
        }
    %>
    
    <%-- Inclure ici d'autres éléments de l'interface utilisateur si nécessaire --%>

    <form action="/projet_framework_depl/utilisateur/logout" method="post">
        <input type="submit" value="logout">
    </form>
</body>
</html>
