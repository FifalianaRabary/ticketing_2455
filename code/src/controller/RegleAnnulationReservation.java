package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import annotations.*;
import session.MySession;
import utils.ModelView;

@Controller
public class RegleAnnulationReservation {
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
        String query = "INSERT INTO Regle_annulation_reservation (id_vol, nb_heure_limite_avant_vol) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, this.idVol);
            stmt.setInt(2, this.nbHeureLimiteAvantVol);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) this.id = rs.getInt(1);
            }
        }
    }

    public static List<RegleAnnulationReservation> getAll(Connection conn) throws SQLException {
        List<RegleAnnulationReservation> regles = new ArrayList<>();
        String query = "SELECT * FROM Regle_annulation_reservation";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                RegleAnnulationReservation regle = new RegleAnnulationReservation();
                regle.setId(rs.getInt("id"));
                regle.setIdVol(rs.getInt("id_vol"));
                regle.setNbHeureLimiteAvantVol(rs.getInt("nb_heure_limite_avant_vol"));
                regles.add(regle);
            }
        }
        return regles;
    }

    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Regle_annulation_reservation SET id_vol = ?, nb_heure_limite_avant_vol = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.idVol);
            stmt.setInt(2, this.nbHeureLimiteAvantVol);
            stmt.setInt(3, this.id);
            stmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String query = "DELETE FROM Regle_annulation_reservation WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        }
    }
}
