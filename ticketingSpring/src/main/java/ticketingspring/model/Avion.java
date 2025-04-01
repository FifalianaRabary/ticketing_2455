package ticketingspring.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Avion {
    private int id;
    private String designation;
    private int id_modele;
    private Date date_fabrication;

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public int getId_modele() { return id_modele; }
    public void setId_modele(int id_modele) { this.id_modele = id_modele; }
    public Date getDate_fabrication() { return date_fabrication; }
    public void setDate_fabrication(Date date_fabrication) { this.date_fabrication = date_fabrication; }

    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Avion (designation, id_modele, date_fabrication) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, this.designation);
            stmt.setInt(2, this.id_modele);
            stmt.setDate(3, this.date_fabrication);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) this.id = rs.getInt(1);
            }
        }
    }

    public static Avion getById(Connection conn, int id) throws SQLException {
        String query = "SELECT * FROM Avion WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Avion avion = new Avion();
                    avion.setId(rs.getInt("id"));
                    avion.setDesignation(rs.getString("designation"));
                    avion.setId_modele(rs.getInt("id_modele"));
                    avion.setDate_fabrication(rs.getDate("date_fabrication"));
                    return avion;
                }
            }
        }
        return null;
    }

    public static List<Avion> getAll(Connection conn) throws SQLException {
        List<Avion> avions = new ArrayList<>();
        String query = "SELECT * FROM Avion";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Avion avion = new Avion();
                avion.setId(rs.getInt("id"));
                avion.setDesignation(rs.getString("designation"));
                avion.setId_modele(rs.getInt("id_modele"));
                avion.setDate_fabrication(rs.getDate("date_fabrication"));
                avions.add(avion);
            }
        }
        return avions;
    }

    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Avion SET designation = ?, id_modele = ?, date_fabrication = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, this.designation);
            stmt.setInt(2, this.id_modele);
            stmt.setDate(3, this.date_fabrication);
            stmt.setInt(4, this.id);
            stmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String query = "DELETE FROM Avion WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        }
    }

    // Méthode pour obtenir le nombre de sièges d'un type donné pour un avion
    public static int getNbSieges(Connection conn, int id_avion, int id_type_siege) throws SQLException {
        String query = "SELECT nb FROM Siege_avion WHERE id_avion = ? AND id_type_siege = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id_avion);
            stmt.setInt(2, id_type_siege);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("nb");
                }
            }
        }
        return 0; // Retourne 0 si aucun siège trouvé
    }
}
