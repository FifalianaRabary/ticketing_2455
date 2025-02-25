package controller;

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


    @Url(url="/reservation/formReservation")
    public ModelView goToFormReservation() {
        try (Connection conn = MyConnection.getConnection()) {
            HashMap<String,Object> map = new HashMap<>();
            
            List<Vol> vols = Vol.getAll(conn);
            List<TypeSiege> typeSieges = TypeSiege.getAll(conn);
            
            map.put("vols", vols);
            map.put("typeSieges", typeSieges);

            String url = "/frontOffice/formulaireReservation.jsp";
            ModelView mv = new ModelView(url,map);
            return mv;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
