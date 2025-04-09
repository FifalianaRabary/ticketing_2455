package model;

import java.sql.*;

public class PrixVolEnfant {
    private int id;
    private int ageMax;
    private double pourcent;

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getAgeMax() { return ageMax; }
    public void setAgeMax(int ageMax) { 
        if (ageMax < 0) throw new IllegalArgumentException("L'âge maximum ne peut être négatif");
        this.ageMax = ageMax; 
    }
    
    public double getPourcent() { return pourcent; }
    public void setPourcent(double pourcent) { 
        if (pourcent < 0 || pourcent > 100) {
            throw new IllegalArgumentException("Le pourcentage doit être entre 0 et 100");
        }
        this.pourcent = pourcent; 
    }

    // CRUD Methods
    public static PrixVolEnfant getCurrent(Connection conn) throws SQLException {
        String query = "SELECT * FROM Prix_vol_enfant ORDER BY id DESC LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PrixVolEnfant pve = new PrixVolEnfant();
                    pve.setId(rs.getInt("id"));
                    pve.setAgeMax(rs.getInt("age_max"));
                    pve.setPourcent(rs.getDouble("pourcent"));
                    return pve;
                }
            }
        }
        return null;
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

    public static boolean estEnfant(Connection conn, int age) throws SQLException {
        if (age < 0) return false;
        
        PrixVolEnfant pve = PrixVolEnfant.getCurrent(conn);
        if (pve == null) {
            throw new SQLException("Aucune règle de prix enfant définie dans la base");
        }
        
        return age <= pve.getAgeMax();
    }
    
    public static double getPourcentReductionEnfant(Connection conn, int age) throws SQLException {
        if (!estEnfant(conn, age)) {
            return 0; 
        }
        PrixVolEnfant pve = PrixVolEnfant.getCurrent(conn);
        return pve.getPourcent();
    }
}