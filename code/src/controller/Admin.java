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
public class Admin {

    private int id;
    private String username;
    private String mdp;
    private String level; // Ajout de l'attribut level

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    // Méthode pour créer un Admin (INSERT)
    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Admin (username, mdp, level) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, this.username);
            stmt.setString(2, this.mdp);
            stmt.setString(3, this.level);
            stmt.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    // Méthode pour récupérer un Admin par ID (SELECT)
    public static Admin getById(Connection conn, int id) throws SQLException {
        String query = "SELECT * FROM Admin WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Admin user = new Admin();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setMdp(rs.getString("mdp"));
                    user.setLevel(rs.getString("level"));
                    return user;
                }
            }
        }
        return null;
    }

    public static String getLevelByUsername(Connection conn, String username) throws SQLException {
        String query = "SELECT level FROM Admin WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("level"); // Retourne directement le niveau
                }
            }
        }
        return null; // Retourne null si l'utilisateur n'existe pas ou n'a pas de niveau
    }
    
    // Méthode pour récupérer tous les Admins (SELECT ALL)
    public static List<Admin> getAll(Connection conn) throws SQLException {
        List<Admin> users = new ArrayList<>();
        String query = "SELECT * FROM Admin";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Admin user = new Admin();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setMdp(rs.getString("mdp"));
                user.setLevel(rs.getString("level"));
                users.add(user);
            }
        }
        return users;
    }

    // Méthode pour mettre à jour un Admin (UPDATE)
    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Admin SET username = ?, mdp = ?, level = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, this.username);
            stmt.setString(2, this.mdp);
            stmt.setString(3, this.level);
            stmt.setInt(4, this.id);
            stmt.executeUpdate();
        }
    }

    // Méthode pour supprimer un Admin (DELETE)
    public void delete(Connection conn) throws SQLException {
        String query = "DELETE FROM Admin WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        }
    }

    // LOGIN
    public static boolean checkLogin(Connection connection, String username, String mdp) {
        System.out.println("FONCTION CHECK LOGIN");
        String sql = "SELECT id, username, mdp, level FROM Admin WHERE username = ? AND mdp = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, mdp);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    @Url(url="/admin/login")
    public ModelView login() {
        HashMap<String,Object> map = new HashMap<>();
        String url = "/admin/login.jsp";
        ModelView mv = new ModelView(url,map);
        return mv;
    }

    @Post()
    @Url(url="/admin/checkLogin")
    public ModelView goToDashBoard(@Argument(name="admin") Admin admin ) 
    {
        System.out.println(admin.getUsername()); 
        System.out.println(admin.getMdp()); 
        try(Connection conn = MyConnection.getConnection()) {
            if(checkLogin(conn, admin.getUsername(), admin.getMdp())){
                System.out.println("IF");

                String level = getLevelByUsername(conn, admin.getUsername());
    
                HashMap<String, Object> data = new HashMap<>();

                data.put("role",level);
    
                String url = "/backOffice/dashboard.jsp";
                ModelView mv = new ModelView(url, data);
                return mv;
              }
              else 
              {
                System.out.println("ELSE");
                HashMap<String, Object> data = new HashMap<>();
                data.put("error", "Invalid credentials");
                String url = "/admin/login.jsp";
                return new ModelView(url, data);
              }
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, Object> data = new HashMap<>();
            data.put("error", "Invalid credentials");
            String url = "/admin/login.jsp";
            return new ModelView(url, data);
        }
    }
}
