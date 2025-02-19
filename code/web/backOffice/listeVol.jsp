<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="controller.Vol" %>
<%@ page import="controller.Avion" %>
<%@ page import="controller.Ville" %>
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
</head>
<body>

    <h2>Recherche de Vols</h2>
    <form method="POST" action="/ticketing/vol/filter">
        <label for="dateDepart">Date de départ :</label>
        <input type="date" id="dateDepart" name="dateDepart">
        
        <label for="dateArrivee">Date d'arrivée :</label>
        <input type="date" id="dateArrivee" name="dateArrivee">
        
        <label for="villeDepart">Ville de départ :</label>
        <select id="villeDepart" name="villeDepart">
            <option value="">-- Sélectionner --</option>
            <% for (Ville ville : villes) { %>
                <option value="<%= ville.getId() %>"><%= ville.getDesignation() %></option>
            <% } %>
        </select>
        
        <label for="villeArrivee">Ville d'arrivée :</label>
        <select id="villeArrivee" name="villeArrivee">
            <option value="">-- Sélectionner --</option>
            <% for (Ville ville : villes) { %>
                <option value="<%= ville.getId() %>"><%= ville.getDesignation() %></option>
            <% } %>
        </select>
        
        <label for="prixMinEco">Prix min économique (€) :</label>
        <input type="number" id="prixMinEco" name="prixMinEco" step="0.01">
        
        <label for="prixMaxEco">Prix max économique (€) :</label>
        <input type="number" id="prixMaxEco" name="prixMaxEco" step="0.01">
        
        <label for="prixMinBusiness">Prix min business (€) :</label>
        <input type="number" id="prixMinBusiness" name="prixMinBusiness" step="0.01">
        
        <label for="prixMaxBusiness">Prix max business (€) :</label>
        <input type="number" id="prixMaxBusiness" name="prixMaxBusiness" step="0.01">
    
        
        <label for="avion">Avion :</label>
        <select id="avion" name="avion">
            <option value="">-- Sélectionner --</option>
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
                <th>Désignation</th>
                <th>Avion</th>
                <th>Ville Départ</th>
                <th>Ville Arrivée</th>
                <th>Heure Départ</th>
                <th>Heure Arrivée</th>
                <th>Prix Économique</th>
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
                        <a href="modifierVol.jsp?id=<%= vol.getId() %>">Modifier</a> |
                        <a href="supprimerVol?id=<%= vol.getId() %>" onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce vol ?');">Supprimer</a>
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

</body>
</html>
