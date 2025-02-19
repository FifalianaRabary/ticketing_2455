package controller;

import java.sql.*;
import java.util.HashMap;

import myconnection.MyConnection;

import annotations.*;
import myconnection.MyConnection;
import session.MySession;
import utils.ModelView;

@Controller
public class Client {
    private int id;
    private String username;
    private String mdp;
    private String level;

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

    // Méthode pour insérer un client
    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Client (username, mdp, level) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, this.username);
            stmt.setString(2, this.mdp);
            stmt.setString(3, this.level);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    // Méthode pour récupérer un client par ID
    public static Client getById(Connection conn, int id) throws SQLException {
        String query = "SELECT * FROM Client WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client client = new Client();
                    client.setId(rs.getInt("id"));
                    client.setUsername(rs.getString("username"));
                    client.setMdp(rs.getString("mdp"));
                    client.setLevel(rs.getString("level"));
                    return client;
                }
            }
        }
        return null;
    }

    public static String getLevelByUsername(Connection conn, String username) throws SQLException {
        String query = "SELECT level FROM Client WHERE username = ?";
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


      // LOGIN
    public static boolean checkLogin(Connection connection, String username, String mdp) {
        System.out.println("FONCTION CHECK LOGIN");
        String sql = "SELECT id, username, mdp, level FROM Client WHERE username = ? AND mdp = ?";
        
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

    @Url(url="/client/login")
    public ModelView login() {
        HashMap<String,Object> map = new HashMap<>();
        String url = "/client/login.jsp";
        ModelView mv = new ModelView(url,map);
        return mv;
    }

    @Post()
    @Url(url="/client/checkLogin")
    public ModelView goToDashBoard(@Argument(name="client") Client client ) 
    {
        System.out.println(client.getUsername()); 
        System.out.println(client.getMdp()); 
        try(Connection conn = MyConnection.getConnection()) {
            if(checkLogin(conn, client.getUsername(), client.getMdp())){
                System.out.println("IF");

                String level = getLevelByUsername(conn, client.getUsername());
    
                HashMap<String, Object> data = new HashMap<>();

                data.put("role",level);
    
                String url = "/frontOffice/dashboard.jsp";
                ModelView mv = new ModelView(url, data);
                return mv;
              }
              else 
              {
                System.out.println("ELSE");
                HashMap<String, Object> data = new HashMap<>();
                data.put("error", "Invalid credentials");
                String url = "/client/login.jsp";
                return new ModelView(url, data);
              }
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, Object> data = new HashMap<>();
            data.put("error", "Invalid credentials");
            String url = "/client/login.jsp";
            return new ModelView(url, data);
        }
    }
}
