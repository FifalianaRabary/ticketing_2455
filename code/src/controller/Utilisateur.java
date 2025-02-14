package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.HashMap;
import annotations.Argument;
import annotations.Controller;
import annotations.Post;
import annotations.Url;
import myconnection.MyConnection;
import session.MySession;
import utils.ModelView;

import java.sql.Statement;


@Controller
public class Utilisateur {
  
    int id;
    String mail;
    String mdp;
    MySession mySession;

    public MySession getMySession() {
        return mySession;
    }

    public void setMySession(MySession mySession) {
        this.mySession = mySession;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    // Méthode pour créer un utilisateur (INSERT)
    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Utilisateur (mail, mdp) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, this.mail);
            stmt.setString(2, this.mdp);
            stmt.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    // Méthode pour récupérer un utilisateur par ID (SELECT)
    public static Utilisateur getById(Connection conn, int id) throws SQLException {
        String query = "SELECT * FROM Utilisateur WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Utilisateur user = new Utilisateur();
                    user.setId(rs.getInt("id"));
                    user.setMail(rs.getString("mail"));
                    user.setMdp(rs.getString("mdp"));
                    return user;
                }
            }
        }
        return null;
    }

    // Méthode pour récupérer tous les utilisateurs (SELECT ALL)
    public static List<Utilisateur> getAll(Connection conn) throws SQLException {
        List<Utilisateur> users = new ArrayList<>();
        String query = "SELECT * FROM Utilisateur";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Utilisateur user = new Utilisateur();
                user.setId(rs.getInt("id"));
                user.setMail(rs.getString("mail"));
                user.setMdp(rs.getString("mdp"));
                users.add(user);
            }
        }
        return users;
    }

    // Méthode pour mettre à jour un utilisateur (UPDATE)
    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Utilisateur SET mail = ?, mdp = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, this.mail);
            stmt.setString(2, this.mdp);
            stmt.setInt(3, this.id);
            stmt.executeUpdate();
        }
    }

    // Méthode pour supprimer un utilisateur (DELETE)
    public void delete(Connection conn) throws SQLException {
        String query = "DELETE FROM Utilisateur WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        }
    }






    // METHODES
    // LOGIN
    public static boolean checkLogin(Connection connection, String mail, String mdp) {
        String sql = "SELECT id, mail, mdp FROM Utilisateur WHERE mail = ? AND mdp = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Remplacer les paramètres de la requête
            stmt.setString(1, mail);
            stmt.setString(2, mdp);
            
            // Exécuter la requête
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Si un utilisateur est trouvé avec le bon mail et mot de passe
                    return true;  // Connexion réussie
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // En cas d'erreur dans la requête ou la connexion
        }
        
        // Si aucun utilisateur correspondant n'est trouvé
        return false;  // Connexion échouée
    }


    @Url(url="/utilisateur/login")
    public ModelView login() {
        HashMap<String,Object> map = new HashMap<>();
        String url = "/utilisateur/login.jsp";
        ModelView mv = new ModelView(url,map);
        return mv;
    }

    @Post()
    @Url(url="/utilisateur/checkLogin")
    public ModelView goToDashBoard(@Argument(name="utilisateur") Utilisateur utilisateur) 
    {
        try(Connection conn = MyConnection.getConnection()){
            if(checkLogin(conn, utilisateur.getMail(), utilisateur.getMdp())){
                utilisateur.getMySession().add("utilisateur", utilisateur);
    
                HashMap<String, Object> data = new HashMap<>();
                // data.put("session", utilisateur.getMySession());
    
                String url = "/frontOffice/dashboard.jsp";
                ModelView mv = new ModelView(url, data);
                return mv;
              }
             
            // Rediriger vers la page de login avec un message d'erreur
            HashMap<String, Object> data = new HashMap<>();
            data.put("error", "Invalid credentials");
            String url = "/utilisateur/login.jsp";
            return new ModelView(url, data);
              
        }
        catch(Exception e){
            e.printStackTrace();
            // En cas d'erreur, rediriger vers une page d'erreur ou de login
            HashMap<String, Object> data = new HashMap<>();
            data.put("error", "Une erreur est survenue.");
            return new ModelView("/utilisateur/login.jsp", data);
        }
          
    }

    
   

    
    @Url(url="/utilisateur/mainaBe")
    public ModelView getListeGrades()
    {
        HashMap<String, Object> data = new HashMap<>();
        String url = "/utilisateur/listeGrades.jsp";
        return new ModelView(url, data);
    }
    

}

    