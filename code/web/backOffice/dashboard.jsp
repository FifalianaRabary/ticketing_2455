
<%@ page import="model.Utilisateur" %>
<%@ page import="session.MySession" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>


<div class="d-flex">


    <main class="content p-4 ms-3 me-3 content-scrollable">


    <div class="new-container">

        <a href="/ticketing/vol/formVol">Inserer un vol </a>
        <a href="/ticketing/vol/listVol">Liste des vols </a>
        <a href="/ticketing/promotion/formPromotion">Promotion  vols </a>
        <br>
        <a href="/ticketing/annulation/formAnnulation">Annulation  reservation </a>
        <br>
        <a href="/ticketing/regleReservation/formRegleReservation">Regle de  reservation </a>
        <br>

        
    
        <%
            Utilisateur admin = (Utilisateur) session.getAttribute("user");
            String role = (String) session.getAttribute("role");
            
            if(admin!=null && role!=null){
                System.out.println("HELL YEAH");
            %>
            <h2 class="my-4">Bienvenu dans le dashboard  <%= admin.getUsername() %> <%= role %> !</h2>  
        <% } else {
                out.println("<p>Error: Admin data is missing.</p>");
            }
        %>
    
    <%-- Inclure ici d'autres éléments de l'interface admin si nécessaire --%>

    <a href="/ticketing/user/logout">Logout </a>

    </div>

    </main>
</div>



