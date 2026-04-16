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
			AuctionHandler.closeExpiredAuctions();
			return Database.getUserItemsBidOn(acct.getUserID(), pageNum).getItemListings();
		} else if (type == ListType.CurrentItemsOnMarket) {
			return Database.getUserItemsOnMarket(acct.getUserID(), pageNum).getItemListings();
		} else if (type == ListType.AllItemsWon) {
			AuctionHandler.closeExpiredAuctions();
			return Database.getUserItemsBought(acct.getUserID());
		}
		return null;
	}
}
