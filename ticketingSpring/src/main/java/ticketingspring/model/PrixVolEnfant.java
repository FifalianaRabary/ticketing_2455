package ticketingspring.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrixVolEnfant {
    private int id;
    private int ageMax;
    private double pourcent;

    // Constructeur vide
    public PrixVolEnfant() {}

    // Constructeur avec tous les champs
    public PrixVolEnfant(int ageMax, double pourcent) {
        this.ageMax = ageMax;
        this.pourcent = pourcent;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(int ageMax) {
        this.ageMax = ageMax;
    }

    public double getPourcent() {
        return pourcent;
    }

    public void setPourcent(double pourcent) {
        if (pourcent < 0 || pourcent > 100) {
            throw new IllegalArgumentException("Le pourcentage doit être compris entre 0 et 100.");
        }
        this.pourcent = pourcent;
    }

    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Prix_vol_enfant (age_max, pourcent) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, this.ageMax);
            stmt.setDouble(2, this.pourcent);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    public static PrixVolEnfant getById(Connection conn, int id) throws SQLException {
        String query = "SELECT * FROM Prix_vol_enfant WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PrixVolEnfant prix = new PrixVolEnfant();
                    prix.setId(rs.getInt("id"));
                    prix.setAgeMax(rs.getInt("age_max"));
                    prix.setPourcent(rs.getDouble("pourcent"));
                    return prix;
                }
            }
        }
        return null;
    }

    public static List<PrixVolEnfant> getAll(Connection conn) throws SQLException {
        List<PrixVolEnfant> prixList = new ArrayList<>();
        String query = "SELECT * FROM Prix_vol_enfant";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                PrixVolEnfant prix = new PrixVolEnfant();
                prix.setId(rs.getInt("id"));
                prix.setAgeMax(rs.getInt("age_max"));
                prix.setPourcent(rs.getDouble("pourcent"));
                prixList.add(prix);
            }
        }
        return prixList;
    }
}