<%@ page import="model.Vol" %>
<%@ page import="model.Avion" %>
<%@ page import="model.Ville" %>
<%@ page import="myconnection.MyConnection" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifier un Vol</title>

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

        /* Style pour le formulaire de filtrage */
        form {
            background-color: #ffffff;
            padding: 1.5rem;
            margin-bottom: 2rem;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 51, 102, 0.1);
            overflow-y: auto;
            max-height: 70vh; /* Limite la hauteur du formulaire à 70% de la hauteur de la vue */
        }

        form label {
            display: block;
            color: #003366;
            font-size: 21px;
            margin-right: 1rem;
        }

        form input, form select, form button {
            padding: 0.8rem;
            margin-bottom: 1rem;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1rem;
            width: 45%;
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
        CatchFlights
    </header>

    <div class="main-container">
        <div class="navigation">
            <a href="/ticketing/backOffice/dashboard">Tableau de bord</a>
            <a href="/ticketing/vol/formVol">Inserer un vol</a>
            <a href="/ticketing/vol/listVol">Liste des vols</a>
            <a href="/ticketing/promotion/formPromotion">Promotion vols</a>
            <a href="/ticketing/annulation/formAnnulation">Annulation reservation</a>
            <a href="/ticketing/regleReservation/formRegleReservation">Regle de reservation</a>
        </div>

        <main class="content">
            <h2>Modifier le Vol</h2>

            <%
                List<Ville> villes = (List<Ville>) request.getAttribute("villes");
                List<Avion> avions = (List<Avion>) request.getAttribute("avions");
                Vol vol = (Vol) request.getAttribute("vol");

                if (villes != null && avions != null && vol != null) {
                    try (Connection conn = MyConnection.getConnection()) {
                        Avion avion = Avion.getById(conn, vol.getIdAvion());
                        Ville villeDepart = Ville.getById(conn, vol.getIdVilleDepart());
                        Ville villeArrivee = Ville.getById(conn, vol.getIdVilleArrivee());
            %>
            <form action="/ticketing/vol/update" method="post">
                <input type="hidden" name="vol.id" value="<%= vol.getId() %>">

                <label for="designation">Désignation :</label>
                <input type="text" name="vol.designation" value="<%= vol.getDesignation() %>" required>
                <br>

                <label for="avion">Avion :</label>
                <select name="vol.idAvion" id="avion" required>
                    <option value="<%= avion.getId() %>"><%= avion.getDesignation() %></option>
                    <% for (Avion a : avions) {
                        if (a.getId() != avion.getId()) { %>
                            <option value="<%= a.getId() %>"><%= a.getDesignation() %></option>
                    <% } } %>
                </select>
                <br>

                <label for="villeDepart">Ville Départ :</label>
                <select name="vol.idVilleDepart" id="villeDepart" required>
                    <option value="<%= villeDepart.getId() %>"><%= villeDepart.getDesignation() %></option>
                    <% for (Ville v : villes) {
                        if (v.getId() != villeDepart.getId()) { %>
                            <option value="<%= v.getId() %>"><%= v.getDesignation() %></option>
                    <% } } %>
                </select>
                <br>

                <label for="villeArrivee">Ville Arrivée :</label>
                <select name="vol.idVilleArrivee" id="villeArrivee" required>
                    <option value="<%= villeArrivee.getId() %>"><%= villeArrivee.getDesignation() %></option>
                    <% for (Ville v : villes) {
                        if (v.getId() != villeArrivee.getId()) { %>
                            <option value="<%= v.getId() %>"><%= v.getDesignation() %></option>
                    <% } } %>
                </select>
                <br>

                <label for="dateDepart">Date et Heure de Départ :</label>
                <input type="datetime-local" name="vol.dateHeureDepart" id="dateDepart" value="<%= vol.getDateHeureDepart() %>" required>
                <br>

                <label for="dateArrivee">Date et Heure d'Arrivée :</label>
                <input type="datetime-local" name="vol.dateHeureArrivee" id="dateArrivee" value="<%= vol.getDateHeureArrivee() %>" required>
                <br>

                <label for="prixEconomique">Prix Siège Économique :</label>
                <input type="number" name="prixEconomique" id="prixEconomique" step="0.01" min="0" value="<%= vol.getPrixSiege(conn, 1) %>" required>
                <br>

                <label for="prixBusiness">Prix Siège Business :</label>
                <input type="number" name="prixBusiness" id="prixBusiness" step="0.01" min="0" value="<%= vol.getPrixSiege(conn, 2) %>" required>
                <br>

                <input type="submit" value="Modifier le Vol">
            </form>

            <a href="/ticketing/user/logout" class="logout">Logout</a>

            <% 
                    } catch (Exception e) {
                        e.printStackTrace(); 
            %>
                <h2 style="color: red;">Une erreur s'est produite lors du chargement des données.</h2>
            <% 
                    } 
                } else { 
            %>
                <h2>Les données nécessaires (villes, avions ou vol) sont manquantes.</h2>
            <% } %>
        </main>
    </div>
</body>
</html>