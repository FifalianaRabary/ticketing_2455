package model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.DetailReservation;


public class Reservation {
    private int id;
    private int idUser;
    private Timestamp dateHeureReservation;
    private int idVol;
    private boolean complete = false;
    private int nbAdulte = 1;
    private int nbEnfant = 0;

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }
    
    public Timestamp getDateHeureReservation() { return dateHeureReservation; }
    public void setDateHeureReservation(Timestamp dateHeureReservation) { 
        this.dateHeureReservation = dateHeureReservation; 
    }
    
    public int getIdVol() { return idVol; }
    public void setIdVol(int idVol) { this.idVol = idVol; }
    
    public boolean isComplete() { return complete; }
    public void setComplete(boolean complete) { this.complete = complete; }
    
    public int getNbAdulte() { return nbAdulte; }
    public void setNbAdulte(int nbAdulte) { 
        if (nbAdulte < 0) throw new IllegalArgumentException("Nombre d'adultes invalide");
        this.nbAdulte = nbAdulte; 
    }
    
    public int getNbEnfant() { return nbEnfant; }
    public void setNbEnfant(int nbEnfant) { 
        if (nbEnfant < 0) throw new IllegalArgumentException("Nombre d'enfants invalide");
        this.nbEnfant = nbEnfant; 
    }

    // Méthodes utilitaires
    public int getTotalPassagers() {
        return nbAdulte + nbEnfant;
    }

    public void setReservationDateNow() {
        setDateHeureReservation(new Timestamp(System.currentTimeMillis())); 
    }

    // CRUD Methods
    public int insertReturningId(Connection conn) throws SQLException {
        String query = "INSERT INTO Reservation (id_user, date_heure_reservation, id_vol, complete, nb_adulte, nb_enfant) " +
                      "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.idUser);
            stmt.setTimestamp(2, this.dateHeureReservation);
            stmt.setInt(3, this.idVol);
            stmt.setBoolean(4, this.complete);
            stmt.setInt(5, this.nbAdulte);
            stmt.setInt(6, this.nbEnfant);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                    return this.id;
                }
                throw new SQLException("Échec de l'insertion, aucun ID généré");
            }
        }
    }

    public static Reservation getById(Connection conn, int id) throws SQLException {
        String query = "SELECT * FROM Reservation WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Reservation res = new Reservation();
                    res.setId(rs.getInt("id"));
                    res.setIdUser(rs.getInt("id_user"));
                    res.setDateHeureReservation(rs.getTimestamp("date_heure_reservation"));
                    res.setIdVol(rs.getInt("id_vol"));
                    res.setComplete(rs.getBoolean("complete"));
                    res.setNbAdulte(rs.getInt("nb_adulte"));
                    res.setNbEnfant(rs.getInt("nb_enfant"));
                    return res;
                }
            }
        }
        return null;
    }

    public static List<Reservation> getAll(Connection conn) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM Reservation";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Reservation res = new Reservation();
                res.setId(rs.getInt("id"));
                res.setIdUser(rs.getInt("id_user"));
                res.setDateHeureReservation(rs.getTimestamp("date_heure_reservation"));
                res.setIdVol(rs.getInt("id_vol"));
                res.setComplete(rs.getBoolean("complete"));
                res.setNbAdulte(rs.getInt("nb_adulte"));
                res.setNbEnfant(rs.getInt("nb_enfant"));
                reservations.add(res);
            }
        }
        return reservations;
    }

    public static List<Reservation> getByUserId(Connection conn, int userId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM Reservation WHERE id_user = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reservation res = new Reservation();
                    res.setId(rs.getInt("id"));
                    res.setIdUser(rs.getInt("id_user"));
                    res.setDateHeureReservation(rs.getTimestamp("date_heure_reservation"));
                    res.setIdVol(rs.getInt("id_vol"));
                    res.setComplete(rs.getBoolean("complete"));
                    res.setNbAdulte(rs.getInt("nb_adulte"));
                    res.setNbEnfant(rs.getInt("nb_enfant"));
                    reservations.add(res);
                }
            }
        }
        return reservations;
    }

    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Reservation SET id_user = ?, id_vol = ?, complete = ?, nb_adulte = ?, nb_enfant = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.idUser);
            stmt.setInt(2, this.idVol);
            stmt.setBoolean(3, this.complete);
            stmt.setInt(4, this.nbAdulte);
            stmt.setInt(5, this.nbEnfant);
            stmt.setInt(6, this.id);
            stmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String query = "DELETE FROM Reservation WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        }
    }






    // ilaina
    // public boolean estPossible(Connection conn) throws SQLException {
    //     int totalPassagers = getTotalPassagers();
    //     if (totalPassagers <= 0) {
    //         return false;
    //     }
    
    //     String query = "SELECT places_restantes >= ? FROM places_disponibles_vol WHERE id_vol = ?";
        
    //     try (PreparedStatement stmt = conn.prepareStatement(query)) {
    //         stmt.setInt(1, totalPassagers);
    //         stmt.setInt(2, this.idVol);
            
    //         try (ResultSet rs = stmt.executeQuery()) {
    //             if (rs.next()) {
    //                 return rs.getBoolean(1);
    //             }
    //         }
    //     }
        
    //     return false;
    // }

    public boolean estPossible(Connection conn) throws SQLException {
        int totalPassagers = getTotalPassagers();
        if (totalPassagers <= 0) {
            return false;
        }
    
        // Requête pour sommer toutes les places disponibles pour ce vol
        String query = "SELECT COALESCE(SUM(nb), 0) AS places_restantes " +
                       "FROM Place_dispo_vol " +
                       "WHERE id_vol = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.idVol);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int placesRestantes = rs.getInt("places_restantes");
                    return placesRestantes >= totalPassagers;
                }
            }
        }
        
        return false;
    }

    
    public boolean estDansLesTemps(Connection conn) throws Exception
    {
        Vol vol = Vol.getById(conn, this.getIdVol());
        RegleReservation regleReservation = RegleReservation.getByIdVol(conn, this.getIdVol());

        if (vol == null || regleReservation == null) {
            System.out.println("regle de reservation inexistante");
            return false; 
        }

        LocalDateTime heureDepart = vol.getDateHeureDepart().toLocalDateTime();

        LocalDateTime heureFinReservation = heureDepart.minusHours(regleReservation.getNbHeureLimiteAvantVol());

        Timestamp heureFinTimestamp = Timestamp.valueOf(heureFinReservation);

        System.out.println("Heure limite de réservation : " + heureFinTimestamp);
        
    
        return this.getDateHeureReservation().before(heureFinTimestamp);
    }

    public boolean estDansLesTempsAnnulation(Connection conn, Timestamp dateHeureAnnulation) throws Exception
    {
        Vol vol = Vol.getById(conn, this.getIdVol());
        RegleAnnulationReservation regle = RegleAnnulationReservation.getByIdVol(conn, this.getIdVol());

        if (vol == null || regle == null) {
            System.out.println("regle d'annulation inexistante");

            return false; 
        }

        LocalDateTime heureDepart = vol.getDateHeureDepart().toLocalDateTime();

        LocalDateTime heureFinAnnulation = heureDepart.minusHours(regle.getNbHeureLimiteAvantVol());

        Timestamp heureFinTimestamp = Timestamp.valueOf(heureFinAnnulation);

        System.out.println("Heure limite d'annulation de vol : " + heureFinTimestamp);

        
        
    
        return dateHeureAnnulation.before(heureFinTimestamp);
    }

    public static boolean estGuichetFerme(Connection conn, int id_vol, int id_type_siege) {
        boolean estFerme = false;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Requête SQL pour vérifier le nombre de places disponibles
            String sql = "SELECT COALESCE(nb, 0) AS places_restantes " +
                         "FROM Place_dispo_vol " +
                         "WHERE id_vol = ? AND id_type_siege = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_vol);
            stmt.setInt(2, id_type_siege);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int placesRestantes = rs.getInt("places_restantes");
                // Si le nombre de places restantes est 0, le guichet est fermé
                if (placesRestantes == 0) {
                    estFerme = true;
                }
            } else {
                // Si aucune ligne n'existe pour ce vol et type de siège, considérer comme fermé
                estFerme = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return estFerme;
    }


    public static double calculPrixReservation(Connection conn, int id_vol, int id_type_siege) {
        double prixFinal = 0.0;

        try {
            PromotionSiege promo = Vol.getPromotionSiDispo(conn, id_vol, id_type_siege);

            Vol vol = Vol.getById(conn, id_vol);
            double prixBase = vol.getPrixSiege(conn, id_type_siege);

            if (promo != null) {
                prixFinal = prixBase -( prixBase * (promo.getPourcent() / 100.0));
            } else {
                prixFinal = prixBase;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return prixFinal;
    }

    public  int countAdultesByReservation(Connection conn) throws SQLException {
        String query = "SELECT * FROM Detail_reservation WHERE id_reservation = ?";
        int count = 0;
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DetailReservation detail = new DetailReservation();
                    detail.setDateNaissance(rs.getDate("date_naissance"));
                    
                    int age = detail.getAge();
                    if (!PrixVolEnfant.estEnfant(conn, age)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public  int countEnfantByReservation(Connection conn) throws SQLException {
        String query = "SELECT * FROM Detail_reservation WHERE id_reservation = ?";
        int count = 0;
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DetailReservation detail = new DetailReservation();
                    detail.setDateNaissance(rs.getDate("date_naissance"));
                    
                    int age = detail.getAge();
                    if (PrixVolEnfant.estEnfant(conn, age)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
    


    public int effectuerReservation1(Connection conn){
        int id=0;

        try {
            id= insertReturningId(conn);
           return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

  


    public List<DetailReservation> getAllDetail(Connection conn) throws SQLException {
        List<DetailReservation> details = new ArrayList<>();
        
        String query = "SELECT * FROM Detail_reservation WHERE id_reservation = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            
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

    public int deleteAllDetail(Connection conn) throws SQLException {
        String query = "DELETE FROM Detail_reservation WHERE id_reservation = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            return stmt.executeUpdate();
        }
    }

    public  void annulerReservation(Connection conn,Timestamp dateHeureAnnulation) throws Exception{
        if(estDansLesTempsAnnulation(conn, dateHeureAnnulation)){
             
            List<DetailReservation> details = getAllDetail(conn);
            for(DetailReservation detail : details){
                Vol.incrementerNbPlaceDispo(conn, idVol, detail.getIdTypeSiege());
                detail.delete(conn);
            }

            delete(conn);
        }
        else
        {
            System.out.println("il est trop tard pour annuler la reservation");
        }
    }


    public static double getMontant(Connection conn, int idReservation) throws SQLException {
        String query = "SELECT montant_total FROM montant_total_reservation WHERE id_reservation = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idReservation);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("montant_total");
                }
            }
        }
        return 0.0;
    }


    
    
    // public void effectuerReservation(Connection conn) throws Exception{
    //     if(estDansLesTemps(conn)){
    //         System.out.println("DANS LES TEMPS");
    //         if(!estGuichetFerme(conn, idVol, idTypeSiege)){
    //             double montant = calculPrixReservation(conn, idVol, idTypeSiege);
    //             this.setMontant(montant);

    //             this.insert(conn);

                
    //             Vol.decrementerNbPlaceDispo(conn,idVol,idTypeSiege);   
    //         }
    //         else
    //         {
    //             System.out.println("les guichets sont fermés");
    //         }
    //     }
    //     else{
    //         System.out.println("il est trop tard pour reservé");
    //     }
    // }

    // public  void annulerReservation(Connection conn,Timestamp dateHeureAnnulation) throws Exception{
    //     if(estDansLesTempsAnnulation(conn, dateHeureAnnulation)){
             
    //         Vol.incrementerNbPlaceDispo(conn, idVol, idTypeSiege);

    //         delete(conn);
    //     }
    //     else
    //     {
    //         System.out.println("il est trop tard pour annuler la reservation");
    //     }
    // }
}