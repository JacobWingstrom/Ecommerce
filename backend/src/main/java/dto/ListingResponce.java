package dto;

import java.util.ArrayList;
import java.util.List;

import model.Item;
import model.Listing;

public class ListingResponce {

	private Listing items;

	public ListingResponce(List<Item> items) {
		this.items = new Listing(items);
	}

	public ArrayList<Item> getItems() {
		return this.items.getItemListings();
	}

}
