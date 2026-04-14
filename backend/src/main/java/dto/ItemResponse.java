package dto;

import model.Item;

public class ItemResponse {
	private Item item;

	public ItemResponse(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return this.item;
	}
}
