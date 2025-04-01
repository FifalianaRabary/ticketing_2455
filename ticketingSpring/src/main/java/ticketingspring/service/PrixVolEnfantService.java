package ticketingspring.service;

import ticketingspring.model.PrixVolEnfant;
import ticketingspring.model.TypeSiege;
import ticketingspring.model.Vol;
import ticketingspring.myconnection.MyConnection;

import java.sql.*;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PrixVolEnfantService {

    // Ajouter un prix de vol enfant
    public void save(PrixVolEnfant prixVolEnfant) throws Exception {
        try (Connection conn = MyConnection.getConnection()) {
            prixVolEnfant.insert(conn);
        }
    }

    // Récupérer un prix de vol enfant par son ID
    public PrixVolEnfant getById(int id) throws Exception {
        try (Connection conn = MyConnection.getConnection()) {
            return PrixVolEnfant.getById(conn, id);
        }
    }

    // Récupérer tous les prix de vol enfant
    public List<PrixVolEnfant> getAll() throws Exception {
        try (Connection conn = MyConnection.getConnection()) {
            return PrixVolEnfant.getAll(conn);
        }
    }

    // Supprimer un prix de vol enfant par son ID
    public void deleteById(int id) throws Exception {
        String query = "DELETE FROM Prix_vol_enfant WHERE id = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


    public List<TypeSiege> getAllTypeSiege() throws Exception {
        try (Connection conn = MyConnection.getConnection()) {
            return TypeSiege.getAll(conn);  // Utilise la méthode statique dans TypeSiege pour récupérer tous les types de sièges
        }
    }

    // Méthode pour récupérer tous les vols
    public List<Vol> getAllVols() throws Exception {
        try (Connection conn = MyConnection.getConnection()) {
            // Crée une classe Vol similaire à TypeSiege ou une méthode qui les récupère
            return Vol.getAll(conn);  // Supposons qu'une classe Vol existe pour récupérer les vols
        }
    }
}
