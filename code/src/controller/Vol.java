package controller;

import java.sql.*;
import annotations.*;

@Controller
public class Vol {
    private int id;
    private Timestamp dateHeureDepart;
    private Timestamp dateHeureArrivee;
    private int idAvion;
    private int idVille;

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Timestamp getDateHeureDepart() { return dateHeureDepart; }
    public void setDateHeureDepart(Timestamp dateHeureDepart) { this.dateHeureDepart = dateHeureDepart; }
    public Timestamp getDateHeureArrivee() { return dateHeureArrivee; }
    public void setDateHeureArrivee(Timestamp dateHeureArrivee) { this.dateHeureArrivee = dateHeureArrivee; }
    public int getIdAvion() { return idAvion; }
    public void setIdAvion(int idAvion) { this.idAvion = idAvion; }
    public int getIdVille() { return idVille; }
    public void setIdVille(int idVille) { this.idVille = idVille; }

    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Vol (date_heure_depart, date_heure_arrivee, id_avion, id_ville) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, this.dateHeureDepart);
            stmt.setTimestamp(2, this.dateHeureArrivee);
            stmt.setInt(3, this.idAvion);
            stmt.setInt(4, this.idVille);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    public static Vol getById(Connection conn, int id) throws SQLException {
        String query = "SELECT * FROM Vol WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Vol vol = new Vol();
                    vol.setId(rs.getInt("id"));
                    vol.setDateHeureDepart(rs.getTimestamp("date_heure_depart"));
                    vol.setDateHeureArrivee(rs.getTimestamp("date_heure_arrivee"));
                    vol.setIdAvion(rs.getInt("id_avion"));
                    vol.setIdVille(rs.getInt("id_ville"));
                    return vol;
                }
            }
        }
        return null;
    }

    public static double getPrixSiege(Connection conn, int idTypeSiege, int idVol) throws SQLException {
        String query = "SELECT p.prix FROM Prix_siege_vol p JOIN Vol v ON p.id_avion = v.id_avion WHERE p.id_type_siege = ? AND v.id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idTypeSiege);
            stmt.setInt(2, idVol);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("prix");
                }
            }
        }
        return -1; // Retourne -1 si aucun prix n'est trouvé
    }
}
