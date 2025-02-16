package controller;

import java.util.HashMap;
import java.util.HashMap;
import annotations.Argument;
import annotations.Controller;
import annotations.Post;
import annotations.Url;
import session.MySession;
import utils.ModelView;

@Controller
public class Utilisateur {
     String username;
     String password;
     MySession mySession;

     static HashMap<String, HashMap<String, Integer>> userGrades = new HashMap<>();

    static {
        // Initialisation des notes des utilisateurs
        HashMap<String, Integer> gradesJohn = new HashMap<>();
        gradesJohn.put("Math", 85);
        gradesJohn.put("Physics", 90);
        gradesJohn.put("Chemistry", 78);

        HashMap<String, Integer> gradesJane = new HashMap<>();
        gradesJane.put("Math", 92);
        gradesJane.put("Physics", 88);
        gradesJane.put("Chemistry", 95);

        userGrades.put("john", gradesJohn);
        userGrades.put("jane", gradesJane);
    }

    public Utilisateur() {
        // Constructeur par défaut
    }

    public Utilisateur(String username, String password, MySession session) {
        this.username = username;
        this.password = password;
        this.mySession = session;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public MySession getMySession() {
        return mySession;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMySession(MySession mySession) {
        this.mySession = mySession;
    }

    public static HashMap<String, HashMap<String, Integer>> getUserGrades() {
        return userGrades;
    }

    public static void setUserGrades(HashMap<String, HashMap<String, Integer>> userGrades) {
        Utilisateur.userGrades = userGrades;
    }

    public static HashMap<String, Integer> getGrades(String username) {
        return userGrades.get(username);
    }

    public static Utilisateur getJohn(MySession session) {
        return new Utilisateur("john", "aaa",session);
    }

    public static Utilisateur getJane(MySession session) {
        return new Utilisateur("jane", "password456",session);
    }

    public boolean isValidUtilisateur(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public  HashMap<String, Integer> getGrades() {
        
            return userGrades.get(this.getUsername());
       
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
    public ModelView goToList(@Argument(name="utilisateur") Utilisateur utilisateur ) 
    {
            if ("john".equals(utilisateur.getUsername())) 
            {
                utilisateur = Utilisateur.getJohn(utilisateur.getMySession());
            } else if ("jane".equals(utilisateur.getUsername())) {
                utilisateur = Utilisateur.getJane(utilisateur.getMySession());
            }
    
            
            if (utilisateur != null && utilisateur.isValidUtilisateur(utilisateur.getUsername(), utilisateur.getPassword())) 
            {
                System.out.println("UTILISATEUR EXISTE OH ");
    
                // Stocker l'utilisateur dans la session
                utilisateur.getMySession().add("utilisateur", utilisateur);
    
                // Créer le ModelView pour rediriger vers la page de liste des notes
                HashMap<String, Object> data = new HashMap<>();
                data.put("session", utilisateur.getMySession());
    
                String url = "/utilisateur/listeGrades.jsp";
                ModelView mv = new ModelView(url, data);
                return mv;
            } 
            else 
            {
                // Rediriger vers la page de login avec un message d'erreur
                HashMap<String, Object> data = new HashMap<>();
                data.put("error", "Invalid credentials");
                String url = "/utilisateur/login.jsp";
                return new ModelView(url, data);
            } 
    }

    @Url(url="/utilisateur/logout")
    public ModelView logout(@Argument(name="mySession") MySession mySession)
    {
        mySession.delete("utilisateur");
        HashMap<String, Object> data = new HashMap<>();
        data.put("error", "Invalid credentials");
        String url = "/utilisateur/login.jsp";
        return new ModelView(url, data);
    }

   

    
    @Url(url="/utilisatteur/mainaBe")
    public ModelView mainaBe()
    {
        HashMap<String, Object> data = new HashMap<>();
        String url = "/utilisatteur/listeGrades.jsp";
        return new ModelView(url, data);
    }
    

}

    