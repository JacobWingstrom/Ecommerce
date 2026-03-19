package model;

import java.time.LocalDate;
import java.util.Map;

public class Seller{

	private Availabilty availabilty;
	private Listing soldList; // All Items that have been sold
	private Listing sellingList; // All Currently Selling Items

	public Seller(String username, String password, String area) {
		//super(username, password, area);
	}

	public Seller(String username, String password, String salt, String area, int userId) {
		//super(username, password, salt, area, userId);
	}

	public Map<LocalDate, AvailabiltyBlock> getAvailabilty() {
		return availabilty.getAvailableTimes();
	}

	public Listing getSoldList() {
		return new Listing(soldList);
	}

	public Listing getSellingList() {
		return new Listing(sellingList);
	}

	public boolean addSoldList(Item item) {
		this.soldList.addItem(new Item(item));
		return true;
	}

	public boolean addSellingList(Item item) {
		this.sellingList.addItem(new Item(item));
		return true;
	}

	// ONCE USER IS NO LONGER Selling AN ITEM (Either sold or took it off)
	public boolean removeSellingList(Item item) {
		if (this.sellingList.removeItem(item)) {
			return true;
		}
		return false;
	}

}
