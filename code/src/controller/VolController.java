package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import annotations.*;
import myconnection.MyConnection;
import session.MySession;
import utils.ModelView;

import model.*;

import java.text.SimpleDateFormat;


@Controller
public class VolController {

    @Url(url="/vol/formVol")
    @Auth(level="admin")
    public ModelView goToFormVol() {

        try (Connection conn = MyConnection.getConnection()) {
            HashMap<String,Object> map = new HashMap<>();
            
            List<Avion> avions = Avion.getAll(conn);
            List<Ville> villes = Ville.getAll(conn);
            
            map.put("villes", villes);
            map.put("avions", avions);

            String url = "/backOffice/formulaireVol.jsp";
            ModelView mv = new ModelView(url,map);
        return mv;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    @Url(url="/vol/modifier")
    @Auth(level="admin")
    public ModelView goToFormUpdateVol(@Argument(name="id") int id) {

        try (Connection conn = MyConnection.getConnection()) {
            HashMap<String,Object> map = new HashMap<>();

            Vol vol = Vol.getById(conn, id);
            
            List<Avion> avions = Avion.getAll(conn);
            List<Ville> villes = Ville.getAll(conn);
            
            map.put("villes", villes);
            map.put("vol",vol);
            map.put("avions", avions);

            String url = "/backOffice/updateVol.jsp";
            ModelView mv = new ModelView(url,map);
        return mv;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

  
    @Url(url="/vol/listVol")
    @Auth(level="admin")
    public ModelView goToListVol() {

        try (Connection conn = MyConnection.getConnection()) {
            HashMap<String,Object> map = new HashMap<>();
            

            List<Vol> vols = Vol.getAll(conn);

            List<Avion> avions = Avion.getAll(conn);
            List<Ville> villes = Ville.getAll(conn);
            
            map.put("villes", villes);
            map.put("avions", avions);
            map.put("vols",vols);


            String url = "/backOffice/listeVol.jsp";
            ModelView mv = new ModelView(url,map);
        return mv;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    @Url(url="/vol/listVolFront")
    public ModelView goToListVolFront() {

        try (Connection conn = MyConnection.getConnection()) {
            HashMap<String,Object> map = new HashMap<>();
            

            List<Vol> vols = Vol.getAll(conn);

            List<Avion> avions = Avion.getAll(conn);
            List<Ville> villes = Ville.getAll(conn);
            
            map.put("villes", villes);
            map.put("avions", avions);
            map.put("vols",vols);


            String url = "/frontOffice/listeVol.jsp";
            ModelView mv = new ModelView(url,map);
        return mv;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    @Url(url="/vol/delete")
    @Auth(level="admin")
    public ModelView deleteVol(@Argument(name="id") int id) {

        try (Connection conn = MyConnection.getConnection()) {
            HashMap<String,Object> map = new HashMap<>();
            
            Vol.deletePlaceDispoByVol(conn,id);
            Vol.deletePrixSiegeByVol(conn,id);
            PromotionSiege.deleteByIdVol(conn,id);
            Vol.delete(conn, id);
           
            List<Vol> vols = Vol.getAll(conn);

            List<Avion> avions = Avion.getAll(conn);
            List<Ville> villes = Ville.getAll(conn);
            
            map.put("villes", villes);
            map.put("avions", avions);
            map.put("vols",vols);


            String url = "/backOffice/listeVol.jsp";
            ModelView mv = new ModelView(url,map);
        return mv;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    @Post()
    @Url(url="/vol/insert")
    @Auth(level="admin")
    public ModelView insertVol(@Argument(name="vol") Vol vol, @Argument(name="prixEconomique") double prixEconomique, @Argument(name="prixBusiness") double prixBusiness) {
    
        vol.printVol();
        System.out.println("prix eco : " + prixEconomique);
        System.out.println("prix business : " + prixBusiness);
    
        Connection conn = null;
        try {
            conn = MyConnection.getConnection();
            conn.setAutoCommit(false); // Désactiver l'auto-commit
    
            if (vol != null && prixEconomique > 0 && prixBusiness > 0) {
                System.out.println("IF");
    
                int idVol = vol.insertReturningId(conn);
    
                // Insertion des prix pour les sièges en économique et business
                int id_type_eco = 1;
                int id_type_business = 2;
                Vol.insertPrixSiege(conn, idVol, id_type_eco, prixEconomique);
                Vol.insertPrixSiege(conn, idVol, id_type_business, prixBusiness);

                Vol.insertPlaceDispo(conn, idVol, vol.getIdAvion());
    
                conn.commit(); // Valider la transaction
    
                HashMap<String, Object> data = new HashMap<>();
                String url = "/backOffice/dashboard.jsp";

                ModelView mv = new ModelView(url,data);
                mv.add("vol",vol);
                mv.setErrorUrl("/backOffice/formVol");
                return mv;
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

    @Post()
    @Url(url="/vol/update")
    @Auth(level="admin")
    public ModelView updateVol(@Argument(name="vol") Vol vol, @Argument(name="prixEconomique") double prixEconomique, @Argument(name="prixBusiness") double prixBusiness) {
    
        vol.printVol();
        System.out.println("prix eco : " + prixEconomique);
        System.out.println("prix business : " + prixBusiness);
    
        Connection conn = null;
        try {
            conn = MyConnection.getConnection();
            conn.setAutoCommit(false); // Désactiver l'auto-commit
    
            if (vol != null && prixEconomique > 0 && prixBusiness > 0) {
                System.out.println("IF");

                vol.update(conn);

                int id_type_eco = 1;
                int id_type_business = 2;
                Vol.updatePrixSiege(conn, vol.getId(), id_type_eco, prixEconomique);
                Vol.updatePrixSiege(conn, vol.getId(), id_type_business, prixBusiness);
    
                conn.commit(); // Valider la transaction

                HashMap<String, Object> data = new HashMap<>();


                List<Vol> vols = Vol.getAll(conn);

                List<Avion> avions = Avion.getAll(conn);
                List<Ville> villes = Ville.getAll(conn);
                
                data.put("villes", villes);
                data.put("avions", avions);
                data.put("vols",vols);
    
                String url = "/backOffice/listeVol.jsp";
                return new ModelView(url, data);
        } else {
                System.out.println("ELSE");
                HashMap<String, Object> data = new HashMap<>();
                data.put("error", "Invalid credentials");
                String url = "/backOffice/listeVol.jsp";
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
            String url = "/backOffice/listeVol.jsp";
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

    // @Post()
    // @Url(url="/vol/filter")
    // public String filtrerVol( 
    // @Argument(name="dateDepart") String dateDepart,
    // @Argument(name="dateArrivee") String dateArrivee,
    // @Argument(name="villeDepart") int villeDepart,
    // @Argument(name="villeArrivee") int villeArrivee,
    // @Argument(name="prixMinEco") double prixMinEco,
    // @Argument(name="prixMaxEco") double prixMaxEco,
    // @Argument(name="prixMinBusiness") double prixMinBusiness,
    // @Argument(name="prixMaxBusiness") double prixMaxBusiness,
    // @Argument(name="avion") int avion
    // ) {
    //     String tout = "";
        
    //     // Affichage des paramètres
    //     tout += "dateDepart: " + dateDepart + "\n";
    //     tout += "dateArrivee: " + dateArrivee + "\n";
    //     tout += "villeDepart: " + villeDepart + "\n";
    //     tout += "villeArrivee: " + villeArrivee + "\n";
    //     tout += "prixMinEco: " + prixMinEco + "\n";
    //     tout += "prixMaxEco: " + prixMaxEco + "\n";
    //     tout += "prixMinBusiness: " + prixMinBusiness + "\n";
    //     tout += "prixMaxBusiness: " + prixMaxBusiness + "\n";
    //     tout += "avion: " + avion + "\n";
        
    //     // Retourner les informations sous forme de chaîne
    //     return tout;
    // }




    @Post()
    @Url(url="/vol/filter")
    @Auth(level="admin")
    public ModelView filtrerVol( 
    @Argument(name="dateDepart") String dateDepart,
    @Argument(name="dateArrivee") String dateArrivee,
    @Argument(name="villeDepart") int villeDepart,
    @Argument(name="villeArrivee") int villeArrivee,
    @Argument(name="prixMinEco") double prixMinEco,
    @Argument(name="prixMaxEco") double prixMaxEco,
    @Argument(name="prixMinBusiness") double prixMinBusiness,
    @Argument(name="prixMaxBusiness") double prixMaxBusiness,
    @Argument(name="avion") int avion
    ) {
        String tout = "";
        
        // Affichage des paramètres
        tout += "dateDepart: " + dateDepart + "\n";
        tout += "dateArrivee: " + dateArrivee + "\n";
        tout += "villeDepart: " + villeDepart + "\n";
        tout += "villeArrivee: " + villeArrivee + "\n";
        tout += "prixMinEco: " + prixMinEco + "\n";
        tout += "prixMaxEco: " + prixMaxEco + "\n";
        tout += "prixMinBusiness: " + prixMinBusiness + "\n";
        tout += "prixMaxBusiness: " + prixMaxBusiness + "\n";
        tout += "avion: " + avion + "\n";
        
        System.out.println("TOUT :"+tout);

           System.out.println("DATE ARRIVEE : "+dateArrivee);
        System.out.println("DATE DEPART : "+dateDepart);
        List<Vol> volsFiltres = new ArrayList<>();

         // Définir un format de date qui correspond à tes chaînes de date
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
         dateFormat.setLenient(false); // Empêche les erreurs silencieuses
                     
            try (Connection conn = MyConnection.getConnection()) {

                Timestamp timestampDateDepart = null;
                Timestamp timestampDateArrivee = null;

        // Convertir les chaînes de caractères en Timestamp
        if(dateDepart !=null){
            timestampDateDepart = new Timestamp(dateFormat.parse(dateDepart).getTime());
        }            
        if(dateArrivee!=null){
             timestampDateArrivee = new Timestamp(dateFormat.parse(dateArrivee).getTime());

        }

    

        volsFiltres = Vol.rechercherVols(conn, timestampDateDepart, timestampDateArrivee, villeDepart, villeArrivee, prixMinEco, prixMaxEco, prixMinBusiness, prixMaxBusiness, avion);            
                HashMap<String, Object> data = new HashMap<>();
                List<Avion> avions = Avion.getAll(conn);
                List<Ville> villes = Ville.getAll(conn);
                
                data.put("villes", villes);
                data.put("avions", avions);
                data.put("vols", volsFiltres);
                
                String url = "/backOffice/listeVol.jsp";
                return new ModelView(url, data);
            } catch (Exception e) {
                e.printStackTrace();
                
                HashMap<String, Object> data = new HashMap<>();
                data.put("error", "Une erreur s'est produite.");
                
                String url = "/backOffice/dashboard.jsp";
                return new ModelView(url, data);
            }

    }



    @Post()
    @Url(url="/vol/filterFront")
    public ModelView filtrerVolFront( 
    @Argument(name="dateDepart") String dateDepart,
    @Argument(name="dateArrivee") String dateArrivee,
    @Argument(name="villeDepart") int villeDepart,
    @Argument(name="villeArrivee") int villeArrivee,
    @Argument(name="prixMinEco") double prixMinEco,
    @Argument(name="prixMaxEco") double prixMaxEco,
    @Argument(name="prixMinBusiness") double prixMinBusiness,
    @Argument(name="prixMaxBusiness") double prixMaxBusiness,
    @Argument(name="avion") int avion
    ) {
        String tout = "";
        
        // Affichage des paramètres
        tout += "dateDepart: " + dateDepart + "\n";
        tout += "dateArrivee: " + dateArrivee + "\n";
        tout += "villeDepart: " + villeDepart + "\n";
        tout += "villeArrivee: " + villeArrivee + "\n";
        tout += "prixMinEco: " + prixMinEco + "\n";
        tout += "prixMaxEco: " + prixMaxEco + "\n";
        tout += "prixMinBusiness: " + prixMinBusiness + "\n";
        tout += "prixMaxBusiness: " + prixMaxBusiness + "\n";
        tout += "avion: " + avion + "\n";
        
        System.out.println("TOUT :"+tout);

           System.out.println("DATE ARRIVEE : "+dateArrivee);
        System.out.println("DATE DEPART : "+dateDepart);
        List<Vol> volsFiltres = new ArrayList<>();

         // Définir un format de date qui correspond à tes chaînes de date
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
         dateFormat.setLenient(false); // Empêche les erreurs silencieuses
                     
            try (Connection conn = MyConnection.getConnection()) {

                Timestamp timestampDateDepart = null;
                Timestamp timestampDateArrivee = null;

        // Convertir les chaînes de caractères en Timestamp
        if(dateDepart !=null){
            timestampDateDepart = new Timestamp(dateFormat.parse(dateDepart).getTime());
        }            
        if(dateArrivee!=null){
             timestampDateArrivee = new Timestamp(dateFormat.parse(dateArrivee).getTime());

        }

    

        volsFiltres = Vol.rechercherVols(conn, timestampDateDepart, timestampDateArrivee, villeDepart, villeArrivee, prixMinEco, prixMaxEco, prixMinBusiness, prixMaxBusiness, avion);            
                HashMap<String, Object> data = new HashMap<>();
                List<Avion> avions = Avion.getAll(conn);
                List<Ville> villes = Ville.getAll(conn);
                
                data.put("villes", villes);
                data.put("avions", avions);
                data.put("vols", volsFiltres);
                
                String url = "/frontOffice/listeVol.jsp";
                return new ModelView(url, data);
            } catch (Exception e) {
                e.printStackTrace();
                
                HashMap<String, Object> data = new HashMap<>();
                data.put("error", "Une erreur s'est produite.");
                
                String url = "/frontOffice/dashboard.jsp";
                return new ModelView(url, data);
            }

    }

   

  
    
}
