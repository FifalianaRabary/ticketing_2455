
<%@ page import="controller.Admin" %>
<%@ page import="session.MySession" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>


<div class="d-flex">


    <main class="content p-4 ms-3 me-3 content-scrollable">


    <div class="new-container">

        <a href="/ticketing/vol/formVol">Inserer un vol </a>
        <a href="/ticketing/vol/listVol">Liste des vols </a>
    
        <%
            Admin admin = (Admin) session.getAttribute("admin"); // ✅ Bon type

            if(admin!=null){
                System.out.println("HELL YEAH");
            %>
            <h2 class="my-4">Welcome to your Dashboard!  <%= admin.getUsername() %> </h2>  
        <% } else {
                out.println("<p>Error: Admin data is missing.</p>");
            }
        %>
    
    <%-- Inclure ici d'autres éléments de l'interface admin si nécessaire --%>

    <form action="/ticketing/admin/logout" method="post">
        <input type="submit" value="logout">
    </form>
    </div>

    </main>
</div>



