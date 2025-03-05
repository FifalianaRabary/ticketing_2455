package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Utilisateur;

import annotations.*;
import myconnection.MyConnection;
import session.MySession;
import utils.ModelView;

@Controller
public class UtilisateurController {


    @Url(url="/user/login")
    public ModelView login() {
        HashMap<String,Object> map = new HashMap<>();
        String url = "/user/login.jsp";
        ModelView mv = new ModelView(url,map);
        return mv;
    }

    @Url(url="/user/upload")
    public ModelView upload() {
        HashMap<String,Object> map = new HashMap<>();
        String url = "/frontOffice/upload.jsp";
        ModelView mv = new ModelView(url,map);
        return mv;
    }

    @Url(url="/backOffice/dashboard")
    public ModelView dashboardBack() {
        HashMap<String,Object> map = new HashMap<>();
        String url = "/backOffice/dashboard.jsp";
        ModelView mv = new ModelView(url,map);
        return mv;
    }

    @Url(url="/frontOffice/dashboard")
    public ModelView dashboardFront() {
        HashMap<String,Object> map = new HashMap<>();
        String url = "/frontOffice/dashboard.jsp";
        ModelView mv = new ModelView(url,map);
        return mv;
    }

    @Url(url="/user/uploading")
    @Post()
    public String uploading() {
        return "upload terminé";
    }

    @Url(url="/user/logout")
    public ModelView logout(@Argument(name="mySession") MySession mySession)
    {
        mySession.delete("user");
        mySession.delete("role");
        HashMap<String, Object> data = new HashMap<>();
        data.put("error", "Invalid credentials");
        String url = "/user/login.jsp";
        return new ModelView(url, data);
    }

    
    @Url(url="/user/testAuth")
    @Auth(level="admin")
    public String testAuth()
    {
        return "Authed";
    }

    @Url(url="/user/jsonRes")
    @RestApi()
    @Get()
    public String jsonRes(@Argument(name="id_user") int id_user){
        try(Connection conn = MyConnection.getConnection()){
            Utilisateur user = Utilisateur.getById(conn, id_user);
            return "L'utilisateur "+user.getUsername()+" est connecté";
        }
        catch(Exception e){
            e.printStackTrace(); // Pour le débogage
            return "Aucune session utilisateur détéctée";
        }
       
    }

    @Post()
    @Url(url="/user/checkLogin")
    public ModelView goToDashBoard(@Argument(name="user") Utilisateur user , @Argument(name="session") MySession session) 
    {
        System.out.println(user.getUsername()); 
        System.out.println(user.getMdp()); 
        try(Connection conn = MyConnection.getConnection()) {
            if(Utilisateur.checkLogin(conn, user.getUsername(), user.getMdp())){
                System.out.println("IF");

                // String level = Utilisateur.getLevelByUsername(conn, user.getUsername());
                Utilisateur utilisateur = Utilisateur.getByUsername(conn, user.getUsername());


                session.add("user",utilisateur);
                session.add("role",utilisateur.getLevel());
    
                HashMap<String, Object> data = new HashMap<>();

                // data.put("role",level);
                String url = null;
                
                if(utilisateur.getLevel().equals("admin")){
                    url = "/backOffice/dashboard.jsp";
                }
                else{
                    url = "/frontOffice/dashboard.jsp";
                }
                ModelView mv = new ModelView(url, data);
                mv.add("session",session);
                return mv;
              }
              else 
              {
                System.out.println("ELSE");
                HashMap<String, Object> data = new HashMap<>();
                data.put("error", "Invalid credentials");
                String url = "/user/login.jsp";
                return new ModelView(url, data);
              }
        } catch (Exception e) {
            System.out.println("INVALID CREDENTIALS");
            e.printStackTrace();
            HashMap<String, Object> data = new HashMap<>();
            data.put("error", "Invalid credentials");
            String url = "/user/login.jsp";
            return new ModelView(url, data);
        }
    }
}
