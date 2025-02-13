package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import annotations.*;
import session.MySession;
import utils.ModelView;

@Controller
public class Avion {
    private int id;
    private int id_modele;
    private int nb_siege_business;
    private int nb_siege_eco;
    private Date date_fabrication;

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getId_modele() { return id_modele; }
    public void setId_modele(int id_modele) { this.id_modele = id_modele; }
    public int getNb_siege_business() { return nb_siege_business; }
    public void setNb_siege_business(int nb_siege_business) { this.nb_siege_business = nb_siege_business; }
    public int getNb_siege_eco() { return nb_siege_eco; }
    public void setNb_siege_eco(int nb_siege_eco) { this.nb_siege_eco = nb_siege_eco; }
    public Date getDate_fabrication() { return date_fabrication; }
    public void setDate_fabrication(Date date_fabrication) { this.date_fabrication = date_fabrication; }

    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Avion (id_modele, nb_siege_business, nb_siege_eco, date_fabrication) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, this.id_modele);
            stmt.setInt(2, this.nb_siege_business);
            stmt.setInt(3, this.nb_siege_eco);
            stmt.setDate(4, this.date_fabrication);
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
                    avion.setId_modele(rs.getInt("id_modele"));
                    avion.setNb_siege_business(rs.getInt("nb_siege_business"));
                    avion.setNb_siege_eco(rs.getInt("nb_siege_eco"));
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
                avion.setId_modele(rs.getInt("id_modele"));
                avion.setNb_siege_business(rs.getInt("nb_siege_business"));
                avion.setNb_siege_eco(rs.getInt("nb_siege_eco"));
                avion.setDate_fabrication(rs.getDate("date_fabrication"));
                avions.add(avion);
            }
        }
        return avions;
    }

    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Avion SET id_modele = ?, nb_siege_business = ?, nb_siege_eco = ?, date_fabrication = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id_modele);
            stmt.setInt(2, this.nb_siege_business);
            stmt.setInt(3, this.nb_siege_eco);
            stmt.setDate(4, this.date_fabrication);
            stmt.setInt(5, this.id);
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
}
