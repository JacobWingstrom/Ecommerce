package service;

import java.sql.SQLException;
import java.util.List;

import model.Account;
import model.Buyer;
import model.Database;
import model.Item;

public class AuthService {

	public static boolean AddUser(String username, String password, String area) throws SQLException {

		if (Database.authenticate(username, password) == null) {
			Database.addUserToDatabase(new Buyer(username, password, area));
			return true;
		}
		return false;
	}

	// FIX THIS METHOD, DONT KNOW WHAT TO DO WITH THE LISTS (BUYER/SELLER)
	public static Account signInUser(String username, String password) throws SQLException {

		Account acct = Database.authenticate(username, password);
		if (acct == null) {
			return null;
		}

//		List<Item> buyerBoughtList = Database.getUserItemsBought(0);
//		List<Item> buyerBidList = Database.getUserItemsBought(0);
//
//		List<Item> sellerSoldList = Database.getUserItemsSold(0);
//		List<Item> sellerSellingList = Database.getUserItemsSold(0);

		return acct;
	}

	// MAKE METHOD FOR LOGOUT

}
