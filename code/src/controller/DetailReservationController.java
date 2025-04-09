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
public class DetailReservationController {


    @Url(url="/detail/delete")
    @Auth(level="user")
    public ModelView deleteDetail(@Argument(name="id") int id, @Argument(name="idUser") int idUser)  {
        try (Connection conn = MyConnection.getConnection()) {

            HashMap<String,Object> map = new HashMap<>();

            DetailReservation detail = DetailReservation.getById(conn, id);
            detail.deleteDetail(conn);

            List<Reservation> reservations = Reservation.getByUserId(conn, idUser);

            map.put("reservations",reservations);

            String url = "/frontOffice/listeReservation.jsp";
            ModelView mv = new ModelView(url,map);
            return mv;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    

}
