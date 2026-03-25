package service;

import java.sql.SQLException;
import java.util.List;

import model.Account;
import model.Database;
import model.Item;
import model.ListType;
import model.Listing;

public abstract class UserService {

	public static Listing getListingType(String username, ListType type) throws SQLException {

		Account acct = Database.getUserByUsername(username);

		if (type == ListType.CurrentBidItems) {
			// LANE MAKE METHOD TO GET ALL USERS current items they're bidding on
//			return DataBase.
		} else if (type == ListType.CurrentItemsSold) {
			return Database.getUserItemsOnMarket(acct.getUserID(), 0);
		}

		return null;
	}

}
