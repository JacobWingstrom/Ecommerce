package service;

import java.sql.SQLException;
import java.util.List;

import javax.xml.crypto.Data;

import model.Account;
import model.Database;
import model.Item;
import model.ListType;
import model.Listing;

public abstract class BuyService {

	public static List<Item> getListingType(String username, int pageNum) throws SQLException {

		Account acct = Database.getUserByUsername(username);

        if (acct == null) return null;

		return Database.getStoreItems(pageNum).getItemListings();
	}

    public static Item getItemById(int itemId) throws SQLException {
		return Database.getItemByItemId(itemId);
	}

}
