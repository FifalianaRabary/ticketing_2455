<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Vol" %>
<%@ page import="model.Avion" %>
<%@ page import="model.Ville" %>
<%@ page import="myconnection.MyConnection" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.sql.Connection" %>

<%
    List<Vol> vols = (List<Vol>) request.getAttribute("vols");
    List<Ville> villes = (List<Ville>) request.getAttribute("villes");
    List<Avion> avions = (List<Avion>) request.getAttribute("avions");
    DecimalFormat df = new DecimalFormat("#0.00");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Vols</title>
    <style>
        /* Styles generaux */
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
            overflow-y: auto;
            max-height: 70vh; /* Limite la hauteur du formulaire à 70% de la hauteur de la vue */
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

         
        /* Style pour le formulaire de filtrage */
        form {
            background-color: #ffffff;
            padding: 1.5rem;
            margin-bottom: 2rem;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 51, 102, 0.1);
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
        }

        form label {
            display: block;
            color: #003366;
            font-size: 1rem;
            margin-right: 1rem;
        }

        form input, form select, form button {
            padding: 0.8rem;
            margin-bottom: 1rem;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: -1rem;
            width: 12%;
        }

        form input[type="number"] {
            width: 23%;
            display: inline-block;
        }

        form button {
            background-color: #003366;
            color: white;
            border: none;
            cursor: pointer;
            font-size: 1rem;
            width: 100%;
            transition: background-color 0.3s ease;
        }

        form button:hover {
            background-color: #0066cc;
        }

        /* Style de la table */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 2rem;
            background-color: #fff;
        }

        table th, table td {
            padding: 12px 15px;
            border: 1px solid #ddd;
            text-align: left;
            font-size: 1rem;
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

        /* Style des liens dans la table */
        table a {
            color: #0066cc;
            text-decoration: none;
            font-size: 1rem;
        }

        table a:hover {
            color: #003366;
        }

        /* Style des messages d'erreur */
        .error {
            color: red;
            font-size: 1rem;
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
                <h2 class="my-4">Recherche de Vols</h2>
                <form method="POST" action="/ticketing/vol/filter">
                    <label for="dateDepart">Date de depart :</label>
                    <input type="date" id="dateDepart" name="dateDepart">
                    
                    <label for="dateArrivee">Date d'arrivee :</label>
                    <input type="date" id="dateArrivee" name="dateArrivee">
                    
                    <label for="villeDepart">Ville de depart :</label>
                    <select id="villeDepart" name="villeDepart">
                        <option value="">-- Selectionner --</option>
                        <% for (Ville ville : villes) { %>
                            <option value="<%= ville.getId() %>"><%= ville.getDesignation() %></option>
                        <% } %>
                    </select>
                    
                    <label for="villeArrivee">Ville d'arrivee :</label>
                    <select id="villeArrivee" name="villeArrivee">
                        <option value="">-- Selectionner --</option>
                        <% for (Ville ville : villes) { %>
                            <option value="<%= ville.getId() %>"><%= ville.getDesignation() %></option>
                        <% } %>
                    </select>
                    
                    <label for="prixMinEco">Prix min economique (€) :</label>
                    <input type="number" id="prixMinEco" name="prixMinEco" step="0.01">
                    
                    <label for="prixMaxEco">Prix max economique (€) :</label>
                    <input type="number" id="prixMaxEco" name="prixMaxEco" step="0.01">
                    
                    <label for="prixMinBusiness">Prix min business (€) :</label>
                    <input type="number" id="prixMinBusiness" name="prixMinBusiness" step="0.01">
                    
                    <label for="prixMaxBusiness">Prix max business (€) :</label>
                    <input type="number" id="prixMaxBusiness" name="prixMaxBusiness" step="0.01">
                
                    <label for="avion">Avion :</label>
                    <select id="avion" name="avion">
                        <option value="">-- Selectionner --</option>
                        <% for (Avion avion : avions) { %>
                            <option value="<%= avion.getId() %>"><%= avion.getDesignation() %></option>
                        <% } %>
                    </select>
                    
                    <button type="submit">Filtrer</button>
                </form>

                <h2>Liste des Vols</h2>
                <table border="1">
                    <thead>
                        <tr>
                            <th>Designation</th>
                            <th>Avion</th>
                            <th>Ville Depart</th>
                            <th>Ville Arrivee</th>
                            <th>Heure Depart</th>
                            <th>Heure Arrivee</th>
                            <th>Prix economique</th>
                            <th>Prix Business</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            try (Connection conn = MyConnection.getConnection()) {
                                if (vols != null) {
                                    for (Vol vol : vols) { 
                                        Avion avion = Avion.getById(conn, vol.getIdAvion());
                                        Ville villeDepart = Ville.getById(conn, vol.getIdVilleDepart());
                                        Ville villeArrivee = Ville.getById(conn, vol.getIdVilleArrivee());
                                        double prixEco = vol.getPrixSiege(conn, 1);
                                        double prixBusiness = vol.getPrixSiege(conn, 2);
                        %>
                        <tr>
                            <td><%= vol.getDesignation() %></td>
                            <td><%= avion.getDesignation() %></td>
                            <td><%= villeDepart.getDesignation() %></td>
                            <td><%= villeArrivee.getDesignation() %></td>
                            <td><%= vol.getDateHeureDepart() %></td>
                            <td><%= vol.getDateHeureArrivee() %></td>
                            <td><%= df.format(prixEco) %> €</td>
                            <td><%= df.format(prixBusiness) %> €</td>
                            <td>
                                <a href="/ticketing/vol/modifier?id=<%= vol.getId() %>">Modifier</a> |
                                <a href="/ticketing/vol/delete?id=<%= vol.getId() %>" onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce vol ?');">Supprimer</a>
                            </td>
                        </tr>
                        <%
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                        %>
                        <tr>
                            <td colspan="9" style="color: red;">Une erreur s'est produite lors du chargement des vols.</td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
                <br>

                <a href="/ticketing/user/logout" class="logout">Logout</a>

            </div>
        </main>
    </div>
</body>
</html>
