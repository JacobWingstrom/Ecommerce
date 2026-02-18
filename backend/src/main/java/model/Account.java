package model;

public abstract class Account {
	
	private String username; // NEED TO HASH AND SALT
	private String password; // NEED TO HASH AND SALT

	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	// NEED TO CREATE HASHING AND SALTING STATIC METHODS TO SET AND GET INFO

	public String getUsername() {
		return username;
	}

	// NEED TO VALIDATE USER THROUGH FRONT
	public void setUsername(String username) {
		this.username = username;
		// CALL DB AND UPDATE USERNAME AND ANY PREV ITEM WITH OLD USERNAME TO NEW USERNAME
	}

	public String getPassword() {
		return password;
	}

	// NEED TO VALIDATE USER THROUGH FRONT
	public void setPassword(String password) {
		this.password = password;
		// CALL DB AND UPDATE PASSWORD
	}
	
	
}
