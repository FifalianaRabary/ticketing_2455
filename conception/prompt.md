voici ma classe utilisateur:
package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import annotations.*;
import session.MySession;
import utils.ModelView;


@controller
public class Utilisateur {

    private int id;
    private String mail;
    private String mdp;

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    // Méthode pour créer un utilisateur (INSERT)
    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Utilisateur (mail, mdp) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, this.mail);
            stmt.setString(2, this.mdp);
            stmt.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    // Méthode pour récupérer un utilisateur par ID (SELECT)
    public static Utilisateur getById(Connection conn, int id) throws SQLException {
        String query = "SELECT * FROM Utilisateur WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Utilisateur user = new Utilisateur();
                    user.setId(rs.getInt("id"));
                    user.setMail(rs.getString("mail"));
                    user.setMdp(rs.getString("mdp"));
                    return user;
                }
            }
        }
        return null;
    }

    // Méthode pour récupérer tous les utilisateurs (SELECT ALL)
    public static List<Utilisateur> getAll(Connection conn) throws SQLException {
        List<Utilisateur> users = new ArrayList<>();
        String query = "SELECT * FROM Utilisateur";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Utilisateur user = new Utilisateur();
                user.setId(rs.getInt("id"));
                user.setMail(rs.getString("mail"));
                user.setMdp(rs.getString("mdp"));
                users.add(user);
            }
        }
        return users;
    }

    // Méthode pour mettre à jour un utilisateur (UPDATE)
    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Utilisateur SET mail = ?, mdp = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, this.mail);
            stmt.setString(2, this.mdp);
            stmt.setInt(3, this.id);
            stmt.executeUpdate();
        }
    }

    // Méthode pour supprimer un utilisateur (DELETE)
    public void delete(Connection conn) throws SQLException {
        String query = "DELETE FROM Utilisateur WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        }
    }
}

faites également les classes avec getters setters et CRUD ( create, getAll, getById, delete, update ) pour les tables suivantes:
-- Création de la table Modèle
CREATE TABLE Modele (
    id SERIAL PRIMARY KEY,
    designation VARCHAR(100) NOT NULL UNIQUE
);

-- Création de la table Avion
CREATE TABLE Avion (
    id SERIAL PRIMARY KEY,
    id_modele INT REFERENCES Modele(id) ON DELETE CASCADE,
    nb_siege_business INT CHECK (nb_siege_business >= 0),
    nb_siege_eco INT CHECK (nb_siege_eco >= 0),
    date_fabrication DATE NOT NULL
);

-- Création de la table Type_siege
CREATE TABLE Type_siege (
    id SERIAL PRIMARY KEY,
    designation VARCHAR(50) NOT NULL UNIQUE
);

-- Création de la table Ville
CREATE TABLE Ville (
    id SERIAL PRIMARY KEY,
    designation VARCHAR(100) NOT NULL UNIQUE
);

-- Création de la table Vol
CREATE TABLE Vol (
    id SERIAL PRIMARY KEY,
    date_heure_depart TIMESTAMP NOT NULL,
    date_heure_arrivee TIMESTAMP NOT NULL CHECK (date_heure_arrivee > date_heure_depart),
    id_avion INT REFERENCES Avion(id) ON DELETE SET NULL,
    id_ville INT REFERENCES Ville(id) ON DELETE CASCADE,
    prix_business DECIMAL(10,2) CHECK (prix_business >= 0),
    prix_eco DECIMAL(10,2) CHECK (prix_eco >= 0)
);

PRENEZ EXEMPLE SUR LA STRUCTURE DE LA CLASSE UTILISATEUR QUE J AI DONNE PLUS TOT. LES CLASSES SE TROUVENT TOUS DANS LE PACKAGE CONTROLLER ET SONT TOUS ANNOTES @controller, ET DONNEZ MOI JUSTE LES CODES, N 'UTILISEZ PAS VOTRE OPTION ECRIRE LE CODE DANS UN PETIT DIV SUR LE COTE LA