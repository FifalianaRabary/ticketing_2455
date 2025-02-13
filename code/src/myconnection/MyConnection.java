package myconnection;


import java.sql.Connection;
import java.sql.DriverManager;

public class MyConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/ticketing";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Fifa16";

    public static Connection getConnection() throws Exception 
    {
        try {
            Class.forName("org.postgresql.Driver");
        } 
        catch (ClassNotFoundException e)  {
            e.printStackTrace();
            throw e ;
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
}
