package model;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import model.Reservation;


public class DetailReservation {
    private int id;
    private int idReservation;
    private int idTypeSiege;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private String passeport;
    private Double montant;

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getIdReservation() { return idReservation; }
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }
    
    public int getIdTypeSiege() { return idTypeSiege; }
    public void setIdTypeSiege(int idTypeSiege) { this.idTypeSiege = idTypeSiege; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public Date getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance; }
    
    public String getPasseport() { return passeport; }
    public void setPasseport(String passeport) { this.passeport = passeport; }
    
    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { 
        if (montant < 0) throw new IllegalArgumentException("Montant invalide");
        this.montant = montant; 
    }

    // CRUD Methods
    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Detail_reservation (id_reservation, id_type_siege, nom, prenom, date_naissance, passeport, montant) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, this.idReservation);
            stmt.setInt(2, this.idTypeSiege);
            stmt.setString(3, this.nom);
            stmt.setString(4, this.prenom);
            stmt.setDate(5, new java.sql.Date(this.dateNaissance.getTime()));
            stmt.setString(6, this.passeport);
            stmt.setDouble(7, this.montant);
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    public static DetailReservation getById(Connection conn, int id) throws SQLException {
        String query = "SELECT * FROM Detail_reservation WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    DetailReservation detail = new DetailReservation();
                    detail.setId(rs.getInt("id"));
                    detail.setIdReservation(rs.getInt("id_reservation"));
                    detail.setIdTypeSiege(rs.getInt("id_type_siege"));
                    detail.setNom(rs.getString("nom"));
                    detail.setPrenom(rs.getString("prenom"));
                    detail.setDateNaissance(rs.getDate("date_naissance"));
                    detail.setPasseport(rs.getString("passeport"));
                    detail.setMontant(rs.getDouble("montant"));
                    return detail;
                }
            }
        }
        return null;
    }

    public static List<DetailReservation> getAll(Connection conn) throws SQLException {
        List<DetailReservation> details = new ArrayList<>();
        String query = "SELECT * FROM Detail_reservation";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                DetailReservation detail = new DetailReservation();
                detail.setId(rs.getInt("id"));
                detail.setIdReservation(rs.getInt("id_reservation"));
                detail.setIdTypeSiege(rs.getInt("id_type_siege"));
                detail.setNom(rs.getString("nom"));
                detail.setPrenom(rs.getString("prenom"));
                detail.setDateNaissance(rs.getDate("date_naissance"));
                detail.setPasseport(rs.getString("passeport"));
                detail.setMontant(rs.getDouble("montant"));
                details.add(detail);
            }
        }
        return details;
    }

    public static List<DetailReservation> getByReservationId(Connection conn, int idReservation) throws SQLException {
        List<DetailReservation> details = new ArrayList<>();
        String query = "SELECT * FROM Detail_reservation WHERE id_reservation = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idReservation);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DetailReservation detail = new DetailReservation();
                    detail.setId(rs.getInt("id"));
                    detail.setIdReservation(rs.getInt("id_reservation"));
                    detail.setIdTypeSiege(rs.getInt("id_type_siege"));
                    detail.setNom(rs.getString("nom"));
                    detail.setPrenom(rs.getString("prenom"));
                    detail.setDateNaissance(rs.getDate("date_naissance"));
                    detail.setPasseport(rs.getString("passeport"));
                    detail.setMontant(rs.getDouble("montant"));
                    details.add(detail);
                }
            }
        }
        return details;
    }

    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Detail_reservation SET id_type_siege = ?, nom = ?, prenom = ?, " +
                      "date_naissance = ?, passeport = ?, montant = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.idTypeSiege);
            stmt.setString(2, this.nom);
            stmt.setString(3, this.prenom);
            stmt.setDate(4, new java.sql.Date(this.dateNaissance.getTime()));
            stmt.setString(5, this.passeport);
            stmt.setDouble(6, this.montant);
            stmt.setInt(7, this.id);
            stmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String query = "DELETE FROM Detail_reservation WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        }
    }

    public static boolean estSiegeDisponible(Connection conn, int idReservation, int idTypeSiege) throws SQLException {
        Reservation reservation = Reservation.getById(conn, idReservation);
        if (reservation == null) {
            throw new SQLException("Réservation introuvable");
        }
    
        String query = "SELECT nb > 0 FROM Place_dispo_vol " +
                     "WHERE id_vol = ? AND id_type_siege = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reservation.getIdVol());
            stmt.setInt(2, idTypeSiege);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        }
        
        // 3. Si aucune entrée trouvée, considérer comme indisponible
        return false;
    }

    // public static boolean estSiegeDisponible(Connection conn, int idReservation, int idTypeSiege) throws SQLException {
    //     Reservation reservation = Reservation.getById(conn, idReservation);
    
    //     String query = "SELECT places_restantes > 0 " +
    //                  "FROM places_disponibles_par_type " +
    //                  "WHERE id_vol = ? AND id_type_siege = ?";
        
    //     try (PreparedStatement stmt = conn.prepareStatement(query)) {
    //         stmt.setInt(1, reservation.getIdVol());
    //         stmt.setInt(2, idTypeSiege);
            
    //         try (ResultSet rs = stmt.executeQuery()) {
    //             if (rs.next()) {
    //                 return rs.getBoolean(1);
    //             }
    //         }
    //     }
    //     return false;
    // }

    public int getAge() 
    {
        if (this.dateNaissance == null) {
            return -1;
        }
        
        LocalDate naissance = new java.sql.Date(this.dateNaissance.getTime()).toLocalDate();
        LocalDate aujourdhui = LocalDate.now();
        
        if (naissance.isAfter(aujourdhui)) {
            return -1;
        }
        
        return Period.between(naissance, aujourdhui).getYears();
    }





    public double calculerMontant(Connection conn) throws SQLException{
        Reservation reservation = Reservation.getById(conn, idReservation);
        Vol vol = Vol.getById(conn, reservation.getIdVol());
        int age = getAge();
        PromotionSiege promo = Vol.getPromotionSiDispo(conn, vol.getId(), this.getIdTypeSiege());
        double prix = vol.getPrixSiege(conn, this.getIdTypeSiege());

        double promoEnfant = PrixVolEnfant.getPourcentReductionEnfant(conn, age);
        if(promoEnfant!=0){
            prix = prix -( prix * (promoEnfant / 100.0));
        }
        if (promo != null) {
            prix = prix -( prix * (promo.getPourcent() / 100.0));
        } 


        return prix;
        
    }

    public boolean estEnfant(Connection conn) throws SQLException{
        int age = getAge();
        return PrixVolEnfant.estEnfant(conn, age);
    }

   

    public boolean estCompleteAdulte(Connection conn) throws SQLException{
        Reservation reservation = Reservation.getById(conn, idReservation);
        int nbAdulte = reservation.countAdultesByReservation(conn);
        if(nbAdulte == reservation.getNbAdulte()){
            return true;
        }
        return false;

    }

    public boolean estCompleteEnfant(Connection conn) throws SQLException{
        Reservation reservation = Reservation.getById(conn, idReservation);
        int nbEnfant = reservation.countEnfantByReservation(conn);
        if(nbEnfant == reservation.getNbEnfant()){
            return true;
        }
        return false;

    }

    public boolean estCompleteReservationApres(Connection conn) throws SQLException{
        Reservation reservation = Reservation.getById(conn, idReservation);
        int nbAdulte = reservation.countAdultesByReservation(conn);
         int nbEnfant = reservation.countEnfantByReservation(conn);

        if(estEnfant(conn)){
            nbEnfant +=1;
        }
        else{
            nbAdulte +=1;
        }

        if(nbAdulte == reservation.getNbAdulte() && nbEnfant == reservation.getNbEnfant()){
            return true;
        }
        return false;


    }


    public void effectuerReservation(Connection conn) throws SQLException{
        Reservation reservation = Reservation.getById(conn, getIdReservation());
        this.setMontant(calculerMontant(conn));
        insert(conn);
        Vol.decrementerNbPlaceDispo(conn, reservation.getIdVol(),getIdTypeSiege());

    }

    public void deleteDetail(Connection conn) throws SQLException{
        Reservation reservation = Reservation.getById(conn, getIdReservation());
        reservation.setComplete(false);

        reservation.update(conn);

        Vol.incrementerNbPlaceDispo(conn, reservation.getIdVol(), getIdTypeSiege());

        delete(conn);
    }
   
}