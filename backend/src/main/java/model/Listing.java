package model;

import java.util.ArrayList;

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
		this.itemListings.clear();
		for (Item item : itemListings.getItemListings()) {
			this.itemListings.add(new Item(item));
		}
	}
	
	// NEED TO ADD PARAMS
	public boolean addItem() {
		return false;
		
	}
	
	// NEED TO ADD PARAMS
	public boolean removeItem() {
		return false;
		
	}
	
	// NEED TO ADD PARAMS
	public boolean editItem() {
		return false;
	}

}
