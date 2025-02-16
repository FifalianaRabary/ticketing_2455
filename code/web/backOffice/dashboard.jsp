
<%@ page import="controller.Admin" %>
<%@ page import="session.MySession" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%@ include file="/templates/header.jsp" %>

<div class="d-flex">

    <%@ include file="/templates/navigation.jsp" %>

    <main class="content p-4 ms-3 me-3 content-scrollable">


    <div class="new-container">

        <%@ include file="templates/footer.jsp" %>
    
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

<%@ include file="/templates/footer.jsp" %>


