package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import annotations.*;
import myconnection.MyConnection;
import session.MySession;
import utils.ModelView;

import java.text.SimpleDateFormat;


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

    public void insertPrixSiege(Connection conn, int idVol, int idTypeSiege, double prix) throws SQLException {
        String query = "INSERT INTO Prix_siege_vol (id_vol, id_type_siege, prix) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idVol);
            stmt.setInt(2, idTypeSiege);
            stmt.setDouble(3, prix);
            stmt.executeUpdate();
        }
    }

    public void updatePrixSiege(Connection conn, int idVol, int idTypeSiege, double prix) throws SQLException {
        String query = "UPDATE Prix_siege_vol SET prix = ? WHERE id_vol = ? AND id_type_siege = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, prix);
            stmt.setInt(2, idVol);
            stmt.setInt(3, idTypeSiege);
            stmt.executeUpdate();
        }
    }
    
    


    @Url(url="/vol/formVol")
    @Auth(level="admin")
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

    @Url(url="/vol/modifier")
    @Auth(level="admin")
    public ModelView goToFormUpdateVol(@Argument(name="id") int id) {

        try (Connection conn = MyConnection.getConnection()) {
            HashMap<String,Object> map = new HashMap<>();

            Vol vol = Vol.getById(conn, id);
            
            List<Avion> avions = Avion.getAll(conn);
            List<Ville> villes = Ville.getAll(conn);
            
            map.put("villes", villes);
            map.put("vol",vol);
            map.put("avions", avions);

            String url = "/backOffice/updateVol.jsp";
            ModelView mv = new ModelView(url,map);
        return mv;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

  
    @Url(url="/vol/listVol")
    @Auth(level="admin")
    public ModelView goToListVol() {

        try (Connection conn = MyConnection.getConnection()) {
            HashMap<String,Object> map = new HashMap<>();
            

            List<Vol> vols = Vol.getAll(conn);

            List<Avion> avions = Avion.getAll(conn);
            List<Ville> villes = Ville.getAll(conn);
            
            map.put("villes", villes);
            map.put("avions", avions);
            map.put("vols",vols);


            String url = "/backOffice/listeVol.jsp";
            ModelView mv = new ModelView(url,map);
        return mv;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    @Url(url="/vol/listVolFront")
    public ModelView goToListVolFront() {

        try (Connection conn = MyConnection.getConnection()) {
            HashMap<String,Object> map = new HashMap<>();
            

            List<Vol> vols = Vol.getAll(conn);

            List<Avion> avions = Avion.getAll(conn);
            List<Ville> villes = Ville.getAll(conn);
            
            map.put("villes", villes);
            map.put("avions", avions);
            map.put("vols",vols);


            String url = "/frontOffice/listeVol.jsp";
            ModelView mv = new ModelView(url,map);
        return mv;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    @Url(url="/vol/delete")
    @Auth(level="admin")
    public ModelView deleteVol(@Argument(name="id") int id) {

        try (Connection conn = MyConnection.getConnection()) {
            HashMap<String,Object> map = new HashMap<>();
            
            Vol.delete(conn, id);

            List<Vol> vols = Vol.getAll(conn);

            List<Avion> avions = Avion.getAll(conn);
            List<Ville> villes = Ville.getAll(conn);
            
            map.put("villes", villes);
            map.put("avions", avions);
            map.put("vols",vols);


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
    @Auth(level="admin")
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

    @Post()
    @Url(url="/vol/update")
    @Auth(level="admin")
    public ModelView updateVol(@Argument(name="vol") Vol vol, @Argument(name="prixEconomique") double prixEconomique, @Argument(name="prixBusiness") double prixBusiness) {
    
        vol.printVol();
        System.out.println("prix eco : " + prixEconomique);
        System.out.println("prix business : " + prixBusiness);
    
        Connection conn = null;
        try {
            conn = MyConnection.getConnection();
            conn.setAutoCommit(false); // Désactiver l'auto-commit
    
            if (vol != null && prixEconomique > 0 && prixBusiness > 0) {
                System.out.println("IF");

                vol.update(conn);

                int id_type_eco = 1;
                int id_type_business = 2;
                updatePrixSiege(conn, vol.getId(), id_type_eco, prixEconomique);
                updatePrixSiege(conn, vol.getId(), id_type_business, prixBusiness);
    
                conn.commit(); // Valider la transaction

                HashMap<String, Object> data = new HashMap<>();


                List<Vol> vols = Vol.getAll(conn);

                List<Avion> avions = Avion.getAll(conn);
                List<Ville> villes = Ville.getAll(conn);
                
                data.put("villes", villes);
                data.put("avions", avions);
                data.put("vols",vols);
    
                String url = "/backOffice/listeVol.jsp";
                return new ModelView(url, data);
        } else {
                System.out.println("ELSE");
                HashMap<String, Object> data = new HashMap<>();
                data.put("error", "Invalid credentials");
                String url = "/backOffice/listeVol.jsp";
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
            String url = "/backOffice/listeVol.jsp";
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

    // @Post()
    // @Url(url="/vol/filter")
    // public String filtrerVol( 
    // @Argument(name="dateDepart") String dateDepart,
    // @Argument(name="dateArrivee") String dateArrivee,
    // @Argument(name="villeDepart") int villeDepart,
    // @Argument(name="villeArrivee") int villeArrivee,
    // @Argument(name="prixMinEco") double prixMinEco,
    // @Argument(name="prixMaxEco") double prixMaxEco,
    // @Argument(name="prixMinBusiness") double prixMinBusiness,
    // @Argument(name="prixMaxBusiness") double prixMaxBusiness,
    // @Argument(name="avion") int avion
    // ) {
    //     String tout = "";
        
    //     // Affichage des paramètres
    //     tout += "dateDepart: " + dateDepart + "\n";
    //     tout += "dateArrivee: " + dateArrivee + "\n";
    //     tout += "villeDepart: " + villeDepart + "\n";
    //     tout += "villeArrivee: " + villeArrivee + "\n";
    //     tout += "prixMinEco: " + prixMinEco + "\n";
    //     tout += "prixMaxEco: " + prixMaxEco + "\n";
    //     tout += "prixMinBusiness: " + prixMinBusiness + "\n";
    //     tout += "prixMaxBusiness: " + prixMaxBusiness + "\n";
    //     tout += "avion: " + avion + "\n";
        
    //     // Retourner les informations sous forme de chaîne
    //     return tout;
    // }




    @Post()
    @Url(url="/vol/filter")
    @Auth(level="admin")
    public ModelView filtrerVol( 
    @Argument(name="dateDepart") String dateDepart,
    @Argument(name="dateArrivee") String dateArrivee,
    @Argument(name="villeDepart") int villeDepart,
    @Argument(name="villeArrivee") int villeArrivee,
    @Argument(name="prixMinEco") double prixMinEco,
    @Argument(name="prixMaxEco") double prixMaxEco,
    @Argument(name="prixMinBusiness") double prixMinBusiness,
    @Argument(name="prixMaxBusiness") double prixMaxBusiness,
    @Argument(name="avion") int avion
    ) {
        String tout = "";
        
        // Affichage des paramètres
        tout += "dateDepart: " + dateDepart + "\n";
        tout += "dateArrivee: " + dateArrivee + "\n";
        tout += "villeDepart: " + villeDepart + "\n";
        tout += "villeArrivee: " + villeArrivee + "\n";
        tout += "prixMinEco: " + prixMinEco + "\n";
        tout += "prixMaxEco: " + prixMaxEco + "\n";
        tout += "prixMinBusiness: " + prixMinBusiness + "\n";
        tout += "prixMaxBusiness: " + prixMaxBusiness + "\n";
        tout += "avion: " + avion + "\n";
        
        System.out.println("TOUT :"+tout);

           System.out.println("DATE ARRIVEE : "+dateArrivee);
        System.out.println("DATE DEPART : "+dateDepart);
        List<Vol> volsFiltres = new ArrayList<>();

         // Définir un format de date qui correspond à tes chaînes de date
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
         dateFormat.setLenient(false); // Empêche les erreurs silencieuses
                     
            try (Connection conn = MyConnection.getConnection()) {

                Timestamp timestampDateDepart = null;
                Timestamp timestampDateArrivee = null;

        // Convertir les chaînes de caractères en Timestamp
        if(dateDepart !=null){
            timestampDateDepart = new Timestamp(dateFormat.parse(dateDepart).getTime());
        }            
        if(dateArrivee!=null){
             timestampDateArrivee = new Timestamp(dateFormat.parse(dateArrivee).getTime());

        }

    

        volsFiltres = Vol.rechercherVols(conn, timestampDateDepart, timestampDateArrivee, villeDepart, villeArrivee, prixMinEco, prixMaxEco, prixMinBusiness, prixMaxBusiness, avion);            
                HashMap<String, Object> data = new HashMap<>();
                List<Avion> avions = Avion.getAll(conn);
                List<Ville> villes = Ville.getAll(conn);
                
                data.put("villes", villes);
                data.put("avions", avions);
                data.put("vols", volsFiltres);
                
                String url = "/backOffice/listeVol.jsp";
                return new ModelView(url, data);
            } catch (Exception e) {
                e.printStackTrace();
                
                HashMap<String, Object> data = new HashMap<>();
                data.put("error", "Une erreur s'est produite.");
                
                String url = "/backOffice/dashboard.jsp";
                return new ModelView(url, data);
            }

    }



    @Post()
    @Url(url="/vol/filterFront")
    public ModelView filtrerVolFront( 
    @Argument(name="dateDepart") String dateDepart,
    @Argument(name="dateArrivee") String dateArrivee,
    @Argument(name="villeDepart") int villeDepart,
    @Argument(name="villeArrivee") int villeArrivee,
    @Argument(name="prixMinEco") double prixMinEco,
    @Argument(name="prixMaxEco") double prixMaxEco,
    @Argument(name="prixMinBusiness") double prixMinBusiness,
    @Argument(name="prixMaxBusiness") double prixMaxBusiness,
    @Argument(name="avion") int avion
    ) {
        String tout = "";
        
        // Affichage des paramètres
        tout += "dateDepart: " + dateDepart + "\n";
        tout += "dateArrivee: " + dateArrivee + "\n";
        tout += "villeDepart: " + villeDepart + "\n";
        tout += "villeArrivee: " + villeArrivee + "\n";
        tout += "prixMinEco: " + prixMinEco + "\n";
        tout += "prixMaxEco: " + prixMaxEco + "\n";
        tout += "prixMinBusiness: " + prixMinBusiness + "\n";
        tout += "prixMaxBusiness: " + prixMaxBusiness + "\n";
        tout += "avion: " + avion + "\n";
        
        System.out.println("TOUT :"+tout);

           System.out.println("DATE ARRIVEE : "+dateArrivee);
        System.out.println("DATE DEPART : "+dateDepart);
        List<Vol> volsFiltres = new ArrayList<>();

         // Définir un format de date qui correspond à tes chaînes de date
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
         dateFormat.setLenient(false); // Empêche les erreurs silencieuses
                     
            try (Connection conn = MyConnection.getConnection()) {

                Timestamp timestampDateDepart = null;
                Timestamp timestampDateArrivee = null;

        // Convertir les chaînes de caractères en Timestamp
        if(dateDepart !=null){
            timestampDateDepart = new Timestamp(dateFormat.parse(dateDepart).getTime());
        }            
        if(dateArrivee!=null){
             timestampDateArrivee = new Timestamp(dateFormat.parse(dateArrivee).getTime());

        }

    

        volsFiltres = Vol.rechercherVols(conn, timestampDateDepart, timestampDateArrivee, villeDepart, villeArrivee, prixMinEco, prixMaxEco, prixMinBusiness, prixMaxBusiness, avion);            
                HashMap<String, Object> data = new HashMap<>();
                List<Avion> avions = Avion.getAll(conn);
                List<Ville> villes = Ville.getAll(conn);
                
                data.put("villes", villes);
                data.put("avions", avions);
                data.put("vols", volsFiltres);
                
                String url = "/frontOffice/listeVol.jsp";
                return new ModelView(url, data);
            } catch (Exception e) {
                e.printStackTrace();
                
                HashMap<String, Object> data = new HashMap<>();
                data.put("error", "Une erreur s'est produite.");
                
                String url = "/frontOffice/dashboard.jsp";
                return new ModelView(url, data);
            }

    }

   

  
    
}
