package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import myconnection.MyConnection;

import annotations.*;
import myconnection.MyConnection;
import session.MySession;
import utils.ModelView;

public class Reservation {
    private int id;
    private int idClient;  // Remplacé Client par idClient
    private Timestamp dateHeureReservation;
    private int idVol;
    private int idTypeSiege;
    private double montant;

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdClient() {  // Getter pour idClient
        return idClient;
    }

    public void setIdClient(int idClient) {  // Setter pour idClient
        this.idClient = idClient;
    }

    public Timestamp getDateHeureReservation() {
        return dateHeureReservation;
    }

    public void setDateHeureReservation(Timestamp dateHeureReservation) {
        this.dateHeureReservation = dateHeureReservation;
    }

    public int getIdVol() {
        return idVol;
    }

    public void setIdVol(int idVol) {
        this.idVol = idVol;
    }

    public int getIdTypeSiege() {
        return idTypeSiege;
    }

    public void setIdTypeSiege(int idTypeSiege) {
        this.idTypeSiege = idTypeSiege;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public static int getNbReservation(Connection conn, int idVol, int idTypeSiege) throws SQLException {
        String query = "SELECT COUNT(*) FROM Reservation WHERE id_vol = ? AND id_type_siege = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idVol);
            stmt.setInt(2, idTypeSiege);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);  // Retourne le nombre de réservations
                }
            }
        }
        return 0;  // Si aucune réservation n'est trouvée, retourne 0
    }
    

    // Méthode pour insérer une réservation
    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Reservation (id_client, id_vol, id_type_siege, montant) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, this.idClient);  // Utilisation de idClient
            stmt.setInt(2, this.idVol);
            stmt.setInt(3, this.idTypeSiege);
            stmt.setDouble(4, this.montant);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    // Méthode pour récupérer une réservation par ID
    public static Reservation getById(Connection conn, int id) throws SQLException {
        String query = "SELECT * FROM Reservation WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Reservation res = new Reservation();
                    res.setId(rs.getInt("id"));
                    res.setIdClient(rs.getInt("id_client"));  // Récupération de idClient
                    res.setDateHeureReservation(rs.getTimestamp("date_heure_reservation"));
                    res.setIdVol(rs.getInt("id_vol"));
                    res.setIdTypeSiege(rs.getInt("id_type_siege"));
                    res.setMontant(rs.getDouble("montant"));
                    return res;
                }
            }
        }
        return null;
    }

    // Méthode pour récupérer toutes les réservations
    public static List<Reservation> getAll(Connection conn) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM Reservation";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Reservation res = new Reservation();
                res.setId(rs.getInt("id"));
                res.setIdClient(rs.getInt("id_client"));  // Récupération de idClient
                res.setDateHeureReservation(rs.getTimestamp("date_heure_reservation"));
                res.setIdVol(rs.getInt("id_vol"));
                res.setIdTypeSiege(rs.getInt("id_type_siege"));
                res.setMontant(rs.getDouble("montant"));
                reservations.add(res);
            }
        }
        return reservations;
    }

    // Méthode pour mettre à jour une réservation
    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Reservation SET id_client = ?, id_vol = ?, id_type_siege = ?, montant = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.idClient);  // Mise à jour de idClient
            stmt.setInt(2, this.idVol);
            stmt.setInt(3, this.idTypeSiege);
            stmt.setDouble(4, this.montant);
            stmt.setInt(5, this.id);
            stmt.executeUpdate();
        }
    }

    // Méthode pour supprimer une réservation
    public void delete(Connection conn) throws SQLException {
        String query = "DELETE FROM Reservation WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        }
    }

  
}
