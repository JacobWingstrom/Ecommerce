package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

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
    /**
    Required methods:

    Setters return a boolean

    Getters return a ResultSet

    Create a class JSONMake class that will consist of static methods to return JSON to the frontend

    Create a class JSONTranslate that will translate JSON into processed information to interact with the database

    getUsername - get the queried user's username
    setUsername - set the queried user's username
    getPassword - get the queried user's password
    setPassword - set the queried user's password
    getUserItemsBought - get the queried user's items bought
    getUserItemsSold  - get the queried user's items sold
    getStoreItems - get the list of items currently in the store
    getUserItemsOnMarket - get the queried's users items they currently have on the market
    getUserAvailability - get the queried user's availability
    setUserAvailability - set the queried user's availability
    setItemInDatabase - set the details of an item in the database
    getBidOnItem - get a bid on an item
    addUserItemSold - add to the queried user's list of items sold
    addUserItemBought - add to the queried user's list of items bought
    setBidOnItem - set a bid on an item
    addUserReport - add a report from a user to the database
    getUserReports - get the list of current reports from the database
    getActiveUsers - get teh list of active users
    getNewlyListedItems - get the list of items that are newly list
    banUser - ban a user from the platform
    suspendUser - suspend a user from the platform
    isUserBanned - get whether a user is banned
    isUserSuspended - represented as a String, gets the date the user suspension ends
    
    
    */
    public static Account getUserByUsername(String username);
    public static void updateUsername(Account account, String newUsername);
    public static void updatePassword(Account account, String hashedPassword);
    public List<Item> getUserItemsBought(Account account);
    public List<Item> getUserItemsSOld(Account account);
    public static void addUserItemBought(Account account, Item item);
    public static void addUserItemSold(Account account, Item item);
    public static List<Listing> getStoreItems();
    public static List<Listing> getUserItemsOnMarket(Account account);
    public static List<Listing> getNewlyListedItems();
    public static void addItem(Item item);
    public static void updateItem(Item item);
    public static Bid getBidOnItem(Item item);
    public static void setBidOnItem(Item item, Bid bid);
    public Availability getUserAvailability(Account account);
    public void setUserAvailability(Account account, Availability availability);
    public void addUserReport(Report report);
    public List<Report> getUserReports();
    public List<Account> getActiveUsers();
    public void banUser(String username);
    public void suspendUser(Account account, LocalDate suspensionEndDate);
    public boolean isUserBanned(Account account);
    public LocalDate getUserSuspensionEnd(Account account);
    
    
}


