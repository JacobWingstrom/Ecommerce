package model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Database {
	private static final int POOL_SIZE = 5;
	private static Queue<Connection> availableConnections = new LinkedList<Connection>();
	private static Set<Connection> usedConnections = new HashSet<Connection>();
	private static final String URL = "jdbc:mysql://127.0.0.1:3307/ecommerce?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
	private static final String USER = "root";
	private static final String PASSWORD = "RootPass123!";
	private static final int pageSize = 20;
	static {
		initializePool();
	}

	private static void initializePool() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			for (int i = 0; i < POOL_SIZE; i++) {
				availableConnections.add(createNewConnection());
			}
			System.out.println("Database connection pool initialized");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Connection createNewConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	public static synchronized Connection getConnection() {
		Connection con = null;
		if (!availableConnections.isEmpty()) {
			con = availableConnections.poll();
			try {
				if (con.isClosed()) {
					con = createNewConnection();
				}
			} catch (SQLException e) {
				try {
					con = createNewConnection();
				} catch (SQLException ex) {
					ex.printStackTrace();
					return null;
				}
			}
		} else {
			try {
				con = createNewConnection();
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		usedConnections.add(con);
		return con;
	}

	public static synchronized void releaseConnection(Connection con) {
		if (con == null) {
			return;
		}
		if (usedConnections.remove(con)) {
			availableConnections.offer(con);
		}
	}

	public static void shutdownPool() {
		try {
			for (Connection con : availableConnections) {
				con.close();
			}
			for (Connection con : usedConnections) {
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
	 * Required methods:
	 * 
	 * Setters return a boolean
	 * 
	 * Getters return a ResultSet
	 * 
	 * Create a class JSONMake class that will consist of static methods to return
	 * JSON to the frontend
	 * 
	 * Create a class JSONTranslate that will translate JSON into processed
	 * information to interact with the database
	 * 
	 * getUsername - get the queried user's username setUsername - set the queried
	 * user's username getPassword - get the queried user's password setPassword -
	 * set the queried user's password getUserItemsBought - get the queried user's
	 * items bought getUserItemsSold - get the queried user's items sold
	 * getStoreItems - get the list of items currently in the store
	 * getUserItemsOnMarket - get the queried's users items they currently have on
	 * the market getUserAvailability - get the queried user's availability
	 * setUserAvailability - set the queried user's availability setItemInDatabase -
	 * set the details of an item in the database getBidOnItem - get a bid on an
	 * item addUserItemSold - add to the queried user's list of items sold
	 * addUserItemBought - add to the queried user's list of items bought
	 * setBidOnItem - set a bid on an item addUserReport - add a report from a user
	 * to the database getUserReports - get the list of current reports from the
	 * database getActiveUsers - get teh list of active users getNewlyListedItems -
	 * get the list of items that are newly list banUser - ban a user from the
	 * platform suspendUser - suspend a user from the platform isUserBanned - get
	 * whether a user is banned isUserSuspended - represented as a String, gets the
	 * date the user suspension ends
	 * 
	 * 
	 */
	public static Account getUserByUsername(String username) throws SQLException {
		username = username.trim(); // Trim whitespace
		Connection con = getConnection();
		if (con == null) {
			return null;
		}
		String query = "SELECT user_id, username, password_hashed, salt, area, token FROM users WHERE username = ?";
		try {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, username);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return new Account(rs.getString("username"), rs.getString("password_hashed"), rs.getString("salt"),rs.getString("area"), rs.getInt("user_id"), rs.getString("token"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
		System.out.println("returning null");
		return null;
	}

	public static Account authenticate(String username, String password) throws SQLException {
		Account user = getUserByUsername(username);

		if (user == null) {
			System.out.println("user is null");
			return null;
		}
		String salt = user.getSalt();
		String hashedPassword = Account.saltPassword(password, salt);

		if (hashedPassword.equals(user.getPassword())) {
		 	System.out.println("hash = password");
		 	return user;
		}
		
		return null;
	}

	public static void updateUsername(Account account, String newUsername) {
		try (Connection con = getConnection()) {

			String query = "UPDATE users SET username = ? WHERE username = ?";
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setString(1, newUsername);
			stmt.setString(2, account.getUsername());

			stmt.executeUpdate();
			account.setUsername(newUsername);
			releaseConnection(con);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updatePassword(Account account, String hashedPassword) {
		try (Connection con = getConnection()) {
			String query = "UPDATE users SET password_hashed = ? WHERE username = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, hashedPassword);
			stmt.setString(2, account.getUsername());
			stmt.executeUpdate();
			account.setPassword(hashedPassword);
			releaseConnection(con);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addUserToDatabase(Account account) {
		String username = account.getUsername();
		String password = account.getPassword();
		String salt = account.getSalt();
		String area = account.getArea();
		Connection con = getConnection();
		if (con == null) {
			System.err.println("Unable to get connection for addUserToDatabase.");
			return;
		}
		String query = "INSERT INTO users (username, password_hashed, salt, area, token) VALUES (?, ?, ?, ?, ?)";
		try {
			PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setString(3, salt);
			stmt.setString(4, area);
			stmt.setString(5, account.getToken());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt(1);
				account.setUserId(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
	}

	public static List<Item> getUserItemsBought(int userId) {
		List<Item> items = new ArrayList<>();
		try (Connection con = getConnection()) {
			String query = "SELECT * FROM items WHERE highest_bidder_id = ? AND sold = TRUE";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Item item = buildItem(rs);
				items.add(item);
			}
			releaseConnection(con);
			return items;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Item buildItem(ResultSet rs) throws SQLException {

		int itemId = rs.getInt("item_id");
		String name = rs.getString("name");
		String description = rs.getString("description");
		BigDecimal currPrice = rs.getBigDecimal("curr_price");
		int sellerId = rs.getInt("seller_id");
		Integer highestBidderId = rs.getInt("highest_bidder_id");
		if (rs.wasNull()) {
    		highestBidderId = null;
		}

		LocalDateTime endTime = rs.getTimestamp("end_time").toLocalDateTime();

		String tag = rs.getString("tag");

		return new Item(name, description, tag, endTime);
	}

	public static List<Item> getUserItemsSold(int userId) {
		List<Item> items = new ArrayList<>();
		try (Connection con = getConnection()) {
			String query = "SELECT * FROM items WHERE seller_id = ? AND sold = TRUE";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Item item = buildItem(rs);
				items.add(item);
			}
			releaseConnection(con);
			return items;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void addUserItemBought(int buyerId, int sellerId, Item item) {
		Connection con = getConnection();
		if (con == null) {
			System.err.println("Unable to get connection for addUserItemBought.");
			return;
		}
		String query = "INSERT INTO items (seller_id, name, description, curr_price, highest_bidder_id, end_time, approved_flag, sold) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, FALSE)";
		try {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, sellerId);
			stmt.setString(2, item.getUsername());
			stmt.setString(3, item.getDescription());
			stmt.setBigDecimal(4, item.getHighestBid());
			stmt.setInt(5, buyerId);
			stmt.setTimestamp(6, null);
			stmt.setBoolean(7, false);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
	}

	public static void markItemSold(int itemId, int buyerId) {
		Connection con = getConnection();
		if (con == null) {
			System.err.println("Unable to get connection for markItemSold.");
			return;
		}
		String query = "UPDATE items SET sold = TRUE, highest_bidder_id = ? WHERE item_id = ?";
		try {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, buyerId);
			stmt.setInt(2, itemId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
	}
	public static Listing getStoreItems(int pageNumber){
		try(Connection con = getConnection()){
			int offset = (pageNumber - 1) * 20;
			String query = "SELECT * FROM items LIMIT 20 OFFSET ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, offset);
			ResultSet rs = stmt.executeQuery();
			Listing listing = new Listing();
			while(rs.next()){
				Item item = buildItem(rs);
				listing.addItem(item);
			}
			return listing;

		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	public static Listing getUserItemsOnMarket(int userId, int pageNumber){
		try(Connection con = getConnection()){
			int offset = (pageNumber - 1) * 20;
			String query = "SELECT * FROM items WHERE seller_id = ? AND sold = false ORDER BY item_id LIMIT 20 OFFSET ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, userId);
			stmt.setInt(2, offset);
			ResultSet rs = stmt.executeQuery();
			Listing listing = new Listing();
			while(rs.next()){
				Item item = buildItem(rs);
				listing.addItem(item);
			}
			return listing;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	public static void addItem(Item item, int sellerId){
		try(Connection con  = getConnection()){
			String query = "INSERT INTO items (\r\n" + //
							"    seller_id,\r\n" + //
							"    name,\r\n" + //
							"    description,\r\n" + //
							"    curr_price,\r\n" + //
							"    highest_bidder_id,\r\n" + //
							"    end_time,\r\n" + //
							"    approved_flag,\r\n" + //
							"    tag,\r\n" + //
							"    sold\r\n" + //
							")\r\n" + //
							"VALUES (?, ?, ?, ?, NULL, ?, FALSE, ?, FALSE);";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, sellerId);
			stmt.setString(2, item.getUsername());
			stmt.setString(3, item.getDescription());
			stmt.setBigDecimal(4, item.getHighestBid());
			stmt.setTimestamp(5, Timestamp.valueOf(item.getEndTime()));
			stmt.setString(6, item.getTag());
			stmt.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public static void updateItem(Item item, int highest_bidder_id){
		try(Connection con = getConnection()){
			String query = "UPDATE ITEMS" + 
			" SET name = ?, description = ?, curr_price = ?"
			+ ", highest_bidder_id = ?, end_time = ?, tag = ? WHERE item_id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, item.getUsername());
			stmt.setString(2, item.getDescription());
			stmt.setBigDecimal(3, item.getHighestBid());
			stmt.setInt(4, highest_bidder_id);
			stmt.setTimestamp(5, Timestamp.valueOf(item.getEndTime()));
			stmt.setString(6, item.getTag());
			stmt.setInt(7, item.getItemId());
			stmt.executeUpdate()
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	/*
	 
	 getNewlyListedItems();
	 public static Bid getBidOnItem(Item item); public
	 static void setBidOnItem(Item item, Bid bid); public Availability
	 getUserAvailability(Account account); public void setUserAvailability(Account
	 account, Availability availability); public void addUserReport(Report
	 report); public List<Report> getUserReports(); public List<Account>
	 getActiveUsers(); public void banUser(String username); public void
	 suspendUser(Account account, LocalDate suspensionEndDate); public boolean
	 isUserBanned(Account account); public LocalDate getUserSuspensionEnd(Account
	 account);
	 */
	

}