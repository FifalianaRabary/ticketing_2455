<%@ page import="controller.Vol" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Regle d'annulation de reservation</title>
</head>
<body>
    <%
        List<Vol> vols = (List<Vol>) request.getAttribute("vols");

        if (vols != null) {
    %>
    
    <h1>Definir une règle d'annulation de reservation</h1>

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

        <input type="submit" value="Valider la règle">

    </form>

    <% } else { %>
        <h2>Les donnees necessaires (vols) sont manquantes.</h2>
    <% } %>
</body>
</html>
