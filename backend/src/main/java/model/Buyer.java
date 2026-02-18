package model;

public class Buyer extends Account {
	
	private Listing boughtList; // All Items that have been Bough
	private Listing bidList; // All Items that User is currently bidding on
	
	public Buyer(String username, String password) {
		super(username, password);
		this.boughtList = new Listing();
		this.bidList = new Listing();
	}
	
	// Deep Copy Constructor
	public Buyer(Buyer buyer) {
		super(buyer.getUsername(), buyer.getPassword());
		this.boughtList = buyer.getBoughtList();
		this.bidList = buyer.getBidList();
	}

	public Listing getBoughtList() {
		return new Listing(boughtList);
	}

	public Listing getBidList() {
		return new Listing(bidList);
	}
}
