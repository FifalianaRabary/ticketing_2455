<%@ page import="model.Utilisateur" %>
<%@ page import="session.MySession" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<!DOCTYPE html>
<html>
<head>
    <title>Dashboard Admin - CatchFlights</title>
    <style>
        /* Styles généraux */
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f7fc;
            display: flex;
            height: 100vh;
            flex-direction: column;
        }

        /* En-tête */
        header {
            background-color: #003366;
            color: white;
            padding: 1% 5%;
            font-size: 1.8rem;
            font-weight: bold;
        }

        /* Conteneur principal pour la page */
        .main-container {
            display: flex;
            flex: 1;
            height: 100%;
        }

        /* Navigation */
        .navigation {
            background-color: #003366;
            color: white;
            width: 20%;
            padding: 2% 5%;
            box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
            display: flex;
            flex-direction: column;
            justify-content: flex-start;
        }

        .navigation a {
            color: white;
            text-decoration: none;
            margin-bottom: 15px;
            font-size: 23px;
            transition: background-color 0.3s ease, color 0.3s ease;
            padding: 10px;
            border-radius: 5px;
        }

        .navigation a:hover {
            background-color: #0066cc;
            color: #e0f7ff;
        }

        /* Contenu principal */
        .content {
            width: 80%;
            padding: 2% 5%;
            background-color: #ffffff;
            overflow-y: auto;
        }

        .new-container {
            background-color: #ffffff;
            padding: 2%;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 51, 102, 0.1);
        }

        .my-4 {
            margin-top: 2%;
            margin-bottom: 2%;
            color: #003366;
        }

        h2 {
            font-size: 1.5rem;
        }

        .logout {
            display: inline-block;
            margin-top: 20px;
            padding: 10px;
            background-color: #ff3333;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        .logout:hover {
            background-color: #cc0000;
        }
    </style>
</head>
<body>
    <!-- En-tête du site -->
    <header>
        CatchFlights - BackOffice
    </header>

    <!-- Conteneur principal (flex) -->
    <div class="main-container">
        <!-- Navigation à gauche -->
        <div class="navigation">
            <a href="/ticketing/backOffice/dashboard">Tableau de bord</a>
            <a href="/ticketing/vol/formVol">Inserer un vol</a>
            <a href="/ticketing/vol/listVol">Liste des vols</a>
            <a href="/ticketing/promotion/formPromotion">Promotion vols</a>
            <a href="/ticketing/annulation/formAnnulation">Annulation reservation</a>
            <a href="/ticketing/regleReservation/formRegleReservation">Regle de reservation</a>
        </div>

        <!-- Contenu principal à droite -->
        <main class="content">
            <div class="new-container">
                <%
                    Utilisateur admin = (Utilisateur) session.getAttribute("user");
                    String role = (String) session.getAttribute("role");
                    
                    if (admin != null && role != null) {
                        System.out.println("HELL YEAH");
                %>
                <h2 class="my-4">Bienvenu dans le dashboard, <%= admin.getUsername() %> <%= role %> !</h2>
                <% 
                    } else {
                        out.println("<p>Error: Admin data is missing.</p>");
                    }
                %>

                <!-- Lien de déconnexion -->
                <a href="/ticketing/user/logout" class="logout">Logout</a>
            </div>
        </main>
    </div>
</body>
</html>
