<%@ page import="model.Vol" %>
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
    <a href="/ticketing/vol/formVol">Inserer un vol </a>
        <a href="/ticketing/vol/listVol">Liste des vols </a>
        <a href="/ticketing/promotion/formPromotion">Promotion  vols </a>
        <br>
        <a href="/ticketing/annulation/formAnnulation">Annulation  reservation </a>
        <br>
        <a href="/ticketing/regleReservation/formRegleReservation">Regle de  reservation </a>
        <br>
        
    <h1>Definir une regle de reservation</h1>

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

    <a href="/ticketing/user/logout">Logout </a>


    <% } else { %>
        <h2>Les donnees necessaires (vols) sont manquantes.</h2>
    <% } %>
</body>
</html>
