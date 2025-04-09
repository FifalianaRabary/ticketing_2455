package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import myconnection.MyConnection;

import model.*;

import annotations.*;
import myconnection.MyConnection;
import session.MySession;
import utils.ModelView;

@Controller
public class ReservationController {


    @Url(url="/reservation/formReservation")
    @Auth(level="user")
    public ModelView goToFormReservation() {
        try (Connection conn = MyConnection.getConnection()) {
            HashMap<String,Object> map = new HashMap<>();
            
            List<Vol> vols = Vol.getAll(conn);
            List<TypeSiege> typeSieges = TypeSiege.getAll(conn);
            
            map.put("vols", vols);
            map.put("typeSieges", typeSieges);

            String url = "/frontOffice/formulaireReservation.jsp";
            ModelView mv = new ModelView(url,map);
            return mv;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Url(url="/reservation/listReservation")
    public ModelView goToListVolFront(@Argument(name="id") int id) {

        try (Connection conn = MyConnection.getConnection()) {
            HashMap<String,Object> map = new HashMap<>();
            

            List<Reservation> reservations = Reservation.getAllById(conn, id);
            
            
            map.put("reservations",reservations);


            String url = "/frontOffice/listeReservation.jsp";
            ModelView mv = new ModelView(url,map);
        return mv;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    @Url(url="/reservation/annulation")
    public ModelView annulerReservation(@Argument(name="id") int id, @Argument(name="id_user") int id_user) {

        try (Connection conn = MyConnection.getConnection()) {
            Reservation reservation = Reservation.getById(conn, id);
            Timestamp dateAnnulation = new Timestamp(System.currentTimeMillis());

            reservation.annulerReservation(conn,dateAnnulation);

            HashMap<String,Object> map = new HashMap<>();

            List<Reservation> reservations = Reservation.getAllById(conn, id_user);
            map.put("reservations",reservations);

            String url = "/frontOffice/listeReservation.jsp";
            ModelView mv = new ModelView(url,map);
        return mv;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }


    @Post()
    @Url(url="/reservation/insert")
    @Auth(level="user")
    public ModelView insertReservation(@Argument(name="reservation") Reservation reservation) {
        reservation.setReservationDateNow();
        reservation.printReservation();
    
        // Connexion à la base de données
        Connection conn = null;
        try {
            conn = MyConnection.getConnection();
            conn.setAutoCommit(false); // Désactiver l'auto-commit
            
            if (reservation != null) {
                System.out.println("RESERVATION NON NULL");
                reservation.effectuerReservation(conn);
    
                // Si la réservation est effectuée, retourner la vue
                HashMap<String, Object> data = new HashMap<>();
                String url = "/frontOffice/dashboard.jsp";
                return new ModelView(url, data);
            } else {
                HashMap<String, Object> data = new HashMap<>();
                data.put("error", "Réservation invalide.");
                String url = "/frontOffice/dashboard.jsp";
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
            String url = "/frontOffice/dashboard.jsp";
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
