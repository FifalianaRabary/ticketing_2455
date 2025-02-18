package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import annotations.*;
import myconnection.MyConnection;
import session.MySession;
import utils.ModelView;

@Controller
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
        String query = "SELECT p.prix FROM Prix_siege_vol p WHERE p.id_type_siege = ? AND v.id = ?";
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

    public void insertPrixSiege(Connection conn, int idVol, int idTypeSiege, double prix) throws SQLException {
        String query = "INSERT INTO Prix_siege_vol (id_vol, id_type_siege, prix) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idVol);
            stmt.setInt(2, idTypeSiege);
            stmt.setDouble(3, prix);
            stmt.executeUpdate();
        }
    }
    


    @Url(url="/vol/formVol")
    public ModelView goToFormVol() {

        try (Connection conn = MyConnection.getConnection()) {
            HashMap<String,Object> map = new HashMap<>();
            
            List<Avion> avions = Avion.getAll(conn);
            List<Ville> villes = Ville.getAll(conn);
            
            map.put("villes", villes);
            map.put("avions", avions);

            String url = "/backOffice/formulaireVol.jsp";
            ModelView mv = new ModelView(url,map);
        return mv;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

  
    @Url(url="/vol/listVol")
    public ModelView goToListVol() {

        try (Connection conn = MyConnection.getConnection()) {
            HashMap<String,Object> map = new HashMap<>();
            

            List<Vol> vols = Vol.getAll(conn);
            
            map.put("vols",vols);
            map.put("connection",conn);


            String url = "/backOffice/listeVol.jsp";
            ModelView mv = new ModelView(url,map);
        return mv;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    @Post()
    @Url(url="/vol/insert")
    public ModelView insertVol(@Argument(name="vol") Vol vol, @Argument(name="prixEconomique") double prixEconomique, @Argument(name="prixBusiness") double prixBusiness) {
    
        vol.printVol();
        System.out.println("prix eco : " + prixEconomique);
        System.out.println("prix business : " + prixBusiness);
    
        Connection conn = null;
        try {
            conn = MyConnection.getConnection();
            conn.setAutoCommit(false); // Désactiver l'auto-commit
    
            if (vol != null && prixEconomique > 0 && prixBusiness > 0) {
                System.out.println("IF");
    
                int idVol = vol.insertReturningId(conn);
    
                // Insertion des prix pour les sièges en économique et business
                int id_type_eco = 1;
                int id_type_business = 2;
                insertPrixSiege(conn, idVol, id_type_eco, prixEconomique);
                insertPrixSiege(conn, idVol, id_type_business, prixBusiness);
    
                conn.commit(); // Valider la transaction
    
                HashMap<String, Object> data = new HashMap<>();
                String url = "/backOffice/dashboard.jsp";
                return new ModelView(url, data);
        } else {
                System.out.println("ELSE");
                HashMap<String, Object> data = new HashMap<>();
                data.put("error", "Invalid credentials");
                String url = "/backOffice/dashboard.jsp";
                return new ModelView(url, data);
        }
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Annuler la transaction en cas d'erreur
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            HashMap<String, Object> data = new HashMap<>();
            data.put("error", "Une erreur s'est produite.");
            String url = "/backOffice/dashboard.jsp";
            return new ModelView(url, data);

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Rétablir l'auto-commit
                    conn.close(); // Fermer la connexion
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }
  
    
}
