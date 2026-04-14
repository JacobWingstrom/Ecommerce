package dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import model.Availability;
import model.AvailabilityBlock;

public class AvailibiltyResponse {

	private String token;
	private boolean[][] availability;

	public AvailibiltyResponse(String token, Availability availability) {
		this.token = token;
		this.availability = createBinaryAvailability(availability);
	}

	public String getToken() {
		return this.token;
	}

	public boolean[][] getAvailability() {
		return this.availability;
	}

	private static boolean[][] createBinaryAvailability(Availability availability) {

		// Group blocks by date
		Map<LocalDate, ArrayList<AvailabilityBlock>> blocksByDate = availability.getBlocksByDate();

		// Determine how many days we need
		int numDays = blocksByDate.size();
		boolean[][] binary = new boolean[numDays][24]; // 24 half-hour slots

		// Sort dates so rows are in chronological order
		ArrayList<LocalDate> dates = new ArrayList<>(blocksByDate.keySet());
		Collections.sort(dates);

		for (int dayIndex = 0; dayIndex < dates.size(); dayIndex++) {

			LocalDate date = dates.get(dayIndex);
			ArrayList<AvailabilityBlock> blocks = blocksByDate.get(date);

			for (AvailabilityBlock block : blocks) {

				LocalTime start = block.getStart();
				LocalTime end = block.getEnd();

				// Convert times to slot indices
				int startSlot = timeToSlot(start);
				int endSlot = timeToSlot(end);

				// Mark all half-hour increments inside the block
				for (int slot = startSlot; slot < endSlot; slot++) {
					if (slot >= 0 && slot < 24) {
						binary[dayIndex][slot] = true;
					}
				}
			}
		}

		return binary;
	}

	private static int timeToSlot(LocalTime time) {

		int hour = time.getHour();
		int minute = time.getMinute();

		// Validate range
		if (hour < 7 || hour > 19 || (hour == 19 && minute > 0)) {
			return -1; // outside 7am–7pm
		}

		int slot = 0;

		// Count full hours past 7am
		for (int h = 7; h < hour; h++) {
			slot += 2; // each hour = 2 half-hour slots
		}

		// Add the half-hour if needed
		if (minute >= 30) {
			slot += 1;
		}

		return slot;
	}

}
