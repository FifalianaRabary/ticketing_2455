<%@ page import="model.Utilisateur" %>
<%@ page import="session.MySession" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>


<div class="d-flex">


    <main class="content p-4 ms-3 me-3 content-scrollable">


    <div class="new-container">

        <a href="/ticketing/vol/listVolFront">Liste des vols </a>
        <br>
        <a href="/ticketing/reservation/formReservation">Faire une reservation </a>
    
        <%
            Utilisateur user = (Utilisateur) session.getAttribute("user");
            String role = (String) session.getAttribute("role");
            
            if(user!=null && role!=null){
                System.out.println("HELL YEAH");
            %>
            <h2 class="my-4">Bienvenu dans le dashboard  <%= user.getUsername() %> <%= role %> !</h2>  
        <% } else {
                out.println("<p>Error: user data is missing.</p>");
            }
        %>
    

        <a href="/ticketing/user/logout">Logout </a>

    </div>

    </main>
</div>



