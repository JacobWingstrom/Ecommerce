package model;

public class Buyer extends Account {
	
	private Listing boughtList; // All Items that have been Bough
	private Listing bidList; // All Items that User is currently bidding on
	
	public Buyer(String username, String password, String area) {
		super(username, password, area);
		this.boughtList = new Listing();
		this.bidList = new Listing();
	}
	public Buyer(String username, String password, String salt, String area, int userId){
		super(username, password, salt, area, userId);
	}
	
	// Deep Copy Constructor
	public Buyer(Buyer buyer) {
		super(buyer.getUsername(), buyer.getPassword(), buyer.getArea());
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
