package model;

import service.JWTTokenGenerator;
import service.PasswordHasher;

public class Account {

	private String username; // NEED TO HASH AND SALT? -> Say not Needed
	private String password;
	private String salt;
	private String area;
	private int userId;
	private Availability availability;
	private String token;

	public Account(String username, String password) {

		this.salt = PasswordHasher.generateSalt();
		this.username = username;
		this.password = PasswordHasher.hash(password, this.salt);
//		this.password = password;
		this.area = "UnKnown";
		userId = -1;
		this.token = createToken();
	}

	/**
	 * This constructor is used specifically when loading an account from the
	 * database
	 */
	public Account(String username, String passwordHash, String salt, String area, int userId, String token) {
		this.username = username;
		this.password = passwordHash;
		this.salt = salt;
		this.area = area;
		this.userId = userId;
		this.token = token;
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
		return PasswordHasher.hash(password, salt);
	}

	// NEED TO VALIDATE USER THROUGH FRONT
	public void setPassword(String password) {

		this.password = PasswordHasher.hash(password, this.salt);
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

	public Availability getAvailability(){
		return this.availability;
	}

}
