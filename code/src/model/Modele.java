package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import annotations.*;
import session.MySession;
import utils.ModelView;

public class Modele {
    private int id;
    private String designation;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Modele (designation) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, this.designation);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) this.id = rs.getInt(1);
            }
        }
    }

    public static Modele getById(Connection conn, int id) throws SQLException {
        String query = "SELECT * FROM Modele WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Modele modele = new Modele();
                    modele.setId(rs.getInt("id"));
                    modele.setDesignation(rs.getString("designation"));
                    return modele;
                }
            }
        }
        return null;
    }

    public static List<Modele> getAll(Connection conn) throws SQLException {
        List<Modele> modeles = new ArrayList<>();
        String query = "SELECT * FROM Modele";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Modele modele = new Modele();
                modele.setId(rs.getInt("id"));
                modele.setDesignation(rs.getString("designation"));
                modeles.add(modele);
            }
        }
        return modeles;
    }

    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Modele SET designation = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, this.designation);
            stmt.setInt(2, this.id);
            stmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String query = "DELETE FROM Modele WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        }
    }
}
