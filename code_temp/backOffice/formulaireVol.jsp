<%@ page import="controller.Vol" %>
<%@ page import="controller.Avion" %>
<%@ page import="controller.Ville" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ajouter un Vol</title>
</head>
<body>
    <%
        List<Ville> villes = (List<Ville>) request.getAttribute("villes");
        List<Avion> avions = (List<Avion>) request.getAttribute("avions");

        if (villes != null && avions != null) {
    %>
    
    <h2>Ajouter un Nouveau Vol</h2>

    <form action="/ticketing/vol/insert" method="post">

        <label for="designation">Designation: </label>
        <input type="text" name="vol.designation" value="vol6">
        
        <!-- Sélection de l'avion -->
        <label for="avion">Avion :</label>
        <select name="vol.idAvion" id="avion" required>
            <option value="">Choisir un avion</option>
            <% for (Avion avion : avions) { %>
                <option value="<%= avion.getId() %>"><%= avion.getDesignation() %></option>
            <% } %>
        </select>

        <br><br>

        <!-- Sélection de la ville -->
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

        <!-- Date et heure de départ -->
        <label for="dateDepart">Date et Heure de Départ :</label>
        <input type="datetime-local" name="vol.dateHeureDepart" id="dateDepart" required>

        <br><br>

        <!-- Date et heure d'arrivée -->
        <label for="dateArrivee">Date et Heure d'Arrivée :</label>
        <input type="datetime-local" name="vol.dateHeureArrivee" id="dateArrivee" required>

        <br><br>

        <!-- Définition des prix pour chaque type de siège -->
        <h3>Définir les prix des sièges :</h3>

        <label for="prixEconomique">Prix Siège Économique :</label>
        <input type="number" name="prixEconomique" id="prixEconomique" step="0.01" min="0" value="200" required>
        <br><br>

        <label for="prixBusiness">Prix Siège Business :</label>
        <input type="number" name="prixBusiness" id="prixBusiness" step="0.01"  value="500" min="0" required>
        <br><br>

        <input type="submit" value="Ajouter le Vol">
    </form>

    <% } else { %>
        <h2>Les données nécessaires (villes ou avions) sont manquantes.</h2>
    <% } %>
</body>
</html>
