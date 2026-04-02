package model;

import java.util.ArrayList;
import java.util.List;

public class Listing {

	private ArrayList<Item> itemListings;

	public Listing() {
		this.itemListings = new ArrayList<Item>();
	}

	public ArrayList<Item> getItemListings() {
		return new ArrayList<>(itemListings);
	}

	// COPY CONSTRUCTOR
	public Listing(Listing itemListings) {
		// Deep copy
		this.itemListings = new ArrayList<Item>();
		this.itemListings.clear();
		for (Item item : itemListings.getItemListings()) {
			this.itemListings.add(new Item(item));
		}
	}

	
	public Listing(List<Item> items) {
		this.itemListings = new ArrayList<Item>();
		this.itemListings.clear();
		for (Item item : items) {
			this.itemListings.add(new Item(item));
		}
	}

	// NEED TO ADD PARAMS
	public boolean addItem(Item item) {
		this.itemListings.add(new Item(item));
		return true;

	}

	// NEED TO ADD PARAMS
	public boolean removeItem(Item item) {
		// FIX THIS -> WONT ACTUALLY REMOVE
		if (this.itemListings.remove(item)) {
			return true;
		}
		return false;

	}

	// NEED TO ADD PARAMS
	public boolean editItem() {
		return false;
	}
}
