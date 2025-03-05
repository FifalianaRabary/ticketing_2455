<%@ page import="model.Vol" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Utilisateur" %>

<%
    Utilisateur admin = (Utilisateur) session.getAttribute("user");

%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Regle de reservation</title>

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
        CatchFlights - BackOffice
    </header>

    <div class="main-container">
        <div class="navigation">
            <a href="/ticketing/backOffice/dashboard">Tableau de bord</a>
            <a href="/ticketing/vol/formVol">Inserer un vol</a>
            <a href="/ticketing/vol/listVol">Liste des vols</a>
            <a href="/ticketing/promotion/formPromotion">Promotion vols</a>
            <a href="/ticketing/annulation/formAnnulation">Annulation reservation</a>
            <a href="/ticketing/regleReservation/formRegleReservation">Regle de reservation</a>
            <a href="/ticketing/user/jsonRes?id_user=<%= admin.getId() %>">Json response</a>

        </div>

        <main class="content">
            <h2>Definir une regle de reservation</h2>

            <%
                List<Vol> vols = (List<Vol>) request.getAttribute("vols");

                if (vols != null) {
            %>
            <form action="/ticketing/regleReservation/insert" method="post">
                <!-- Selection du vol -->
                <label for="vol">Vol :</label>
                <select name="regleReservation.idVol" id="vol" required>
                    <option value="">Choisir un vol</option>
                    <% for (Vol vol : vols) { %>
                        <option value="<%= vol.getId() %>"><%= vol.getDesignation() %></option>
                    <% } %>
                </select>

                <br><br>

                <!-- Nombre d'heures limite avant le vol -->
                <label for="nbHeureLimiteAvantVol">Nombre d'heures limite avant vol de fin de prise de reservation :</label>
                <input type="number" name="regleReservation.nbHeureLimiteAvantVol" id="nbHeureLimiteAvantVol" required>

                <br><br>

                <input type="submit" value="Valider la regle">
            </form>

            <a href="/ticketing/user/logout" class="logout">Logout</a>

            <% } else { %>
                <h2>Les donnees necessaires (vols) sont manquantes.</h2>
            <% } %>
        </main>
    </div>
</body>
</html>