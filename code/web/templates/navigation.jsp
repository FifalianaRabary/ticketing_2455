<!-- Barre de navigation gauche -->
<div class="sidebar">
    <div class="logo">Navigation</div>
    <a href="index.jsp">Dashboard</a>

    <!-- Rubrique avec sous-menu (deroulable) -->
    <!-- <a class="dropdown-toggle" href="#menu1" data-bs-toggle="collapse" aria-expanded="false">Gestion des vignes</a>
    <div class="collapse collapse-menu" id="menu1">
        <a href="#">Production</a>
        <a href="#">Qualite</a>
        <a href="#">Entretien</a>
    </div> -->

    <a class="dropdown-toggle" href="#menu1" data-bs-toggle="collapse" aria-expanded="false">Unite</a>
    <div class="collapse collapse-menu" id="menu1">
        <a class="dropped" href="insertionUnite.jsp">Insertion Unite</a>
        <a class="dropped" href="ListeUniteServlet">Liste Unite</a>
    </div> 

    <a class="dropdown-toggle" href="#menu2" data-bs-toggle="collapse" aria-expanded="false">Ingredient</a>
    <div class="collapse collapse-menu" id="menu2">
        <a class="dropped" href="IngredientServlet">Insertion Ingredient</a>
        <a class="dropped" href="ListeIngredientServlet">Liste Ingredient</a>

    </div> 

    <a class="dropdown-toggle" href="#menu3" data-bs-toggle="collapse" aria-expanded="false">Type produit</a>
    <div class="collapse collapse-menu" id="menu3">
        <a class="dropped" href="TypeProduitServlet">Insertion Type produit</a>
        <a class="dropped" href="ListeTypeProduitServlet">Liste Type produit</a>

    </div> 

    <!-- <a class="dropdown-toggle" href="#menu4" data-bs-toggle="collapse" aria-expanded="false">Role</a>
    <div class="collapse collapse-menu" id="menu4">
        <a class="dropped" href="insertionRole.jsp">Insertion role</a>
        <a class="dropped" href="ListeRoleServlet">Liste role</a>

    </div>

    <a class="dropdown-toggle" href="#menu5" data-bs-toggle="collapse" aria-expanded="false">Utilisateur</a>
    <div class="collapse collapse-menu" id="menu5">
        <a class="dropped" href="UtilisateurServlet">Insertion Utilisateur</a>
        <a class="dropped" href="ListeUtilisateurServlet">Liste Utilisateur</a>

    </div> -->

    <a class="dropdown-toggle" href="#menu7" data-bs-toggle="collapse" aria-expanded="false">Fournisseur</a>
    <div class="collapse collapse-menu" id="menu7">
        <a class="dropped" href="insertionFournisseur.jsp">Insertion Fournisseur</a>
        <a class="dropped" href="ListeFournisseurServlet">Liste Fournisseur</a>

    </div>

    <!-- <a class="dropdown-toggle" href="#menu8" data-bs-toggle="collapse" aria-expanded="false">Status</a>
    <div class="collapse collapse-menu" id="menu8">
        <a class="dropped" href="insertionStatus.jsp">Insertion Status</a>
        <a class="dropped" href="ListeStatusServlet">Liste Status</a>

    </div> -->

    <a class="dropdown-toggle" href="#menu9" data-bs-toggle="collapse" aria-expanded="false">Achat Ingredient</a>
    <div class="collapse collapse-menu" id="menu9">
        <a class="dropped" href="AchatIngredientFournisseurServlet">Insertion achat ingredient</a>
        <a class="dropped" href="ListeAchatIngredientFournisseurServlet">Liste achat ingredient</a>

    </div>

    <a class="dropdown-toggle" href="#menu10" data-bs-toggle="collapse" aria-expanded="false">Production</a>
    <div class="collapse collapse-menu" id="menu10">
        <a class="dropped" href="ProductionServlet">Insertion  production</a>
        <a class="dropped" href="FiltreProductionServlet">Liste production avec filtre</a>

    </div>

    <a class="dropdown-toggle" href="#menu11" data-bs-toggle="collapse" aria-expanded="false">Commande</a>
    <div class="collapse collapse-menu" id="menu11">
        <a class="dropped" href="CommandeServlet">Insertion  commande</a>
        <!-- <a class="dropped" href="Liste-Servlet">Liste - </a> -->
        <a class="dropped" href="RechercheCommandeServlet">Liste Commande-client </a>

    </div>

    <a class="dropdown-toggle" href="#menu12" data-bs-toggle="collapse" aria-expanded="false">Conseil du mois</a>
    <div class="collapse collapse-menu" id="menu12">
        <a class="dropped" href="ConseilMoisServlet">Insertion  Conseil Mois</a>
        <!-- <a class="dropped" href="ListeConseilMoisServlet">Liste ConseilMois </a> -->
        <a class="dropped" href="ListeConseilMoisServlet">Liste Conseil Mois </a>

    </div>

    <a class="dropdown-toggle" href="#menu13" data-bs-toggle="collapse" aria-expanded="false">Categorie produit</a>
    <div class="collapse collapse-menu" id="menu13">
        <a class="dropped" href="insertionCategorieProduit.jsp">Insertion categorie produit</a>
        <a class="dropped" href="ListeCategorieProduitServlet">Liste categorie produit</a>

    </div>

    <a class="dropdown-toggle" href="#menu14" data-bs-toggle="collapse" aria-expanded="false">Vendeur</a>
    <div class="collapse collapse-menu" id="menu14">
        <a class="dropped" href="insertionVendeur.jsp">Insertion vendeur</a>
        <a class="dropped" href="ListeVendeurServlet">Liste vendeur</a>

    </div>
    
</div>
