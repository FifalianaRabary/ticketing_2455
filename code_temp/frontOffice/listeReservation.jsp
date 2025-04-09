<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Reservation" %>
<%@ page import="model.Vol" %>
<%@ page import="model.Utilisateur" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="myconnection.MyConnection" %>

<%
    List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
    DecimalFormat df = new DecimalFormat("#0.00");
    Utilisateur user = (Utilisateur) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Réservations</title>

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

        /* Style du tableau */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 2rem;
        }

        table th, table td {
            padding: 12px;
            text-align: left;
            border: 1px solid #ddd;
        }

        table th {
            background-color: #003366;
            color: white;
        }

        table tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        table tr:hover {
            background-color: #f1f1f1;
        }

        table a {
            color: #003366;
            text-decoration: none;
            transition: color 0.3s ease;
        }

        table a:hover {
            color: #0066cc;
        }
    </style>
</head>
<body>

    <!-- En-tête du site -->
    <header>
        CatchFlights - FrontOffice
    </header>

    <div class="main-container">
        <div class="navigation">
            <%
                if (user != null) {
            %>
            <a href="/ticketing/frontOffice/dashboard">Tableau de bord</a>
            <a href="/ticketing/vol/listVolFront">Liste des vols</a>
            <a href="/ticketing/reservation/formReservation">Faire une réservation</a>
            <a href="/ticketing/reservation/listReservation?id=<%= user.getId() %>">Voir mes réservations</a>
            <a href="/ticketing/user/upload">Upload photo passeport</a>
            <% } else { %>
            <p>Erreur : utilisateur non connecté.</p>
            <% } %>
        </div>

        <main class="content">
            <div class="new-container">
                <h2 class="my-4">Liste des Réservations</h2>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Vol</th>
                            <th>Date et Heure de Réservation</th>
                            <th>Montant</th>
                            <th>Details</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            try (Connection conn = MyConnection.getConnection()) {
                                if (reservations != null && user != null) {
                                    int id_user = user.getId();
                                    for (Reservation reservation : reservations) { 
                                        Vol vol = Vol.getById(conn, reservation.getIdVol());
                        %>
                        <tr>
                            <td><%= reservation.getId() %></td>
                            <td><%= vol != null ? vol.getDesignation() : "Vol inconnu" %></td>
                            <td><%= reservation.getDateHeureReservation() %></td>
                            <td><%= df.format(reservation.getMontant(conn,reservation.getId())) %> €</td>
                            <td><a href="/ticketing/reservation/voirDetail?id=<%= reservation.getId() %>">Voir detail</a></td>
                            <td><a href="/ticketing/reservation/annulation?id=<%= reservation.getId() %>&id_user=<%= id_user %>">Annuler</a></td>
                        </tr>
                        <%
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                        %>
                        <tr>
                            <td colspan="5" style="color: red;">Une erreur s'est produite lors du chargement des réservations.</td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>

                <a href="/ticketing/user/logout" class="logout">Logout</a>
            </div>
        </main>
    </div>
</body>
</html>