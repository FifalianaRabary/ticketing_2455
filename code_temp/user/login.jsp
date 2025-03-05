<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <style>
        /* Styles généraux */
        body {
            font-family: Arial, sans-serif;
            background-color: #e0f7ff;
            color: #003366;
            margin: 0;
            padding: 0;
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column; /* Centrer le contenu verticalement et horizontalement */
            font-size: 23px;
        }

        /* Conteneur du titre */
        .div-titre {
            margin-top: 26%;        }

        h1 {
            color: #002244;
            font-size: 2rem; /* Augmenter la taille du titre */
            margin: 0;
        }

        /* Conteneur principal du formulaire */
        .form-container {
            background-color: #ffffff;
            width: 80%; /* largeur du formulaire en pourcentage */
            max-width: 400px; /* limite maximale de largeur */
            margin: 20px auto;
            padding: 5%;
            border-radius: 10px;
            box-shadow: 0px 0px 10px rgba(0, 51, 102, 0.3);
        }

        /* Champs input */
        .input-container {
            margin: 5% 0;
        }

        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 3%;
            margin: 0;
            border: 1px solid #99c2ff;
            border-radius: 5px;
            font-size: 1rem;
        }

        /* Bouton de soumission */
        .submit-container {
            margin-top: 10%;
        }

        input[type="submit"] {
            background-color: #003366;
            color: white;
            border: none;
            padding: 3% 5%;
            font-size: 1rem;
            border-radius: 5px;
            cursor: pointer;
            transition: background 0.3s ease;
            width: 100%;
        }

        input[type="submit"]:hover {
            background-color: #004999;
        }
    </style>
</head>
<body>
    <div class="div-titre">
        <h1>Login</h1>
    </div>
    <div class="form-container">
        <form action="/ticketing/user/checkLogin" method="post">
            <div class="input-container">
                <label for="username">Nom d'Utilisateur:</label><br>
                <input type="text" name="user.username" id="username" required value="fifa"><br>
            </div>
            <div class="input-container">
                <label for="mdp">Mot de passe:</label><br>
                <input type="password" name="user.mdp" id="mdp" required value="fifa"><br>
            </div>
            <div class="submit-container">
                <input type="submit" value="Login">
            </div>
        </form>
    </div>
</body>
</html>
