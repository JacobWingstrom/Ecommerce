package model;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Item {

	private String username;
	private String description;
	private String tag;
	private BigDecimal highestBid;
	private Buyer highestBuyer;
	private int itemId;
	private LocalDateTime end_time;
	private byte[] image;
	public Item(String username, String description, String tag, LocalDateTime end_time, byte[] image) {
		this.itemId = -1;
		this.username = username;
		this.description = description;
		this.tag = tag;
		this.highestBid = new BigDecimal(0.0);
		this.highestBuyer = null;
		this.end_time = end_time;
		this.image = image;

	}

	public Item(String username, String description, String tag, BigDecimal highestBid, LocalDateTime end_time, byte[] image) {
		this.itemId = -1;
		this.username = username;
		this.description = description;
		this.tag = tag;
		this.highestBid = highestBid;
		this.highestBuyer = null;
		this.end_time = end_time;
		this.image = image;

	}
	public Item(String username, String description, String tag, int itemId, BigDecimal highestBid, LocalDateTime end_time, byte[] image){
		this.itemId = itemId;
		this.username = username;
		this.description = description;
		this.tag = tag;
		this.highestBid = highestBid;
		this.highestBuyer = null;
		this.end_time = end_time;
		this.image = image;
	}

	public Item(Item item) {
		this.username = item.username;
		this.description = item.description;
		this.tag = item.tag;
		this.highestBid = item.highestBid;
		this.highestBuyer = item.highestBuyer != null ? new Buyer(item.highestBuyer) : null;
		this.image = item.image;
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

	public BigDecimal getHighestBid() {
		return highestBid;
	}

	// NEED TO UPDATE IN DB
	public void setHighestBid(BigDecimal highestBid) {
		this.highestBid = highestBid;
	}

	public Buyer getHighestBuyer() {
		return highestBuyer;
	}

	public int getItemId(){
		return this.itemId;
	}

	// NEED TO UPDATE IN DB
	public void setHighestBuyer(Buyer highestBuyer) {
		// Deep Copy
		this.highestBuyer = new Buyer(highestBuyer);
	}

	public LocalDateTime getEndTime(){
		return this.end_time;
	}
	public static byte[] loadImage(String path){
		try {
			File file = new File(path);
			byte[] data = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(data);
			fis.close();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public byte[] getImage(){
		return image;
	}
}
