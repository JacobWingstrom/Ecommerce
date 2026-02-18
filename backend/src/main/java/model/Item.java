package model;

public class Item {

	private String username;
	private String description;
	private String tag;
	private Double highestBid;
	private Buyer highestBuyer;

	public Item(String username, String description, String tag) {

		this.username = username;
		this.description = description;
		this.tag = tag;
		this.highestBid = 0.0;
		this.highestBuyer = null;

	}

	public Item(Item item) {

		this.username = item.username;
		this.description = item.description;
		this.tag = item.tag;
		this.highestBid = item.highestBid;
		this.highestBuyer = new Buyer(item.highestBuyer);
	}

	public String getUsername() {
		return username;
	}

	// INCASE USER CHANGES THEIR USERNAME
	public void setUsername(String username) {
		this.username = username;
	}

	public String getDescription() {
		return description;
	}

	// NEED TO UPDATE IN DB
	public void setDescription(String description) {
		this.description = description;
	}

	public String getTag() {
		return tag;
	}

	// NEED TO UPDATE IN DB
	public void setTag(String tag) {
		this.tag = tag;
	}

	public Double getHighestBid() {
		return highestBid;
	}

	// NEED TO UPDATE IN DB
	public void setHighestBid(Double highestBid) {
		this.highestBid = highestBid;
	}

	public Buyer getHighestBuyer() {
		return highestBuyer;
	}

	// NEED TO UPDATE IN DB
	public void setHighestBuyer(Buyer highestBuyer) {
		// Deep Copy
		this.highestBuyer = new Buyer(highestBuyer);
	}

}
