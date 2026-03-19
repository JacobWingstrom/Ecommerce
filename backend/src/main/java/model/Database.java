package model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public abstract class Database {
	private static final int POOL_SIZE = 5;
	private static Queue<Connection> availableConnections = new LinkedList();
	private static Set<Connection> usedConnections = new HashSet<>();
	private static final String URL = "jdbc:mysql://127.0.0.1:3307/ecommerce?useSSL=false&serverTimezone=UTC";
	private static final String USER = "root";
	private static final String PASSWORD = "RootPass123!";
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

	private static Connection createNewConnection() throws Exception {
		return DriverManager.getConnection(URL, USER, PASSWORD);

	}

	public static synchronized Connection getConnection() {
		if (availableConnections.isEmpty()) {
			System.out.println("No available connections in pool.");
			return null;
		} else {
			Connection con = availableConnections.poll();
			usedConnections.add(con);
			return con;
		}
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
		try (Connection con = getConnection()) {
			String query = "SELECT user_id, username, password_hashed, salt, area FROM users WHERE username = ?";
			PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, username);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					releaseConnection(con);
					return new Account(rs.getString("username"), rs.getString("password_hashed"), rs.getString("salt"),
							rs.getString("area"), rs.getInt("user_id"), rs.getString("token"));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Account authenticate(String username, String password) throws SQLException {
		Account user = getUserByUsername(username);

		if (user == null) {
			return null;
		}
		String salt = user.getSalt();
		String hashedPassword = Account.saltPassword(password, salt);

		if (hashedPassword.equals(user.getPassword())) {
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
			stmt.setString(2, account.getPassword());
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
		try (Connection con = getConnection()) {
			String query = "INSERT INTO users (username, password_hashed, salt, area) " + "VALUES (?, ?, ?, ?);";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setString(3, salt);
			stmt.setString(4, area);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt(1);
				account.setUserId(id);
			}
			releaseConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<Item> getUserItemsBought(int userId) {
		List<Item> items = new ArrayList<>();
		try (Connection con = getConnection()) {
			String query = "SELECT * FROM items WHERE highest_bidder_id = ? AND sold = TRUE";
			PreparedStatement stmt = con.prepareStatement(query);
			String userIdString = Integer.toString(userId);
			stmt.setString(1, userIdString);
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
		int highestBidderId = rs.getInt("highest_bidder_id");

		LocalDateTime endTime = rs.getTimestamp("end_time").toLocalDateTime();

		String tag = rs.getString("tag");

		return new Item(name, description, tag, endTime);
	}

	public static List<Item> getUserItemsSold(int userId) {
		List<Item> items = new ArrayList<>();
		try (Connection con = getConnection()) {
			String query = "SELECT * FROM items WHERE seller_id = ? AND sold = TRUE";
			PreparedStatement stmt = con.prepareStatement(query);
			String userIdString = Integer.toString(userId);
			stmt.setString(1, userIdString);
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
		try (Connection con = getConnection()) {
			String query = "INSERT INTO items (seller_id, name, description, curr_price, highest_bidder_id, end_time, approved_flag, sold) "
					+ "VALUES (?, ? ?, ?, NULL, ?, 0, ?, FALSE)";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, sellerId);
			stmt.setString(2, item.getUsername());
			stmt.setString(3, item.getDescription());
			stmt.setBigDecimal(4, item.getHighestBid());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void markItemSold(int itemId, int buyerId) {
		try (Connection con = getConnection()) {
			String query = "UPDATE items SET sold = TRUE, highest_bidder_id = ?, WHERE item_id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, buyerId);
			stmt.setInt(2, itemId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*
	 * public static List<Listing> getStoreItems(); public static List<Listing>
	 * getUserItemsOnMarket(Account account); public static List<Listing>
	 * getNewlyListedItems(); public static void addItem(Item item); public static
	 * void updateItem(Item item); public static Bid getBidOnItem(Item item); public
	 * static void setBidOnItem(Item item, Bid bid); public Availability
	 * getUserAvailability(Account account); public void setUserAvailability(Account
	 * account, Availability availability); public void addUserReport(Report
	 * report); public List<Report> getUserReports(); public List<Account>
	 * getActiveUsers(); public void banUser(String username); public void
	 * suspendUser(Account account, LocalDate suspensionEndDate); public boolean
	 * isUserBanned(Account account); public LocalDate getUserSuspensionEnd(Account
	 * account);
	 */
	

}
