package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import annotations.*;
import session.MySession;
import utils.ModelView;

@Controller
public class Ville {
    private int id;
    private String designation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Ville (designation) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, this.designation);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    public static Ville getById(Connection conn, int id) throws SQLException {
        String query = "SELECT * FROM Ville WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Ville ville = new Ville();
                    ville.setId(rs.getInt("id"));
                    ville.setDesignation(rs.getString("designation"));
                    return ville;
                }
            }
        }
        return null;
    }

    public static List<Ville> getAll(Connection conn) throws SQLException {
        List<Ville> villes = new ArrayList<>();
        String query = "SELECT * FROM Ville";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Ville ville = new Ville();
                ville.setId(rs.getInt("id"));
                ville.setDesignation(rs.getString("designation"));
                villes.add(ville);
            }
        }
        return villes;
    }

    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Ville SET designation = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, this.designation);
            stmt.setInt(2, this.id);
            stmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String query = "DELETE FROM Ville WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        }
    }
}
