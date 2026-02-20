package model;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public abstract class Account {

	private String username; // NEED TO HASH AND SALT
	private String password; // NEED TO HASH AND SALT

	public Account(String username, String password) {
		this.username = username;
		this.password = password;
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

	// NEED TO VALIDATE USER THROUGH FRONT
	public void setPassword(String password) {
		this.password = password;
		// CALL DB AND UPDATE PASSWORD
	}

	// NEED TO CREATE HASHING AND SALTING STATIC METHODS TO SET AND GET INFO
	static class AccountSalting {

		/**
		 * Generates a random salt using {@link SecureRandom} and encodes it in Base64.
		 *
		 * @return the generated salt string
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
		 *
		 * @param password the plain-text password
		 * @param salt     the salt to use
		 * @return the hashed password string
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
