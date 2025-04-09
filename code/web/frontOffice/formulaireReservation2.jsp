<%@ page import="model.Vol" %>
<%@ page import="model.Reservation" %>
<%@ page import="model.TypeSiege" %>
<%@ page import="model.Utilisateur" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reserver un Vol</title>

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
        CatchFlights - FrontOffice
    </header>

    <div class="main-container">
        <div class="navigation">
            <%
                Utilisateur user = (Utilisateur) session.getAttribute("user");
                int idClient = (user != null) ? user.getId() : -1;

                if (user != null) {
            %>
            <a href="/ticketing/frontOffice/dashboard">Tableau de bord</a>
            <a href="/ticketing/vol/listVolFront">Liste des vols</a>
            <a href="/ticketing/reservation/formReservation">Faire une reservation</a>
            <a href="/ticketing/reservation/listReservation?id=<%= user.getId() %>">Voir mes reservations</a>
            <a href="/ticketing/user/upload">Upload photo passeport</a>
            <% } else { %>
            <p>Erreur : utilisateur non connecte.</p>
            <% } %>
        </div>

        <main class="content">
            <div class="new-container">

                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>
                
                <%
                    List<TypeSiege> typeSieges = (List<TypeSiege>) request.getAttribute("typeSieges");
                    Integer idReservation = (Integer) request.getAttribute("idReservation");

                    if (typeSieges != null && idReservation != null ) {
                %>
                <h2 class="my-4">Reserver un Vol : Etape 2</h2>
                <h3>Inserez les détails</h3>

                <form action="/ticketing/reservation/insertDetail" method="post">
                    <!-- ID Client cache -->
                    <input type="hidden" name="detailReservation.idReservation" value="<%= idReservation %>">
                
                    <!-- Informations personnelles -->
                    <div class="form-group">
                        <label for="nom">Nom :</label>
                        <input type="text" name="detailReservation.nom" id="nom" required>
                    </div>
                
                    <div class="form-group">
                        <label for="prenom">Prénom :</label>
                        <input type="text" name="detailReservation.prenom" id="prenom" required>
                    </div>
                
                    <div class="form-group">
                        <label for="dateNaissance">Date de naissance :</label>
                        <input type="date" name="detailReservation.dateNaissance" id="dateNaissance" required>
                    </div>
                
                    <!-- Selection du type de siege -->
                    <div class="form-group">
                        <label for="typeSiege">Type de Siège :</label>
                        <select name="detailReservation.idTypeSiege" id="typeSiege" required>
                            <option value="">Choisir un type de siège</option>
                            <% for (TypeSiege type : typeSieges) { %>
                                <option value="<%= type.getId() %>"><%= type.getDesignation() %></option>
                            <% } %>
                        </select>
                    </div>
                
                    <input type="submit" value="ajouter détail" class="btn btn-primary">
                </form>

                <a href="/ticketing/user/logout" class="logout">Logout</a>

                <% } else { %>
                <h2>Les donnees necessaires (vols, types de sieges ou client) sont manquantes.</h2>
                <% } %>
            </div>
        </main>
    </div>
</body>
</html>