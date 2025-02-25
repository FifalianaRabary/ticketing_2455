package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.plaf.nimbus.State;

import annotations.*;
import myconnection.MyConnection;
import session.MySession;
import utils.ModelView;

import java.text.SimpleDateFormat;


public class Vol {
    private int id;
    private String designation;
    private Timestamp dateHeureDepart;
    private Timestamp dateHeureArrivee;
    private int idAvion;
    private int idVilleDepart;
    private int idVilleArrivee;

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public Timestamp getDateHeureDepart() { return dateHeureDepart; }
    public void setDateHeureDepart(Timestamp dateHeureDepart) { this.dateHeureDepart = dateHeureDepart; }

    public Timestamp getDateHeureArrivee() { return dateHeureArrivee; }
    public void setDateHeureArrivee(Timestamp dateHeureArrivee) { this.dateHeureArrivee = dateHeureArrivee; }

    public int getIdAvion() { return idAvion; }
    public void setIdAvion(int idAvion) { this.idAvion = idAvion; }

    public int getIdVilleDepart() { return idVilleDepart; }
    public void setIdVilleDepart(int idVilleDepart) { this.idVilleDepart = idVilleDepart; }

    public int getIdVilleArrivee() { return idVilleArrivee; }
    public void setIdVilleArrivee(int idVilleArrivee) { this.idVilleArrivee = idVilleArrivee; }

    public void printVol() {
        System.out.println("ID: " + id);
        System.out.println("Désignation: " + designation);
        System.out.println("Date et heure de départ: " + dateHeureDepart);
        System.out.println("Date et heure d'arrivée: " + dateHeureArrivee);
        System.out.println("ID Avion: " + idAvion);
        System.out.println("ID Ville de départ: " + idVilleDepart);
        System.out.println("ID Ville d'arrivée: " + idVilleArrivee);
    }

    public String getVolInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append("\n");
        sb.append("Désignation: ").append(designation).append("\n");
        sb.append("Date et heure de départ: ").append(dateHeureDepart).append("\n");
        sb.append("Date et heure d'arrivée: ").append(dateHeureArrivee).append("\n");
        sb.append("ID Avion: ").append(idAvion).append("\n");
        sb.append("ID Ville de départ: ").append(idVilleDepart).append("\n");
        sb.append("ID Ville d'arrivée: ").append(idVilleArrivee).append("\n");
        return sb.toString();
    }
    


    public static List<Vol> rechercherVols(Connection conn, Timestamp dateDepart, Timestamp dateArrivee, 
                                       Integer idVilleDepart, Integer idVilleArrivee, 
                                       Double prixMinEco, Double prixMaxEco, 
                                       Double prixMinBusiness, Double prixMaxBusiness, 
                                        Integer idAvion) throws SQLException {
    List<Vol> resultats = new ArrayList<>();
    
    String query = "SELECT DISTINCT v.* FROM Vol v " +
                   "JOIN Avion a ON v.id_avion = a.id " +
                   "JOIN Prix_siege_vol psv ON v.id = psv.id_vol " +
                   "WHERE 1=1 "; 

    List<Object> params = new ArrayList<>();

    if (dateDepart != null) {
        query += "AND DATE(v.date_heure_depart) = ? ";
        params.add(new java.sql.Date(dateDepart.getTime())); 
    }
    if (dateArrivee != null) {
        query += "AND DATE(v.date_heure_arrivee) = ? ";
        params.add(new java.sql.Date(dateArrivee.getTime())); 
    }
    
    if (idVilleDepart != null && idVilleDepart>0) {
        query += "AND v.id_ville_depart = ? ";
        params.add(idVilleDepart);
    }
    if (idVilleArrivee != null && idVilleArrivee>0) {
        query += "AND v.id_ville_arrivee = ? ";
        params.add(idVilleArrivee);
    }
    if (prixMinEco != null && prixMinEco>0) {
        query += "AND psv.id_type_siege = 1 AND psv.prix >= ? ";
        params.add(prixMinEco);
    }
    if (prixMaxEco != null && prixMaxEco >0 ) {
        query += "AND psv.id_type_siege = 1 AND psv.prix <= ? ";
        params.add(prixMaxEco);
    }
    if (prixMinBusiness != null && prixMinBusiness>0) {
        query += "AND psv.id_type_siege = 2 AND psv.prix >= ? ";
        params.add(prixMinBusiness);
    }
    if (prixMaxBusiness != null && prixMaxBusiness>0) {
        query += "AND psv.id_type_siege = 2 AND psv.prix <= ? ";
        params.add(prixMaxBusiness);
    }
    if (idAvion != null && idAvion>0) {
        query += "AND a.id = ? ";
        params.add(idAvion);
    }

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i));
        }

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Vol vol = new Vol();
                vol.setId(rs.getInt("id"));
                vol.setDesignation(rs.getString("designation"));
                vol.setDateHeureDepart(rs.getTimestamp("date_heure_depart"));
                vol.setDateHeureArrivee(rs.getTimestamp("date_heure_arrivee"));
                vol.setIdAvion(rs.getInt("id_avion"));
                vol.setIdVilleDepart(rs.getInt("id_ville_depart"));
                vol.setIdVilleArrivee(rs.getInt("id_ville_arrivee"));
                resultats.add(vol);
            }
        }
    }
    return resultats;
}

    

    // Méthode pour insérer un vol
    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Vol (designation, date_heure_depart, date_heure_arrivee, id_avion, id_ville_depart, id_ville_arrivee) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, this.designation);
            stmt.setTimestamp(2, this.dateHeureDepart);
            stmt.setTimestamp(3, this.dateHeureArrivee);
            stmt.setInt(4, this.idAvion);
            stmt.setInt(5, this.idVilleDepart);
            stmt.setInt(6, this.idVilleArrivee);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    public static void insertPlaceDispo(Connection conn, int id_vol, int id_avion) throws SQLException {
        int id_siege_eco = 1;
        int id_siege_business = 2;
        
        int siege_eco = Avion.getNbSieges(conn, id_avion, id_siege_eco);
        int siege_business = Avion.getNbSieges(conn, id_avion, id_siege_business);
    
        String query = "INSERT INTO place_dispo_vol (id_vol, id_type_siege, nb) VALUES (?, ?, ?)";
    
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            // Insérer sièges économiques
            stmt.setInt(1, id_vol);
            stmt.setInt(2, id_siege_eco);
            stmt.setInt(3, siege_eco);
            stmt.executeUpdate();
            
            // Insérer sièges business
            stmt.setInt(1, id_vol);
            stmt.setInt(2, id_siege_business);
            stmt.setInt(3, siege_business);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw e; // Remonter l'erreur pour que insertVol gère le rollback
        }
    }

    public static void deletePlaceDispoByVol(Connection conn, int id_vol) throws SQLException {
        String query = "DELETE FROM place_dispo_vol WHERE id_vol = ?";
    
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id_vol);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e; // Remonter l'exception pour une meilleure gestion des erreurs
        }
    }
    
    public static void deletePrixSiegeByVol(Connection conn, int id_vol) throws SQLException {
        String query = "DELETE FROM prix_siege_vol WHERE id_vol = ?";
    
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id_vol);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e; // Remonter l'exception pour une meilleure gestion des erreurs
        }
    }
    
    
    

    public int insertReturningId(Connection conn) throws SQLException {
        String query = "INSERT INTO Vol (designation, date_heure_depart, date_heure_arrivee, id_avion, id_ville_depart, id_ville_arrivee) " +
                       "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, this.designation);
            stmt.setTimestamp(2, this.dateHeureDepart);
            stmt.setTimestamp(3, this.dateHeureArrivee);
            stmt.setInt(4, this.idAvion);
            stmt.setInt(5, this.idVilleDepart);
            stmt.setInt(6, this.idVilleArrivee);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.id = rs.getInt("id");
                    return this.id;
                }
            }
        }
        return -1; // Retourne -1 si l'insertion échoue
    }
    

    // Méthode pour récupérer un vol par son ID
    public static Vol getById(Connection conn, int id) throws SQLException {
        String query = "SELECT * FROM Vol WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Vol vol = new Vol();
                    vol.setId(rs.getInt("id"));
                    vol.setDesignation(rs.getString("designation"));
                    vol.setDateHeureDepart(rs.getTimestamp("date_heure_depart"));
                    vol.setDateHeureArrivee(rs.getTimestamp("date_heure_arrivee"));
                    vol.setIdAvion(rs.getInt("id_avion"));
                    vol.setIdVilleDepart(rs.getInt("id_ville_depart"));
                    vol.setIdVilleArrivee(rs.getInt("id_ville_arrivee"));
                    return vol;
                }
            }
        }
        return null;
    }

    // Méthode pour récupérer tous les vols
    public static List<Vol> getAll(Connection conn) throws SQLException {
        List<Vol> vols = new ArrayList<>();
        String query = "SELECT * FROM Vol";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Vol vol = new Vol();
                vol.setId(rs.getInt("id"));
                vol.setDesignation(rs.getString("designation"));
                vol.setDateHeureDepart(rs.getTimestamp("date_heure_depart"));
                vol.setDateHeureArrivee(rs.getTimestamp("date_heure_arrivee"));
                vol.setIdAvion(rs.getInt("id_avion"));
                vol.setIdVilleDepart(rs.getInt("id_ville_depart"));
                vol.setIdVilleArrivee(rs.getInt("id_ville_arrivee"));
                vols.add(vol);
            }
        }
        return vols;
    }

    // Méthode pour mettre à jour un vol
    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Vol SET designation = ?, date_heure_depart = ?, date_heure_arrivee = ?, id_avion = ?, id_ville_depart = ?, id_ville_arrivee = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, this.designation);
            stmt.setTimestamp(2, this.dateHeureDepart);
            stmt.setTimestamp(3, this.dateHeureArrivee);
            stmt.setInt(4, this.idAvion);
            stmt.setInt(5, this.idVilleDepart);
            stmt.setInt(6, this.idVilleArrivee);
            stmt.setInt(7, this.id);
            stmt.executeUpdate();
        }
    }

    // Méthode pour supprimer un vol
    public static void delete(Connection conn, int id) throws SQLException {
        String query = "DELETE FROM Vol WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


    

    public  double getPrixSiege(Connection conn, int idTypeSiege) throws SQLException {
        String query = "SELECT p.prix FROM Prix_siege_vol p WHERE p.id_type_siege = ? AND p.id_vol = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idTypeSiege);
            stmt.setInt(2, this.id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("prix");
                }
            }
        }
        return -1; // Retourne -1 si aucun prix n'est trouvé
    }

    public static void insertPrixSiege(Connection conn, int idVol, int idTypeSiege, double prix) throws SQLException {
        String query = "INSERT INTO Prix_siege_vol (id_vol, id_type_siege, prix) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idVol);
            stmt.setInt(2, idTypeSiege);
            stmt.setDouble(3, prix);
            stmt.executeUpdate();
        }
    }

    public static void updatePrixSiege(Connection conn, int idVol, int idTypeSiege, double prix) throws SQLException {
        String query = "UPDATE Prix_siege_vol SET prix = ? WHERE id_vol = ? AND id_type_siege = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, prix);
            stmt.setInt(2, idVol);
            stmt.setInt(3, idTypeSiege);
            stmt.executeUpdate();
        }
    }
      

}
