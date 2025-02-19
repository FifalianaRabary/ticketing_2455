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
public class PromotionSiege {
    private int id;
    private double pourcent;
    private int idVol;
    private int idTypeSiege;
    private int nbSiege;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getPourcent() { return pourcent; }
    public void setPourcent(double pourcent) { this.pourcent = pourcent; }
    public int getIdVol() { return idVol; }
    public void setIdVol(int idVol) { this.idVol = idVol; }
    public int getIdTypeSiege() { return idTypeSiege; }
    public void setIdTypeSiege(int idTypeSiege) { this.idTypeSiege = idTypeSiege; }
    public int getNbSiege() { return nbSiege; }
    public void setNbSiege(int nbSiege) { this.nbSiege = nbSiege; }

    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Promotion_siege (pourcent, id_vol, id_type_siege, nb_siege) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, this.pourcent);
            stmt.setInt(2, this.idVol);
            stmt.setInt(3, this.idTypeSiege);
            stmt.setInt(4, this.nbSiege);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) this.id = rs.getInt(1);
            }
        }
    }

    public static List<PromotionSiege> getAll(Connection conn) throws SQLException {
        List<PromotionSiege> promotions = new ArrayList<>();
        String query = "SELECT * FROM Promotion_siege";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                PromotionSiege promo = new PromotionSiege();
                promo.setId(rs.getInt("id"));
                promo.setPourcent(rs.getDouble("pourcent"));
                promo.setIdVol(rs.getInt("id_vol"));
                promo.setIdTypeSiege(rs.getInt("id_type_siege"));
                promo.setNbSiege(rs.getInt("nb_siege"));
                promotions.add(promo);
            }
        }
        return promotions;
    }

    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Promotion_siege SET pourcent = ?, id_vol = ?, id_type_siege = ?, nb_siege = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, this.pourcent);
            stmt.setInt(2, this.idVol);
            stmt.setInt(3, this.idTypeSiege);
            stmt.setInt(4, this.nbSiege);
            stmt.setInt(5, this.id);
            stmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String query = "DELETE FROM Promotion_siege WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        }
    }


    @Url(url="/promotion/formPromotion")
    public ModelView goToFormPromotion() {

        try (Connection conn = MyConnection.getConnection()) {
            HashMap<String,Object> map = new HashMap<>();
            
            List<Vol> vols = Vol.getAll(conn);
            List<TypeSiege> typeSieges = TypeSiege.getAll(conn);
            
            map.put("vols", vols);
            map.put("typeSieges", typeSieges);

            String url = "/backOffice/formulairePromotion.jsp";
            ModelView mv = new ModelView(url,map);
        return mv;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }


    @Post()
    @Url(url="/promotion/insert")
    public ModelView insertVol(@Argument(name="promotionSiege") PromotionSiege promotionSiege) {
    

    
        Connection conn = null;
        try {
            conn = MyConnection.getConnection();
            conn.setAutoCommit(false); // Désactiver l'auto-commit
    
            if (promotionSiege != null ) {
                System.out.println("IF");
    
    
                promotionSiege.insert(conn);
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
