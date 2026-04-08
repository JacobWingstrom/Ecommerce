package model;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Item {

	private String username;
	private String description;
	private String tag;
	private BigDecimal highestBid;
	private int highestBidderId;
	private int itemId;
	private LocalDateTime end_time;
	private byte[] image;

	public Item(String username, String description, String tag, LocalDateTime end_time, byte[] image) {
		this.itemId = -1;
		this.username = username;
		this.description = description;
		this.tag = tag;
		this.highestBid = BigDecimal.ZERO;
		this.highestBidderId = -1;
		this.end_time = end_time;
		this.image = image;

	}

	public Item(String username, String description, String tag, BigDecimal highestBid, LocalDateTime end_time,
			byte[] image) {
		this.itemId = -1;
		this.username = username;
		this.description = description;
		this.tag = tag;
		this.highestBid = highestBid;
		this.highestBidderId = -1;
		this.end_time = end_time;
		this.image = image;

	}

	public Item(String username, String description, String tag, int itemId, BigDecimal highestBid,
			LocalDateTime end_time, byte[] image) {
		this.itemId = itemId;
		this.username = username;
		this.description = description;
		this.tag = tag;
		this.highestBid = highestBid;
		this.highestBidderId = -1;
		this.end_time = end_time;
		this.image = image;
	}

	public Item(Item item) {
		this.username = item.username;
		this.description = item.description;
		this.itemId = item.itemId;
		this.tag = item.tag;
		this.highestBid = item.highestBid;
		this.highestBidderId = item.highestBidderId;
		this.image = item.image;
		this.end_time = item.end_time;
	}

	@JsonIgnore
	public Item getItem() {
		return new Item(this);
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

	public int getHighestBidderId() {
		return highestBidderId;
	}

	public int getItemId() {
		return this.itemId;
	}

	// NEED TO UPDATE IN DB
	public void setHighestBidderId(int highestBidderId) {
		this.highestBidderId = highestBidderId;
	}

	public LocalDateTime getEndTime() {
		return this.end_time;
	}

	public static byte[] loadImage(String path) {
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

	public byte[] getImage() {
		return image;
	}
}
