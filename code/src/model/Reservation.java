package model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import myconnection.MyConnection;

import annotations.*;
import myconnection.MyConnection;
import session.MySession;
import utils.ModelView;

public class Reservation {
    private int id;
    private int idUtilisateur;  // Remplacé Client par idUtilisateur
    private Timestamp dateHeureReservation;
    private int idVol;
    private int idTypeSiege;
    private double montant;

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUtilisateur() {  // Getter pour idUtilisateur
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {  // Setter pour idUtilisateur
        this.idUtilisateur = idUtilisateur;
    }

    public Timestamp getDateHeureReservation() {
        return dateHeureReservation;
    }

    public void setDateHeureReservation(Timestamp dateHeureReservation) {
        this.dateHeureReservation = dateHeureReservation;
    }

    public int getIdVol() {
        return idVol;
    }

    public void setIdVol(int idVol) {
        this.idVol = idVol;
    }

    public int getIdTypeSiege() {
        return idTypeSiege;
    }

    public void setIdTypeSiege(int idTypeSiege) {
        this.idTypeSiege = idTypeSiege;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }


    public void setReservationDateNow() {
       setDateHeureReservation(new Timestamp(System.currentTimeMillis())); 
    }

    public void printReservation() {
        System.out.println("ID Utilisateur: " + idUtilisateur);
        System.out.println("Date et heure de réservation: " + dateHeureReservation);
        System.out.println("ID Vol: " + idVol);
        System.out.println("ID Type de siège: " + idTypeSiege);
        System.out.println("Montant: " + montant);
    }

    public static int getNbReservation(Connection conn, int idVol, int idTypeSiege) throws SQLException {
        String query = "SELECT COUNT(*) FROM Reservation WHERE id_vol = ? AND id_type_siege = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idVol);
            stmt.setInt(2, idTypeSiege);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);  // Retourne le nombre de réservations
                }
            }
        }
        return 0;  // Si aucune réservation n'est trouvée, retourne 0
    }
    

    // Méthode pour insérer une réservation
    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Reservation (id_utilisateur, id_vol, id_type_siege, montant) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, this.idUtilisateur);  // Utilisation de idUtilisateur
            stmt.setInt(2, this.idVol);
            stmt.setInt(3, this.idTypeSiege);
            stmt.setDouble(4, this.montant);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    // Méthode pour récupérer une réservation par ID
    public static Reservation getById(Connection conn, int id) throws SQLException {
        String query = "SELECT * FROM Reservation WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Reservation res = new Reservation();
                    res.setId(rs.getInt("id"));
                    res.setIdUtilisateur(rs.getInt("id_utilisateur"));  // Récupération de idUtilisateur
                    res.setDateHeureReservation(rs.getTimestamp("date_heure_reservation"));
                    res.setIdVol(rs.getInt("id_vol"));
                    res.setIdTypeSiege(rs.getInt("id_type_siege"));
                    res.setMontant(rs.getDouble("montant"));
                    return res;
                }
            }
        }
        return null;
    }


    // Méthode pour récupérer toutes les réservations par ID utilisateur
    public static List<Reservation> getAllById(Connection conn, int idUtilisateur) throws SQLException {
        String query = "SELECT * FROM Reservation WHERE id_utilisateur = ?";
        List<Reservation> reservations = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idUtilisateur);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reservation res = new Reservation();
                    res.setId(rs.getInt("id"));
                    res.setIdUtilisateur(rs.getInt("id_utilisateur"));  // Récupération de idUtilisateur
                    res.setDateHeureReservation(rs.getTimestamp("date_heure_reservation"));
                    res.setIdVol(rs.getInt("id_vol"));
                    res.setIdTypeSiege(rs.getInt("id_type_siege"));
                    res.setMontant(rs.getDouble("montant"));
                    reservations.add(res);
                }
            }
        }
        return reservations;
    }


    // Méthode pour récupérer toutes les réservations
    public static List<Reservation> getAll(Connection conn) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM Reservation";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Reservation res = new Reservation();
                res.setId(rs.getInt("id"));
                res.setIdUtilisateur(rs.getInt("id_utilisateur"));  // Récupération de idUtilisateur
                res.setDateHeureReservation(rs.getTimestamp("date_heure_reservation"));
                res.setIdVol(rs.getInt("id_vol"));
                res.setIdTypeSiege(rs.getInt("id_type_siege"));
                res.setMontant(rs.getDouble("montant"));
                reservations.add(res);
            }
        }
        return reservations;
    }

    // Méthode pour mettre à jour une réservation
    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Reservation SET id_utilisateur = ?, id_vol = ?, id_type_siege = ?, montant = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.idUtilisateur);  // Mise à jour de idUtilisateur
            stmt.setInt(2, this.idVol);
            stmt.setInt(3, this.idTypeSiege);
            stmt.setDouble(4, this.montant);
            stmt.setInt(5, this.id);
            stmt.executeUpdate();
        }
    }

    // Méthode pour supprimer une réservation
    public void delete(Connection conn) throws SQLException {
        String query = "DELETE FROM Reservation WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        }
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
    
    public void effectuerReservation(Connection conn) throws Exception{
        if(estDansLesTemps(conn)){
            System.out.println("DANS LES TEMPS");
            if(!estGuichetFerme(conn, idVol, idTypeSiege)){
                double montant = calculPrixReservation(conn, idVol, idTypeSiege);
                this.setMontant(montant);

                this.insert(conn);

                
                Vol.decrementerNbPlaceDispo(conn,idVol,idTypeSiege);   
            }
            else
            {
                System.out.println("les guichets sont fermés");
            }
        }
        else{
            System.out.println("il est trop tard pour reservé");
        }
    }

    public  void annulerReservation(Connection conn,Timestamp dateHeureAnnulation) throws Exception{
        if(estDansLesTempsAnnulation(conn, dateHeureAnnulation)){
             
            Vol.incrementerNbPlaceDispo(conn, idVol, idTypeSiege);

            delete(conn);
        }
        else
        {
            System.out.println("il est trop tard pour annuler la reservation");
        }
    }

  
}
