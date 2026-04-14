package service;

import java.sql.SQLException;
import model.Account;
import model.Database;

public abstract class AuthService {

	public static boolean AddUser(String username, String password) throws SQLException {

		if (Database.getUserByUsername(username) == null) {
			System.out.println("user no exist");
			Database.addUserToDatabase(new Account(username, password));
			return true;
		}
		return false;
	}

	public static Account signInUser(String username, String password) throws SQLException {
		Account acct = Database.authenticate(username, password);
		if (acct == null) {
			return null;
		}
		return acct;
	}

	public static Account getUserFromToken(String token) throws SQLException {
		Account acct = Database.getUserByToken(token);

		if (acct == null) {
			return null;
		}
		return acct;
	}

	// MAKE METHOD FOR LOGOUT

}
