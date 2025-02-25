package model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import annotations.*;
import myconnection.MyConnection;
import session.MySession;
import utils.ModelView;

public class RegleReservation {
    private int id;
    private int idVol;
    private int nbHeureLimiteAvantVol;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdVol() { return idVol; }
    public void setIdVol(int idVol) { this.idVol = idVol; }
    public int getNbHeureLimiteAvantVol() { return nbHeureLimiteAvantVol; }
    public void setNbHeureLimiteAvantVol(int nbHeureLimiteAvantVol) { this.nbHeureLimiteAvantVol = nbHeureLimiteAvantVol; }

    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Regle_reservation (id_vol, nb_heure_limite_avant_vol) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, this.idVol);
            stmt.setInt(2, this.nbHeureLimiteAvantVol);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) this.id = rs.getInt(1);
            }
        }
    }

    public static List<RegleReservation> getAll(Connection conn) throws SQLException {
        List<RegleReservation> regles = new ArrayList<>();
        String query = "SELECT * FROM Regle_reservation";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                RegleReservation regle = new RegleReservation();
                regle.setId(rs.getInt("id"));
                regle.setIdVol(rs.getInt("id_vol"));
                regle.setNbHeureLimiteAvantVol(rs.getInt("nb_heure_limite_avant_vol"));
                regles.add(regle);
            }
        }
        return regles;
    }

    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Regle_reservation SET id_vol = ?, nb_heure_limite_avant_vol = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.idVol);
            stmt.setInt(2, this.nbHeureLimiteAvantVol);
            stmt.setInt(3, this.id);
            stmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String query = "DELETE FROM Regle_reservation WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        }
    }


    public static RegleReservation getByIdVol(Connection conn, int idVol) throws SQLException {
        String query = "SELECT * FROM Regle_reservation WHERE id_vol = ? LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idVol);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    RegleReservation regle = new RegleReservation();
                    regle.setId(rs.getInt("id"));
                    regle.setIdVol(rs.getInt("id_vol"));
                    regle.setNbHeureLimiteAvantVol(rs.getInt("nb_heure_limite_avant_vol"));
                    return regle;
                }
            }
        }
        return null; // Retourne null si aucune règle n'est trouvée
    }
    

    

    public static boolean reservationPossible(Connection conn, int idvol, Reservation reservation) throws Exception
    {
        Vol vol = Vol.getById(conn, idvol);
        RegleReservation regleReservation = getByIdVol(conn, idvol);

        if (vol == null || regleReservation == null) {
            return false; // Vérifie si les données existent
        }

        // Convertir Timestamp en LocalDateTime
        LocalDateTime heureDepart = vol.getDateHeureDepart().toLocalDateTime();

        // Soustraire le nombre d'heures
        LocalDateTime heureFinReservation = heureDepart.minusHours(regleReservation.getNbHeureLimiteAvantVol());

        // Reconvertir en Timestamp si nécessaire
        Timestamp heureFinTimestamp = Timestamp.valueOf(heureFinReservation);

        System.out.println("Heure limite de réservation : " + heureFinTimestamp);
        
    
        return reservation.getDateHeureReservation().before(heureFinTimestamp);
    }




      
}