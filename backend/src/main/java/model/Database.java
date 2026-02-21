package model;
import java.sql.Connection;
import java.sql.DriverManager;



public class Database {
    public Database(){
        makeConnection();
    }
    private static Connection makeConnection(){
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3307/ecommerce?useSSL=false&serverTimezone=UTC",
                "root",
                "RootPass123!");
            if(con != null){
                System.out.println("Connected successfully to database.");
                return con;
            }
        } catch (Exception e) {
            System.out.println("Not connected to database.");
            e.printStackTrace();
        }
        return null;
    }
}
