package model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
/**
 * This class manages the database connection pool and provides methods for interacting
 * with the MySQL database, including user authentication, item management, account updates
 * and management, and bid management. 
 */
public class Database {
	//Create a maximum pool of 5 connections, managed wiht a queue of available connections and a set of used ones
	private static final int POOL_SIZE = 5;
	private static Queue<Connection> availableConnections = new LinkedList<Connection>();
	private static Set<Connection> usedConnections = new HashSet<Connection>();
	//URL of the Docker container running MySQL
	private static final String URL = "jdbc:mysql://127.0.0.1:3307/ecommerce?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
	private static final String USER = "root";
	private static final String PASSWORD = "RootPass123!";
	private static final int pageSize = 20;
	static {
		initializePool();
	}
	/**
	 * Initializes the connection pool by loading the MySQL JDBC driver and creating
	 * a set of connections to to be used for database interactions.
	 * 
	 * This method takes no parameters
	 * @return void
	 * Precondition:
	 * 	- The MySQL JDBC driver is available in the classpath
	 * Postcondition:
	 *  - The connection pool is initialized with a set of connections ready for use
	 */
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
	/**
	 * Creates a new connection to the MySQL database using the specified URL,
	 * username, and password. This method is used internally by the connection pool
	 * @return a new Connection object to the database
	 */
	private static Connection createNewConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	/**
	 * Retrieves a connection from the pool of available connections. If no connections are
	 * available, a new connection is created. The method checks if the retrieved connection is
	 * closed and if so, it attempts to create a new connection. 
	 * @return a Connection object that can be used for database interactions or null
	 * if a connection cannot be established
	 */
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
	/**
	 * Releases a connection back to the pool of available connections. 
	 * @param con - the connection to be returned to the pool
	 * @return void
	 */
	public static synchronized void releaseConnection(Connection con) {
		if (con == null) {
			return;
		}
		if (usedConnections.remove(con)) {
			availableConnections.offer(con);
		}
	}
	/**
	 * Shuts down the connection pool by closing all connections in both the available
	 * and used sets. 
	 * @return void
	 */
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

		/**
	 * Retrieves an Account object from the database based on the provided username.
	 * @param username - the username of the account to be retrieved
	 * @return - an Account object containing the user's details if found, null otherwise
	 * @throws SQLException
	 */
	public static Account getUserByToken(String token) throws SQLException {
		token = token.trim(); // Trim whitespace
		Connection con = getConnection();
		if (con == null) {
			return null;
		}
		String query = "SELECT user_id, username, password_hashed, salt, area, token FROM users WHERE token = ?";
		try {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, token);
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

	/**
	 * Retrieves an Account object from the database based on the provided username.
	 * @param username - the username of the account to be retrieved
	 * @return - an Account object containing the user's details if found, null otherwise
	 * @throws SQLException
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
	/**
	 * Authenticates a user by comparing the provided username and password against the stored credentials
	 * @param username - the username of the account to be authenticated
	 * @param password - the plaintext password provided for authentication
	 * @return an Account object if authentication is successful, null otherwise
	 * @throws SQLException
	 */
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
	/**
	 * Updates the username of the specified account in the database. 
	 * @param account - the Account object representing the user whose username is to be updated
	 * @param newUsername - the new username to be set for the account
	 * @return void
	 */
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
	/**
	 * Updates the password of the specified account in the database.
	 * @param account - the Account object representing the user whose password is to be updated
	 * @param hashedPassword - the new hashed password to be set for the account
	 */
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
	/**
	 * Adds a new user account to the database with the details provided in the Account object.
	 * @param account - the Account object containing the user's details to be added to the database
	 * @return void
	 */
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
	/**
	 * Retrieves a list of Item objects representing the items bought by the user wiht the specified user ID.
	 * @param userId - the ID of the user whose bought items are to be retrieved
	 * @return a List of Item objects representing the items bought by the user, or null if an error occurrs
	 */
	public static List<Item> getUserItemsBought(int userId) {
		List<Item> items = new ArrayList<>();
		try (Connection con = getConnection()) {
			String query = "SELECT * FROM items WHERE highest_bidder_id = ? AND sold = TRUE ORDER BY end_time DESC";
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
	/**
	 * Helper method to build an Item object from a ResultSet. This method extracts the relevant
	 * fields from the ResultSet and constructs an Item object with those values. 
	 * @param rs - the ResultSet containing the item data retrieved from the database
	 * @return - an Item object constructed from the data in the ResultSet
	 * @throws SQLException
	 */
	private static Item buildItem(ResultSet rs) throws SQLException {

		int itemId = rs.getInt("item_id");
		String name = rs.getString("name");
		String description = rs.getString("description");
		BigDecimal currPrice = rs.getBigDecimal("curr_price");
		int sellerId = rs.getInt("seller_id");
		Integer highestBidderId = rs.getInt("highest_bidder_id");
		byte[] image = rs.getBytes("image");
		boolean sold = rs.getBoolean("sold");
		if (rs.wasNull()) {
    		highestBidderId = null;
		}

		LocalDateTime endTime = rs.getTimestamp("end_time").toLocalDateTime();

		String tag = rs.getString("tag");

		return new Item(name, description, tag, itemId, currPrice, endTime, image, sold);
	}
	/**
	 * Retrieves a List of Item objects representing the items sold by the user with the specified user ID.
	 * @param userId - the ID of the user whose sold items are to be retrieved
	 * @return a List of Item objects representing the items sold by the user or null if an error occurs.
	 */
	public static List<Item> getUserItemsSold(int userId) {
		List<Item> items = new ArrayList<>();
		try (Connection con = getConnection()) {
			String query = "SELECT * FROM items WHERE seller_id = ? AND sold = TRUE ORDER BY end_time DESC";
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
	/**
	 * Retrieves a List of Item objects representing the items sold by the user with the specified user ID.
	 * @param userId - the ID of the user whose sold items are to be retrieved
	 * @return a List of Item objects representing the items sold by the user or null if an error occurs.
	 */
	public static Item getItemByItemId(int userId) {
		Item item = null;
		try (Connection con = getConnection()) {
			String query = "SELECT * FROM items WHERE item_id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				item = buildItem(rs);
			}
			releaseConnection(con);
			return item;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * This method adds a record to the database indicating that a user has bought an item from another user.
	 * @param buyerId - the ID of the user who bought the item
	 * @param sellerId - the ID of the user who sold the item
	 * @param item - the Item object representing the item that was bought.
	 * @return void
	 */
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
	/**
	 * Marks an item as sold in the database by updating the corresponding record with the buyer's ID.
	 * @param itemId - the ID of hte item that was sold
	 * @param buyerId - the ID of the user who bought the item
	 * @return void
	 */
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
	/**
	 * Retrieves a Listing object containing a list of Items currently available in the store, w
	 * with a maximum of 20 items per page. The method calculates the appropriate offset based on the 
	 * provided page number. 
	 * @param pageNumber - the page number for which to retrieve the store items (the nth 20 items)
	 * @return a Listing object containing a List of Items currently available in the store, or null if there was an error. 
	 */
	public static Listing getStoreItems(int pageNumber){
		try(Connection con = getConnection()){
			int offset = (pageNumber - 1) * 20;
			String query = "SELECT * FROM items ORDER BY end_time DESC LIMIT 20 OFFSET ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, offset);
			ResultSet rs = stmt.executeQuery();
			Listing listing = new Listing();
			while(rs.next()){
				Item item = buildItem(rs);
				listing.addItem(item);
				System.out.println("Row found: " + item.getUsername());
			}
			return listing;

		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Retrieves a Listing object containing a list of Items that the user with the specified user ID has 
	 * currently listed for sale on the market, with a maximum of 20 items per page.
	 * @param userId - the ID of the user whose listed items are to be retrieved
	 * @param pageNumber - the page number for which to retrieve the user's listed items (the nth 20 items)
	 * @return a Listing object containing a List of Items that the user has currently listed for sale on the market.
	 */
	public static Listing getUserItemsOnMarket(int userId, int pageNumber){
		try(Connection con = getConnection()){
			int offset = (pageNumber - 1) * 20;
			String query = "SELECT * FROM items WHERE seller_id = ? AND sold = false ORDER BY end_time DESC LIMIT 20 OFFSET ?";
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

	/**
	 * Adds a new item to the database with the details provided in the Item object and associates it with
	 * the specified seller ID. 
	 * @param item - the Item object containing the details of the item to be added to the database
	 * @param sellerId - the ID of the user who is selling the item
	 * @return void
	 */
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
							"    sold,\r\n" + //
							"    `image`\r\n" + //
							")\r\n" + //
							"VALUES (?, ?, ?, ?, NULL, ?, FALSE, ?, FALSE, ?);";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, sellerId);
			stmt.setString(2, item.getUsername());
			stmt.setString(3, item.getDescription());
			stmt.setBigDecimal(4, item.getHighestBid());
			stmt.setTimestamp(5, Timestamp.valueOf(item.getEndTime()));
			stmt.setString(6, item.getTag());
			stmt.setBytes(7, item.getImage());
			stmt.executeUpdate();
			stmt.close();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	/**
	 * Updates the details of an existing item in the database with the information provide in the Item object and
	 * the specified highest bidder ID. 
	 * @param item - the Item object containing the updated details of the item to be updated in the database
	 * @param highest_bidder_id - the ID of the user who currently has the highest bid on the item
	 * @return void
	 */
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
			stmt.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	/**
	 * Retrieves a List of UserBidItem objects representing the items that the user with the specified
	 * user ID has placed bids on along with the details of the user's bid on each item. 
	 * @param userId - the ID of the user whose bid items are to be retrieved
	 * @param pageNumber - the page number for which to retrieve the user's bid items (the nth 20 items)
	 * @return an ArrayList of UserBidItem objects representing the items that the user has placed bids on
	 */
	public static ArrayList<UserBidItem> getUserItemsBidOn(int userId, int pageNumber){
		int pageSize = 20;
		int offset = (pageNumber - 1) * pageSize;

		try (Connection con = getConnection()) {
			String query =
				"SELECT i.item_id, i.name, i.description, i.tag, i.curr_price AS current_highest_bid, i.end_time, " +
				"b.bid_id AS user_bid_id, b.amount AS user_bid_amount, b.timestamp AS user_bid_time " +
				"FROM items i " +
				"JOIN bids b ON i.item_id = b.item_id AND b.bidder_id = ? " +
				"WHERE i.sold = FALSE " +
				"GROUP BY i.item_id, i.name, i.description, i.tag, i.curr_price, i.end_time, b.bid_id, b.amount, b.timestamp " +
				"LIMIT ? OFFSET ? ORDER BY i.end_time DESC";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, userId);
			stmt.setInt(2, pageSize);
			stmt.setInt(3, offset);

			ResultSet rs = stmt.executeQuery();
			ArrayList<UserBidItem> listing = new ArrayList<>();

			while (rs.next()) {
				// Build the Item object
				Item item = new Item(
					rs.getString("name"),
					rs.getString("description"),
					rs.getString("tag"),
					rs.getInt("item_id"),
					rs.getBigDecimal("curr_price"),
					rs.getTimestamp("end_time").toLocalDateTime(),
					rs.getBytes("image"),
					rs.getBoolean("sold")
				);

				// Build the user's Bid object
				Bid userBid = new Bid(
					rs.getInt("user_bid_id"),
					userId,
					rs.getInt("item_id"),
					rs.getBigDecimal("user_bid_amount"),
					rs.getTimestamp("user_bid_time").toLocalDateTime()
				);

				// Wrap in a composite object if Listing is designed for that
				listing.add(new UserBidItem(item, userBid));  // or listing.addItem(new UserBidItem(item, userBid));
			}

			return listing;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Adds a bid to the database
	 * @param bid - the Bid object containing the details of the bid to be added to the database
	 * @return void
	 */
	public static void addBidToDatabase(Bid bid){
		try(Connection con = getConnection()){
			String query = "INSERT INTO bids (bidder_id, item_id, amount) VALUES (?, ?, ?)";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, bid.getBidderId());
			stmt.setInt(2, bid.getItemId());
			stmt.setBigDecimal(3, bid.getAmount());
			stmt.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	public static void setBidAndHighestBidderToDatabase(Item item) {
		try(Connection con = getConnection()){
			String updateItem = "UPDATE items SET curr_price = ?, highest_bidder_id = ? WHERE item_id = ?";
			PreparedStatement itemStmt = con.prepareStatement(updateItem);
			itemStmt.setBigDecimal(1, item.getHighestBid());
			itemStmt.setInt(2, item.getHighestBidderId());
			itemStmt.setInt(3, item.getItemId());
			itemStmt.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	/**
	 * updates the current highest bid on an item in the database with the specified bid ID
	 * and amount that the bid is being updated to.
	 * @param bidId - the ID of the bid to be updated
	 * @param amount - the new amount to be set for the bid. 
	 */
	public static void setBidOnItem(int bidId, BigDecimal amount){
		try(Connection con = getConnection()){

			// 1. Update the bid
			String updateBid = "UPDATE bids SET amount = ? WHERE bid_id = ?";
			PreparedStatement bidStmt = con.prepareStatement(updateBid);
			bidStmt.setBigDecimal(1, amount);
			bidStmt.setInt(2, bidId);
			bidStmt.executeUpdate();

			// 2. Get item_id
			String getItem = "SELECT item_id FROM bids WHERE bid_id = ?";
			PreparedStatement itemStmt = con.prepareStatement(getItem);
			itemStmt.setInt(1, bidId);
			ResultSet rs = itemStmt.executeQuery();

			if(rs.next()){
				int itemId = rs.getInt("item_id");

				// 3. Update item with TRUE highest bid
				String updateItem = 
					"UPDATE items SET curr_price = (" +
					"   SELECT MAX(amount) FROM bids WHERE item_id = ?" +
					"), highest_bidder_id = (" +
					"   SELECT bidder_id FROM bids " +
					"   WHERE item_id = ? " +
					"   ORDER BY amount DESC FETCH FIRST 1 ROW ONLY" +
					") WHERE item_id = ?";

				PreparedStatement updateStmt = con.prepareStatement(updateItem);
				updateStmt.setInt(1, itemId);
				updateStmt.setInt(2, itemId);
				updateStmt.setInt(3, itemId);
				updateStmt.executeUpdate();
			}

		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	public static void setExpiredItemsSold(){
		try(Connection con = getConnection()) {
			String query = "UPDATE items SET sold = TRUE WHERE end_time <= NOW() AND sold = FALSE";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	//Methods for availability: insertinb a block, getting blocks for a user, deleting a block, overlap query
	public static boolean updateAvailablity(int userId, Availability blocks){
		try(Connection con  = getConnection()){
			String deleteStatement = "DELETE FROM availablity WHERE user_id = ?";
			PreparedStatement deleteStmt = con.prepareStatement(deleteStatement);
			deleteStmt.setInt(1, userId);
			deleteStmt.executeUpdate();
			String insertStatement = "INSERT INTO availability (user_id, date, start_time, end_time) VALUES (?, ?, ?, ?)";
			PreparedStatement insertStmt = con.prepareStatement(insertStatement);
			for(AvailabilityBlock block : blocks.getBlocks()){
				insertStmt.setInt(1, userId);
				insertStmt.setDate(2, java.sql.Date.valueOf(block.getDate()));
				insertStmt.setTime(3, java.sql.Time.valueOf(block.getStart()));
				insertStmt.setTime(4, java.sql.Time.valueOf(block.getEnd()));
				insertStmt.addBatch();
			}
			insertStmt.executeBatch();
			return true;

		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}
	public static Availability getUserAvailability(int userId){
		try(Connection con = getConnection()){
			String query = "SELECT date, start_time, end_time FROM availability WHERE user_id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			Availability availability = new Availability();
			while(rs.next()){
				AvailabilityBlock block = new AvailabilityBlock(
					rs.getTime("start_time").toLocalTime(),
					rs.getTime("end_time").toLocalTime(),
					rs.getDate("date").toLocalDate(),
					rs.getInt("block_id"),
					rs.getInt("user_id")
				);
				availability.addBlock(block);
			}
			return availability;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	public static List<Timeslot> getOverlap(int userA, int userB){
		List<Timeslot> overlaps = new ArrayList<>();
		String sql = "SELECT \r\n" + //
						"    a1.date,\r\n" + //
						"    GREATEST(a1.start_time, a2.start_time) AS overlap_start,\r\n" + //
						"    LEAST(a1.end_time, a2.end_time) AS overlap_end\r\n" + //
						"FROM availability a1\r\n" + //
						"JOIN availability a2\r\n" + //
						"  ON a1.date = a2.date\r\n" + //
						"WHERE a1.user_id = ?\r\n" + //
						"  AND a2.user_id = ?\r\n" + //
						"  AND a1.start_time < a2.end_time\r\n" + //
						"  AND a2.start_time < a1.end_time\r\n" + //
						"ORDER BY a1.date, overlap_start;";
		try(Connection con = getConnection()){
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, userA);
			stmt.setInt(2, userB);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				LocalDate date = rs.getDate("data").toLocalDate();
				LocalTime startTime = rs.getTime("overlap_start").toLocalTime();
				LocalTime endTime = rs.getTime("overlap_end").toLocalTime();
				LocalDateTime start = LocalDateTime.of(date, startTime);
				LocalDateTime end = LocalDateTime.of(date, endTime);
				overlaps.add(new Timeslot(start, end));
			}
			return overlaps;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	/*
	LIST OF ALL ADMIN-RELATED METHODS 

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