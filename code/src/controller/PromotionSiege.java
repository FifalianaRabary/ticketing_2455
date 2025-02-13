package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import annotations.*;
import session.MySession;
import utils.ModelView;

@Controller
public class PromotionSiege {
    private int id;
    private double pourcent;
    private int idVol;
    private int idTypeSiege;
    private int nbSiege;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getPourcent() { return pourcent; }
    public void setPourcent(double pourcent) { this.pourcent = pourcent; }
    public int getIdVol() { return idVol; }
    public void setIdVol(int idVol) { this.idVol = idVol; }
    public int getIdTypeSiege() { return idTypeSiege; }
    public void setIdTypeSiege(int idTypeSiege) { this.idTypeSiege = idTypeSiege; }
    public int getNbSiege() { return nbSiege; }
    public void setNbSiege(int nbSiege) { this.nbSiege = nbSiege; }

    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Promotion_siege (pourcent, id_vol, id_type_siege, nb_siege) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, this.pourcent);
            stmt.setInt(2, this.idVol);
            stmt.setInt(3, this.idTypeSiege);
            stmt.setInt(4, this.nbSiege);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) this.id = rs.getInt(1);
            }
        }
    }

    public static List<PromotionSiege> getAll(Connection conn) throws SQLException {
        List<PromotionSiege> promotions = new ArrayList<>();
        String query = "SELECT * FROM Promotion_siege";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                PromotionSiege promo = new PromotionSiege();
                promo.setId(rs.getInt("id"));
                promo.setPourcent(rs.getDouble("pourcent"));
                promo.setIdVol(rs.getInt("id_vol"));
                promo.setIdTypeSiege(rs.getInt("id_type_siege"));
                promo.setNbSiege(rs.getInt("nb_siege"));
                promotions.add(promo);
            }
        }
        return promotions;
    }

    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Promotion_siege SET pourcent = ?, id_vol = ?, id_type_siege = ?, nb_siege = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, this.pourcent);
            stmt.setInt(2, this.idVol);
            stmt.setInt(3, this.idTypeSiege);
            stmt.setInt(4, this.nbSiege);
            stmt.setInt(5, this.id);
            stmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String query = "DELETE FROM Promotion_siege WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        }
    }
}
