<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="controller.Vol" %>
<%@ page import="controller.Avion" %>
<%@ page import="controller.Ville" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.sql.Connection" %>

<%
    List<Vol> vols = (List<Vol>) request.getAttribute("vols");
    Connection conn = (Connection) request.getAttribute("connection");
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
            <% if(vols!=null && conn!=null){


                for (Vol vol : vols) { 
                    System.out.println("heyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy : "+vol.getDesignation());

                    Avion avion = Avion.getById(conn,vol.getIdAvion());
                    Ville villeDepart = Ville.getById(conn, vol.getIdVilleDepart());
                    Ville villeArrivee = Ville.getById(conn, vol.getIdVilleArrivee());
                    double prixEco = vol.getPrixSiege(conn,1);
                    double prixBusiness = vol.getPrixSiege(conn,2);
                    
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
            <% } 
            } %>
            
           
        </tbody>
    </table>
</body>
</html>
