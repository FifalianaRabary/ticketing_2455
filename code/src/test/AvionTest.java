package test;

import controller.Avion;
import myconnection.MyConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class AvionTest {
    public static void main(String[] args) {
    
        try (Connection conn = MyConnection.getConnection()) {
            int idTest = 1; // ID de l'avion à tester

            Avion avion = Avion.getById(conn, idTest);
            if (avion != null) {
                System.out.println("Avion trouvé :");
                System.out.println("ID : " + avion.getId());
                System.out.println("Désignation : " + avion.getDesignation());
                System.out.println("ID Modèle : " + avion.getId_modele());
                System.out.println("Date de fabrication : " + avion.getDate_fabrication());
            } else {
                System.out.println("Aucun avion trouvé avec l'ID " + idTest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
