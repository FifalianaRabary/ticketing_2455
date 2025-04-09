<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Reservation" %>
<%@ page import="model.DetailReservation" %>
<%@ page import="model.Vol" %>
<%@ page import="model.TypeSiege" %>
<%@ page import="model.Utilisateur" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="myconnection.MyConnection" %>

<%
    List<DetailReservation> details = (List<DetailReservation>) request.getAttribute("details");
    DecimalFormat df = new DecimalFormat("#0.00");
    Utilisateur user = (Utilisateur) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des details de la reservation</title>

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
                <h2 class="my-4">Détails de la Réservation #<%= request.getParameter("id") %></h2>
                
                <!-- Affichage des messages -->
                <% if (request.getAttribute("success") != null) { %>
                    <div class="alert alert-success">
                        <%= request.getAttribute("success") %>
                    </div>
                <% } %>
                
                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>
    
                <table class="details-table">
                    <thead>
                        <tr>
                            <th>Nom</th>
                            <th>Prénom</th>
                            <th>Date de naissance</th>
                            <th>Type de siège</th>
                            <th>Passeport</th>
                            <th>Montant</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            try (Connection conn = MyConnection.getConnection()) {
                                int reservationId = Integer.parseInt(request.getParameter("id"));
                                Reservation reservation = Reservation.getById(conn, reservationId);
                                
                                for (DetailReservation detail : details) {
                                    TypeSiege typeSiege = TypeSiege.getById(conn, detail.getIdTypeSiege());
                        %>
                        <tr>
                            <td><%= detail.getNom() %></td>
                            <td><%= detail.getPrenom() %></td>
                            <td><%= detail.getDateNaissance() %></td>
                            <td><%= typeSiege != null ? typeSiege.getDesignation() : "Inconnu" %></td>
                            <td><%= detail.getPasseport() != null ? detail.getPasseport() : "Non renseigné" %></td>
                            <td><%= df.format(detail.getMontant()) %> €</td>
                            <td><a href="/ticketing/detail/delete?id=<%= detail.getId() %>">Supprimer</a></td>

                        </tr>
                        <%
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                        %>
                        <tr>
                            <td colspan="7" style="color: red;">Une erreur s'est produite lors du chargement des détails.</td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
    
                <div class="actions">
                    <a href="/ticketing/reservation/listReservation?id=<%= user != null ? user.getId() : "" %>" 
                       class="btn-back">
                        Retour à la liste
                    </a>
                    <a href="/ticketing/user/logout" class="logout">Logout</a>
                </div>
            </div>
        </main>
    </div>
</body>
</html>