package controller;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import annotations.*;
import myconnection.MyConnection;
import session.MySession;
import utils.ModelView;

import model.*;

@Controller
public class RegleReservationController {
         
    @Url(url="/regleReservation/formRegleReservation")
    @Auth(level="admin")
    public ModelView goToFormAnnulation() {

        try (Connection conn = MyConnection.getConnection()) {
            HashMap<String,Object> map = new HashMap<>();
            
            List<Vol> vols = Vol.getAll(conn);
            
            map.put("vols", vols);

            String url = "/backOffice/formulaireRegleReservation.jsp";
            ModelView mv = new ModelView(url,map);
        return mv;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    @Post()
    @Url(url="/regleReservation/insert")
    @Auth(level="admin")
    public ModelView insertRegleReservation(@Argument(name="regleReservation") RegleReservation regleReservation) {
    

    
        Connection conn = null;
        try {
            conn = MyConnection.getConnection();
            conn.setAutoCommit(false); // Désactiver l'auto-commit
    
            if (regleReservation != null ) {
                System.out.println("IF");
    
    
                regleReservation.insert(conn);
                // Insertion des prix pour les sièges en économique et business
             
    
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
