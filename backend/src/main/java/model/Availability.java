package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Availability {
	private int user_id;

	private ArrayList<AvailabilityBlock> availabilityBlocks;

	public Availability() {
		this.availabilityBlocks = new ArrayList<AvailabilityBlock>();
	}

	public Map<LocalDate, ArrayList<AvailabilityBlock>> getBlocksByDate() {
		Map<LocalDate, ArrayList<AvailabilityBlock>> blocksByDate = new HashMap<LocalDate, ArrayList<AvailabilityBlock>>();
		for (AvailabilityBlock block : this.availabilityBlocks) {
			blocksByDate.computeIfAbsent(block.getDate(), k -> new ArrayList<>()).add(block);
		}
		return blocksByDate;
	}

	public ArrayList<AvailabilityBlock> getBlocks() {
		return this.availabilityBlocks;
	}

	public boolean addBlock(AvailabilityBlock block) {
		if (block.getUserId() != null && block.getUserId() != user_id) {
			return false;
		}
		return this.availabilityBlocks.add(block);
	}

	public boolean removeBlock(int blockId) {
		return this.availabilityBlocks.removeIf(block -> block.getBlockId() == blockId);
	}

}
