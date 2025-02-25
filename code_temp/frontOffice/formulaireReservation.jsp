<%@ page import="model.Vol" %>
<%@ page import="model.TypeSiege" %>
<%@ page import="model.Utilisateur" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reserver un Vol</title>
</head>
<body>

    <%
        List<Vol> vols = (List<Vol>) request.getAttribute("vols");
        List<TypeSiege> typeSieges = (List<TypeSiege>) request.getAttribute("typeSieges");

        Utilisateur user = (Utilisateur) session.getAttribute("user");

        int idClient = user.getId();

        if(user==null){
            System.out.println("user est null");
        }
       
    

        if (vols != null && typeSieges != null) {
    %>

    <a href="/ticketing/vol/listVolFront">Liste des vols </a>
        <br>
    <a href="/ticketing/reservation/formReservation">Faire une reservation </a>
    <br>
    <a href="/ticketing/reservation/listReservation?id=i<%= idClient %>">Voir mes reservations</a>

    
    <h2>Reserver un Vol</h2>

    <form action="/ticketing/reservation/insert" method="post">

        <!-- ID Client cache -->
        <input type="hidden" name="reservation.idUtilisateur" value="<%= idClient %>">

        <!-- Selection du vol -->
        <label for="vol">Vol :</label>
        <select name="reservation.idVol" id="vol" required>
            <option value="">Choisir un vol</option>
            <% for (Vol vol : vols) { %>
                <option value="<%= vol.getId() %>"><%= vol.getDesignation() %></option>
            <% } %>
        </select>

        <br><br>

        <!-- Selection du type de siège -->
        <label for="typeSiege">Type de Siège :</label>
        <select name="reservation.idTypeSiege" id="typeSiege" required>
            <option value="">Choisir un type de siège</option>
            <% for (TypeSiege type : typeSieges) { %>
                <option value="<%= type.getId() %>"><%= type.getDesignation() %></option>
            <% } %>
        </select>

        <br><br>


        <input type="submit" value="Reserver le Vol">
    </form>


    <a href="/ticketing/user/logout">Logout </a>


    <% } else { %>
        <h2>Les donnees necessaires (vols, types de sièges ou client) sont manquantes.</h2>
    <% } %>
</body>
</html>
