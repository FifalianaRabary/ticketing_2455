<%@ page import="controller.Vol" %>
<%@ page import="controller.TypeSiege" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>promotion de vol</title>
</head>
<body>
    <%
        List<Vol> vols = (List<Vol>) request.getAttribute("vols");
        List<TypeSiege> typeSieges = (List<TypeSiege>) request.getAttribute("typeSieges");

        if (vols != null ) {
    %>
    
    <h1>Determiner une  promotion de vol</h1>

    <form action="/ticketing/promotion/insert" method="post">

    
        
        <!-- Sélection du vol -->
        <label for="vol">Vol :</label>
        <select name="promotionSiege.idVol" id="vol" required>
            <option value="">Choisir un vol</option>
            <% for (Vol vol : vols) { %>
                <option value="<%= vol.getId() %>"><%= vol.getDesignation() %></option>
            <% } %>
        </select>

        <br><br>

        <!-- Sélection du type siege -->
        <label for="ville">Type siege :</label>
        <select name="promotionSiege.idTypeSiege" id="typeSiege" required>
            <option value="">Choisir un type de siege</option>
            <% for (TypeSiege typeSiege : typeSieges) { %>
                <option value="<%= typeSiege.getId() %>"><%= typeSiege.getDesignation() %></option>
            <% } %>
        </select>

        

        <label for="pourcent">Pourcent:</label>
        <input type="number" name="promotionSiege.pourcent" id="pourcent" required>

        <br><br>

        <label for="nbSiege">Nombre de siege :</label>
        <input type="number" name="promotionSiege.nbSiege" id="nbSiege" required>

        <br><br>

        <input type="submit" value="valider la promotion">

    </form>

    <% } else { %>
        <h2>Les données nécessaires (vols ou typeSieges) sont manquantes.</h2>
    <% } %>
</body>
</html>
