package service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import model.Account;
import model.Bid;
import model.Database;
import model.Item;

public abstract class BuyService {

	public static List<Item> getListingType(String username, int pageNum) throws SQLException {

		Account acct = Database.getUserByUsername(username);

		if (acct == null)
			return null;

		return Database.getStoreItems(pageNum).getItemListings();
	}

	public static Item getItemById(int itemId) throws SQLException {
		return Database.getItemByItemId(itemId);
	}

	public static Item setBid(Bid bid) throws SQLException {
		Item item = Database.getItemByItemId(bid.getItemId());

		if (item == null) {
			return null;
		}

		BigDecimal minimumBid = item.getHighestBid().add(BigDecimal.ONE);
		if (bid.getAmount().compareTo(minimumBid) >= 0) {
			Database.setBidOnItem(bid.getItemId(), bid.getAmount());
			item.setHighestBid(bid.getAmount());
			item.setHighestBidderId(bid.getBidderId());
			return item;
		}

		return null;
	}

}
