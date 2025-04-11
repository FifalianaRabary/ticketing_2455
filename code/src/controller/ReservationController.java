package controller;

import java.lang.reflect.Type;
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


    @Url(url="/reservation/formReservation1")
    @Auth(level="user")
    public ModelView goToFormReservation1(@Argument(name="idVol") int idVol) {
        try (Connection conn = MyConnection.getConnection()) {

            HashMap<String,Object> map = new HashMap<>();
            

            map.put("idVol", idVol);

            String url = "/frontOffice/formulaireReservation1.jsp";
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
            

            List<Reservation> reservations = Reservation.getByUserId(conn, id);
            
            
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
    @Url(url="/reservation/insert1")
    @Auth(level="user")
    public ModelView insertReservation1(@Argument(name="reservation") Reservation reservation) {
        reservation.setReservationDateNow();
    
        Connection conn = null;
        try {
            conn = MyConnection.getConnection();
            conn.setAutoCommit(false); 
            
            if (reservation != null) 
            {
                System.out.println("RESERVATION NON NULL");
                HashMap<String, Object> data = new HashMap<>();
                String url ;

                // check s'il y a encore assez de place pour ce vol pour le nombre de passagers entrée
                // si oui faire l'insertion, si non throws exception 
                if(reservation.estPossible(conn)){
                    if(reservation.estDansLesTemps(conn)){
                        int idReservation = reservation.effectuerReservation1(conn);

                        List<TypeSiege> typeSieges = TypeSiege.getAll(conn);
                        
                        data.put("idReservation", idReservation);
                        data.put("typeSieges",typeSieges);
                        url = "/frontOffice/formulaireReservation2.jsp";
                        return new ModelView(url, data);
                    }
                    else{
                        data.put("error", "Vous n'êtes plus dans les temps pour effectuer une reservation sur ce vol");
                        url = "/frontOffice/formulaireReservation1.jsp";
                        return new ModelView(url, data);
                    }
                }
                else{
                    data.put("error", "Il n'y a plus assez de place sur ce vol pour le nombre de passager entré ");
                    url = "/frontOffice/formulaireReservation1.jsp";
                    return new ModelView(url, data);
                }
                
            } else {
                HashMap<String, Object> data = new HashMap<>();
                data.put("error", "Réservation invalide.");
                String url = "/frontOffice/formulaireReservation1.jsp";
                return new ModelView(url, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            HashMap<String, Object> data = new HashMap<>();
            data.put("error", "Une erreur s'est produite.");
            String url = "/frontOffice/formulaireReservation1.jsp";
            return new ModelView(url, data);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); 
                    conn.close(); 
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }


    @Post()
    @Url(url="/reservation/insertDetail")
    @Auth(level="user")
    public ModelView insertDetail(@Argument(name="detailReservation") DetailReservation detail) {

    
        Connection conn = null;
        try {
            conn = MyConnection.getConnection();
            conn.setAutoCommit(false); 
            
            if (detail != null) {
                System.out.println("ATO AM IF 1");
                System.out.println("RESERVATION NON NULL");

                int idReservation = detail.getIdReservation();
                        
                HashMap<String, Object> data = new HashMap<>();

                List<TypeSiege> typeSieges = TypeSiege.getAll(conn);
                data.put("idReservation", idReservation);
                String url = "/frontOffice/formulaireReservation2.jsp";

                if(DetailReservation.estSiegeDisponible(conn, detail.getIdReservation(), detail.getIdTypeSiege())){
                    System.out.println("ATO AM IF 2");
                    if(detail.estEnfant(conn)){
                        System.out.println("ATO AM IF 3");
                        if(detail.estCompleteEnfant(conn)){
                            System.out.println("ATO AM IF 4");
                            data.put("typeSieges", typeSieges);

                            data.put("error", "Tous les details pour les passagers enfant ont déjà été insérés");
                            return new ModelView(url, data);
                        }
                        else{
                            if(detail.estCompleteReservationApres(conn)){
                                System.out.println("ATO AM IF 6");
        
                                Reservation reservation = Reservation.getById(conn, idReservation);
                                reservation.setComplete(true);
                                reservation.update(conn);
        
                                detail.effectuerReservation(conn);
        
                                double montant_total = Reservation.getMontant(conn, idReservation);
                                data.put("succes", "reservation effectuée avec succes");
                                data.put("total", montant_total);
                                url = "/frontOffice/totalReservation.jsp";
                                return new ModelView(url, data);
                            }
                            else{
                                data.put("typeSieges", typeSieges);
                                detail.effectuerReservation(conn);
                                 url = "/frontOffice/formulaireReservation2.jsp";
                                return new ModelView(url, data);

                            }
                        }
                    }
                    else
                    {
                        System.out.println("ATO AM ELSE 3");

                        if(detail.estCompleteAdulte(conn)){
                            System.out.println("ATO AM IF 5");
                            data.put("typeSieges", typeSieges);

                            data.put("error", "Tous les details pour les passagers adultes ont déjà été insérés");
                            return new ModelView(url, data);
                        }
                        else{
                            if(detail.estCompleteReservationApres(conn)){
                                System.out.println("ATO AM IF 6");
        
                                Reservation reservation = Reservation.getById(conn, idReservation);
                                reservation.setComplete(true);
                                reservation.update(conn);
        
                                detail.effectuerReservation(conn);
        
                                double montant_total = Reservation.getMontant(conn, idReservation);
                                data.put("succes", "reservation effectuée avec succes");
                                data.put("total", montant_total);
                                url = "/frontOffice/totalReservation.jsp";
                                return new ModelView(url, data);
                            }
                            else{
                                data.put("typeSieges", typeSieges);
                                detail.effectuerReservation(conn);
                                 url = "/frontOffice/formulaireReservation2.jsp";
                                return new ModelView(url, data);

                            }
                        }                        
                        
                    }
                

                    //feno ve zay lay isanah adulte sy isanah enfant
                    // if(detail.estCompleteReservationApres(conn)){
                    //     System.out.println("ATO AM IF 6");

                    //     Reservation reservation = Reservation.getById(conn, idReservation);
                    //     reservation.setComplete(true);
                    //     reservation.update(conn);

                    //     detail.effectuerReservation(conn);

                    //     double montant_total = Reservation.getMontantTotal(conn, idReservation);
                    //     data.put("succes", "reservation effectuée avec succes");
                    //     data.put("total", montant_total);
                    //     url = "/frontOffice/totalReservation.jsp";
                    //     return new ModelView(url, data);
                    // }
                    // return new ModelView(url, data);
                }
                else
                {

                    System.out.println("ATO AM ELSE 2");
                    data.put("error", "Il n'y a plus assez de place sur ce vol pour le type de siege demandé ");
                    return new ModelView(url, data);
                }
               
            } else {
                System.out.println("ATO AM ELSE 1");
                HashMap<String, Object> data = new HashMap<>();
                data.put("error", "Réservation invalide.");
                String url = "/frontOffice/formulaireReservation2.jsp";
                return new ModelView(url, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            HashMap<String, Object> data = new HashMap<>();
            data.put("error", "Une erreur s'est produite.");
            String url = "/frontOffice/formulaireReservation2.jsp";
            return new ModelView(url, data);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); 
                    conn.close(); 
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }
    

    @Url(url="/reservation/annulation")
    public ModelView annulerReservation(@Argument(name="id") int id, @Argument(name="id_user") int id_user) {

        try (Connection conn = MyConnection.getConnection()) {
            Reservation reservation = Reservation.getById(conn, id);
            Timestamp dateAnnulation = new Timestamp(System.currentTimeMillis());

            System.out.println("AVANT ANNULATION");
            reservation.annulerReservation(conn,dateAnnulation);

            System.out.println("APRES ANNULATION");

            HashMap<String,Object> map = new HashMap<>();

            List<Reservation> reservations = Reservation.getByUserId(conn, id_user);
            map.put("reservations",reservations);

            String url = "/frontOffice/listeReservation.jsp";
            ModelView mv = new ModelView(url,map);
            return mv;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    
    @Url(url="/reservation/voirDetail")
    @Auth(level="user")
    public ModelView annulerReservation(@Argument(name="id") int id) {

        try (Connection conn = MyConnection.getConnection()) {
            Reservation reservation = Reservation.getById(conn, id);
            

            HashMap<String,Object> map = new HashMap<>();

            List<DetailReservation> details = reservation.getAllDetail(conn);

            map.put("details",details);

            String url = "/frontOffice/listeDetailReservation.jsp";
            ModelView mv = new ModelView(url,map);
            return mv;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
    

}
