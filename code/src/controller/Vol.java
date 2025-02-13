package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import annotations.*;
import session.MySession;
import utils.ModelView;

@Controller
public class Vol {
    private int id;
    private Timestamp dateHeureDepart;
    private Timestamp dateHeureArrivee;
    private int idAvion;
    private int idVille;
    private double prixBusiness;
    private double prixEco;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDateHeureDepart() {
        return dateHeureDepart;
    }

    public void setDateHeureDepart(Timestamp dateHeureDepart) {
        this.dateHeureDepart = dateHeureDepart;
    }

    public Timestamp getDateHeureArrivee() {
        return dateHeureArrivee;
    }

    public void setDateHeureArrivee(Timestamp dateHeureArrivee) {
        this.dateHeureArrivee = dateHeureArrivee;
    }

    public int getIdAvion() {
        return idAvion;
    }

    public void setIdAvion(int idAvion) {
        this.idAvion = idAvion;
    }

    public int getIdVille() {
        return idVille;
    }

    public void setIdVille(int idVille) {
        this.idVille = idVille;
    }

    public double getPrixBusiness() {
        return prixBusiness;
    }

    public void setPrixBusiness(double prixBusiness) {
        this.prixBusiness = prixBusiness;
    }

    public double getPrixEco() {
        return prixEco;
    }

    public void setPrixEco(double prixEco) {
        this.prixEco = prixEco;
    }

    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Vol (date_heure_depart, date_heure_arrivee, id_avion, id_ville, prix_business, prix_eco) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, this.dateHeureDepart);
            stmt.setTimestamp(2, this.dateHeureArrivee);
            stmt.setInt(3, this.idAvion);
            stmt.setInt(4, this.idVille);
            stmt.setDouble(5, this.prixBusiness);
            stmt.setDouble(6, this.prixEco);
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
                    vol.setPrixBusiness(rs.getDouble("prix_business"));
                    vol.setPrixEco(rs.getDouble("prix_eco"));
                    return vol;
                }
            }
        }
        return null;
    }
}
