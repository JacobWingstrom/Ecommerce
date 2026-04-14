package service;

import java.sql.SQLException;
import java.util.List;
import model.Account;
import model.Database;
import model.Item;
import model.ListType;

public abstract class UserService {

	public static List<Item> getListingType(String username, ListType type, int pageNum) throws SQLException {

		Account acct = Database.getUserByUsername(username);

		if (type == ListType.CurrentBidItems) {
			// LANE MAKE METHOD TO GET ALL USERS current items they're bidding on
//			return DataBase.
			// UPDATE NAME
		} else if (type == ListType.CurrentItemsOnMarket) {
			return Database.getUserItemsOnMarket(acct.getUserID(), pageNum).getItemListings();
		} else if (type == ListType.AllItemsWon) {
			return Database.getUserItemsBought(acct.getUserID());
		}
		return null;
	}

}
