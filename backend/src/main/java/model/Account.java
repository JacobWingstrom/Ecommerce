package model;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;
import service.JWTTokenGenerator;

public class Account {

	private String username; // NEED TO HASH AND SALT? -> Say not Needed
	private String password;
	private String salt;
	private String area;
	private int userId;
	private Availabilty availabilty;
	private String token;

	public Account(String username, String password) {

		this.salt = AccountSalting.generateSalt();
		this.username = username;
		this.password = AccountSalting.hashPassword(password, this.salt);
		this.area = "UnKnown";
		userId = -1;
		this.token = createToken();
	}

	/**
	 * This constructor is used specifically when loading an account from the
	 * database
	 */
	public Account(String username, String passwordHash, String salt, String area, int userId) {
		this.username = username;
		this.password = passwordHash;
		this.salt = salt;
		this.area = area;
		this.userId = userId;
	}

	private String createToken() {
		return JWTTokenGenerator.generateToken(username);
	}

	public String getToken() {
		return this.token;
	}

	public String getUsername() {
		return username;
	}

	// NEED TO VALIDATE USER THROUGH FRONT
	public void setUsername(String username) {
		this.username = username;
		// CALL DB AND UPDATE USERNAME AND ANY PREV ITEM WITH OLD USERNAME TO NEW
		// USERNAME
	}

	public String getPassword() {
		return password;
	}

	public static String saltPassword(String password, String salt) {
		return AccountSalting.hashPassword(password, salt);
	}

	// NEED TO VALIDATE USER THROUGH FRONT
	public void setPassword(String password) {

		this.password = AccountSalting.hashPassword(password, this.salt);
		// CALL DB AND UPDATE PASSWORD
	}

	public String getArea() {
		return area;
	}

	public void setArea(String newArea) {
		area = newArea;
	}

	public String getSalt() {
		return this.salt;
	}

	public int getUserID() {
		return this.userId;
	}

	public void setUserId(int newId) {
		this.userId = newId;
	}

	public Map<LocalDate, AvailabiltyBlock> getAvailabilty() {
		return availabilty.getAvailableTimes();
	}

	private static class AccountSalting {

		/**
		 * Generates a random salt using {@link SecureRandom} and encodes it in Base64.
		 *
		 */
		public static String generateSalt() {
			SecureRandom random = new SecureRandom();
			byte[] salt = new byte[16];
			random.nextBytes(salt);
			return Base64.getEncoder().encodeToString(salt);
		}

		/**
		 * Hashes the given password with the specified salt using SHA-256 and encodes
		 * the result in Base64.
		 */
		public static String hashPassword(String password, String salt) {
			try {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				String saltedPassword = salt + password;
				byte[] hash = digest.digest(saltedPassword.getBytes());
				return Base64.getEncoder().encodeToString(hash);
			} catch (Exception e) {
				throw new RuntimeException("Error hashing password", e);
			}
		}
	}

}
