package dto;

import java.util.ArrayList;

import model.Item;
import model.Listing;

public class ItemResponse {
    private Item item;

	public ItemResponse(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return this.item;
	}
}
