<%@ page import="controller.Vol" %>
<%@ page import="controller.Avion" %>
<%@ page import="controller.Ville" %>
<%@ page import="myconnection.MyConnection" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifier un Vol</title>
</head>
<body>
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
    
    <h2>Modifier le Vol</h2>

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
</body>
</html>