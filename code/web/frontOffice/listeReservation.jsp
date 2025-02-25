<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Reservation" %>
<%@ page import="model.Vol" %>
<%@ page import="model.Utilisateur" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="myconnection.MyConnection" %>


<%
    List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
    DecimalFormat df = new DecimalFormat("#0.00");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Réservations</title>
</head>
<body>
    <a href="/ticketing/vol/listVolFront">Liste des vols </a>
    <br>
    <a href="/ticketing/reservation/formReservation">Faire une reservation </a>
    <br>
   
    
    <h2>Liste des Réservations</h2>
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Vol</th>
                <th>Date et Heure de Réservation</th>
                <th>Montant</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <%
                try (Connection conn = MyConnection.getConnection()) {
                    if (reservations != null) {
                        for (Reservation reservation : reservations) { 
                            Vol vol = Vol.getById(conn, reservation.getIdVol());
            %>
                <tr>
                    <td><%= reservation.getId() %></td>
                    <td><%= vol != null ? vol.getDesignation() : "Vol inconnu" %></td>
                    <td><%= reservation.getDateHeureReservation() %></td>
                    <td><%= df.format(reservation.getMontant()) %> €</td>
                    <td><a href="/ticketing/reservation/annulation?id=<%= reservation.getId() %>&id_user=<%= id_user %>">Annuler</a></td>
                </tr>
            <%
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
            %>
                <tr>
                    <td colspan="6" style="color: red;">Une erreur s'est produite lors du chargement des réservations.</td>
                </tr>
            <%
                }
            %>
        </tbody>
    </table>

    <a href="/ticketing/user/logout">Logout</a>

</body>
</html>
