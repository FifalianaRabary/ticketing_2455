<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Site de Gestion de Viticulture</title>
    <!-- Bootstrap CSS -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/header.css" rel="stylesheet">
    <link href="css/navigation.css" rel="stylesheet">
    <link href="css/footer.css" rel="stylesheet">
    <link href="css/contenu.css" rel="stylesheet">
    <link href="css/calendrier.css" rel="stylesheet">
    <!-- Font Awesome for Bell Icon -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <link href="assets/css/fullcalendar.min.css" rel="stylesheet">

    <style>
        /* Liste déroulante des notifications */
.notification-list {
    position: absolute;
    top: 100%; /* Placée sous l'icône */
    right: 0;
    background-color: white;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    border-radius: 5px;
    width: 250px;
    z-index: 1000;
    padding: 10px;
    display: none; /* Cachée par défaut */
}

.notification-list.active {
    display: block; /* Affichée lorsque active */
}

.notification-item {
    padding: 10px;
    border-bottom: 1px solid #ddd;
    font-size: 14px;
    color: #333;
}

.notification-item:last-child {
    border-bottom: none;
}

.notification-item:hover {
    background-color: #f5f5f5;
    cursor: pointer;
}
    </style>
</head>
<body>

<!-- Bande rouge foncé pour le header -->
<header class="header-bar">
    <div class="contenu-header">
        <!-- Slogan -->
        <h1 class="slogan mb-0">Boulangerie</h1>
        <!-- Icône de cloche pour les notifications -->
        <div class="notification-bell" onclick="toggleNotifications()">
            <i class="fas fa-bell fa-2x"></i>
            <!-- Badge de notification -->
            <span class="badge rounded-pill">3</span>
            <!-- Liste déroulante des notifications -->
            <div id="notification-list" class="notification-list">
                <p class="notification-item">Nouvelle tâche ajoutée</p>
                <p class="notification-item">Rappel : récolte demain</p>
                <p class="notification-item">Message : Vérifiez l'état des vignes</p>
            </div>
        </div>
    </div>
</header>

<!-- Script pour la gestion des notifications -->
<script>
    function toggleNotifications() {
        const notificationList = document.getElementById('notification-list');
        notificationList.classList.toggle('active');
    }

    document.addEventListener('click', function(event) {
        const bell = document.querySelector('.notification-bell');
        const notificationList = document.getElementById('notification-list');
        if (!bell.contains(event.target)) {
            notificationList.classList.remove('active');
        }
    });
</script>

