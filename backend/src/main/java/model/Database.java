package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;


public class Database {
    private static final int POOL_SIZE = 5;
    private static Queue<Connection> availableConnections = new LinkedList();
    private static Set<Connection> usedConnections = new HashSet<>();
    private static final String URL = "jdbc:mysql://127.0.0.1:3307/ecommerce?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "RootPass123!";
    static{
        initializePool();
    }
    private static void initializePool(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for(int i = 0; i < POOL_SIZE; i++){
                availableConnections.add(createNewConnection());
            }
            System.out.println("Database connection pool initialized");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static Connection createNewConnection() throws Exception{
        return DriverManager.getConnection(URL, USER, PASSWORD);

    }
    public static synchronized Connection getConnection(){
        if(availableConnections.isEmpty()){
            System.out.println("No available connections in pool.");
            return null;
        }
        else{
            Connection con = availableConnections.poll();
            usedConnections.add(con);
            return con;
        }
    }
    public static synchronized void releaseConnection(Connection con){
        if(con == null){
            return;
        }
        if(usedConnections.remove(con)){
            availableConnections.offer(con);
        }
    }
    public static void shutdownPool(){
        try {
            for(Connection con : availableConnections){
                con.close();
            }
            for(Connection con: usedConnections){
                con.close();
            }
            availableConnections.clear();
            usedConnections.clear();
            System.out.println("Connection pool shut down.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


