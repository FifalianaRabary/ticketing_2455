<%@ page import="model.Vol" %>
<%@ page import="model.Avion" %>
<%@ page import="model.Ville" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Utilisateur" %>

<%
 Utilisateur admin = (Utilisateur) session.getAttribute("user");

    List<Ville> villes = (List<Ville>) request.getAttribute("villes");
    List<Avion> avions = (List<Avion>) request.getAttribute("avions");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ajouter un Vol</title>

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
            <!-- <div class="new-container"> -->
                <h2>Ajouter un Nouveau Vol</h2>

                <%
                    if (villes != null && avions != null) {
                %>
                <form action="/ticketing/vol/insert" method="post">
            
                    <label for="designation">Designation: </label>
                    <input type="text" name="vol.designation" value="vol6">
                    
                    <!-- Selection de l'avion -->
                    <label for="avion">Avion :</label>
                    <select name="vol.idAvion" id="avion" required>
                        <option value="">Choisir un avion</option>
                        <% for (Avion avion : avions) { %>
                            <option value="<%= avion.getId() %>"><%= avion.getDesignation() %></option>
                        <% } %>
                    </select>
            
                    <br><br>
            
                    <!-- Selection de la ville -->
                    <label for="ville">Ville Depart :</label>
                    <select name="vol.idVilleDepart" id="ville" required>
                        <option value="">Choisir une ville</option>
                        <% for (Ville ville : villes) { %>
                            <option value="<%= ville.getId() %>"><%= ville.getDesignation() %></option>
                        <% } %>
                    </select>
            
                    <label for="ville">Ville Arrivee :</label>
                    <select name="vol.idVilleArrivee" id="ville" required>
                        <option value="">Choisir une ville</option>
                        <% for (Ville ville : villes) { %>
                            <option value="<%= ville.getId() %>"><%= ville.getDesignation() %></option>
                        <% } %>
                    </select>
            
                    <br><br>
            
                    <!-- Date et heure de depart -->
                    <label for="dateDepart">Date et Heure de Depart :</label>
                    <input type="datetime-local" name="vol.dateHeureDepart" id="dateDepart" required>
            
                    <br><br>
            
                    <!-- Date et heure d'arrivee -->
                    <label for="dateArrivee">Date et Heure d'Arrivee :</label>
                    <input type="datetime-local" name="vol.dateHeureArrivee" id="dateArrivee" required>
            
                    <br><br>
            
                    <!-- Definition des prix pour chaque type de siege -->
                    <h3>Definir les prix des sieges :</h3>
            
                    <label for="prixEconomique">Prix Siege economique :</label>
                    <input type="number" name="prixEconomique" id="prixEconomique" step="0.01" min="0" value="200" required>
                    <br><br>
            
                    <label for="prixBusiness">Prix Siege Business :</label>
                    <input type="number" name="prixBusiness" id="prixBusiness" step="0.01"  value="500" min="0" required>
                    <br><br>
            
                    <input type="submit" value="Ajouter le Vol">
                </form>
            
            
                <% } else { %>
                    <h2>Les donnees necessaires (villes ou avions) sont manquantes.</h2>
                <% } %>

                <a href="/ticketing/user/logout" class="logout">Logout</a>

            <!-- </div> -->
        </main>
    </div>
    

  
</body>
</html>
