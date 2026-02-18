package model;

import java.time.LocalDate;
import java.util.Map;

public class Seller extends Account {
	
	private Availabilty availabilty;
	private Listing soldList; // All Items that have been sold
	private Listing sellingList; // All Currently Selling Items

	public Seller(String username, String password) {
		super(username, password);
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
	
	
}
